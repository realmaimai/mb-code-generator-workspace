package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import com.maimai.utils.JsonUtils;
import com.maimai.utils.PropertiesUtils;
import com.maimai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.*;
import java.util.*;

@Slf4j
public class BuildTable {
    private static Connection connection = null;
    private static final String SQL_SHOW_TABLE_STATUS = "show table status";
    private static final String SQL_SHOW_TABLE_FIELDS= "show full fields from %s";
    private static final String SQL_SHOW_TABLE_INDEX= "show index from %s";

    static {
        String driverName = PropertiesUtils.getString("db", "driver", "name");
        String url = PropertiesUtils.getString("db", "url");
        String user = PropertiesUtils.getString("db", "username");
        String password = PropertiesUtils.getString("db", "password");
        try {
            // connect to db via jdbc
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            log.info("error when connecting to db: " + e);
        }
    }

    public static List<TableInfo> getTables() {
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        List<TableInfo> tableInfoList = new ArrayList<>();

        try {
            ps = connection.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while (tableResult.next()) {
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = tableName.substring(tableName.indexOf("_") + 1);
                }
                // if it is a table name, then the first letter should be uppercase
                // if it is a field name, then should be lowercase
                beanName = processField(beanName, true);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setComment(comment);
                tableInfo.setBeanName(beanName);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_QUERY);
                readFieldInfo(tableInfo);
                getKeyIndexInfo(tableInfo);
                log.info("table info: " + JsonUtils.convertObj2Json(tableInfo));
                tableInfoList.add(tableInfo);
            }
        } catch (Exception e) {
            log.info("get table error: " + e);
        } finally {
            if (!Objects.isNull(ps)) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("prepare statement error: " + e);
                }
            }
            if (!Objects.isNull(tableResult)) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    log.info("result set error: " + e);
                }
            }
            if (!Objects.isNull(connection)) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.info("connection error: " + e);
                }
            }
        }
        return tableInfoList;
    }

    public static void readFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        List<FieldInfo> fieldInfoList = new ArrayList<>();

        boolean haveDate = false;
        boolean haveDateTime = false;
        boolean haveDecimal = false;

        try {
            ps = connection.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String fieldName = fieldResult.getString("field");
                String fieldType = fieldResult.getString("type");
                // check if it is auto-increment
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");

                if (fieldType.indexOf("(") > 0) {
                    fieldType = fieldType.substring(0, fieldType.indexOf("("));
                }
                String propertyName = processField(fieldName, false);
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setComment(comment);
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));
                fieldInfo.setSqlType(fieldType);
                fieldInfo.setJavaType(processJavaType(fieldType));

                // check table has special type
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldType)) {
                    haveDateTime = true;
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldType)) {
                    haveDate = true;
                }
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, fieldType)) {
                    haveDecimal = true;
                }
                tableInfo.setHaveDateTime(haveDateTime);
                tableInfo.setHaveDate(haveDate);
                tableInfo.setHaveBigDecimal(haveDecimal);

                fieldInfoList.add(fieldInfo);
                tableInfo.setFieldInfoList(fieldInfoList);

            }

        } catch (Exception e) {
            log.info("get field information from connection error: " + e);
        } finally {
            if (!Objects.isNull(ps)) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("prepare statement error: " + e);
                }
            }
            if (!Objects.isNull(fieldResult)) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    log.info("result set error: " + e);
                }
            }
        }
    }


    public static void getKeyIndexInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet indexResult = null;

        try {
            Map<String, FieldInfo> tempMap = new HashMap<>();

            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                tempMap.put(fieldInfo.getFieldName(), fieldInfo);
            }

            ps = connection.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            indexResult = ps.executeQuery();
            while (indexResult.next()) {
                String keyName = indexResult.getString("key_name");
                Integer nonUnique = indexResult.getInt("non_unique");
                // check if it is auto-increment
                String columnName = indexResult.getString("column_name");
                if (nonUnique == 1) {
                    continue;
                }
                Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
                List<FieldInfo> keyFieldList = keyIndexMap.get(keyName);
                if (Objects.isNull(keyFieldList)) {
                    keyFieldList = new ArrayList<>();
                    keyIndexMap.put(keyName, keyFieldList);
                }
                keyFieldList.add(tempMap.get(columnName));
            }

        } catch (Exception e) {
            log.info("get index information error: " + e);
        } finally {
            if (!Objects.isNull(ps)) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.info("prepare statement error: " + e);
                }
            }
            if (!Objects.isNull(indexResult)) {
                try {
                    indexResult.close();
                } catch (SQLException e) {
                    log.info("result set error: " + e);
                }
            }
        }
    }

    /**
     * handle string to camel case
     * @param field - your string
     * @param firstLetterUpperCase - boolean
     * @return camel case style string
     */
    private static String processField(String field, Boolean firstLetterUpperCase) {
        StringBuffer sb = new StringBuffer();
        String[] split = field.split("_");
        // first sub split value if you need to uppercase
        sb.append(firstLetterUpperCase? StringUtils.firstLetterUpperCase(split[0]) : split[0]);
        for (int i=1; i<split.length;i++) {
            String s = StringUtils.firstLetterUpperCase(split[i]);
            sb.append(s);
        }
        return sb.toString();
    }

    private static String processJavaType(String type) {
        if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,type)||ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,type)){
            return "Date";
        }
        else if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE,type)){
            return "BigDecimal";
        }else if(ArrayUtils.contains(Constants.SQL_STRING_TYPE,type)){
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE,type)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE,type)) {
            return "Long";
        }else {
            throw new RuntimeException("cannot recognize sql type:"+type);
        }
    }

    public static void main(String[] args) {
        getTables();
    }
}
