package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import com.maimai.utils.PropertiesUtils;
import com.maimai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO change this class to DatabaseUtils
@Slf4j
public class BuildTable {
    private static Connection connection = null;
    private static final String SQL_SHOW_TABLE_STATUS = "show table status";
    private static final String SQL_SHOW_FIELDS= "show full fields from %s";

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

    public static void getTables() {
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
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);
                log.info("table name: {}, table bean name: {}, table bean param name: {}", tableInfo.getTableName(),
                        tableInfo.getBeanName(), tableInfo.getBeanParamName());
                readFieldInfo(tableInfo);

                tableInfoList.add(tableInfo);
            }
        } catch (Exception e) {
            log.info("get table from connection error: " + e);
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
    }

    public static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        List<TableInfo> fieldInfoList = new ArrayList<>();

        try {
            ps = connection.prepareStatement(String.format(SQL_SHOW_FIELDS, tableInfo.getTableName()));
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
                fieldName = processField(fieldName, false);
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setComment(comment);
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));
                fieldInfo.setSqlType(fieldType);

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
        return null;
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
        // first sub split value if need to uppercase
        sb.append(firstLetterUpperCase? StringUtils.firstLetterUpperCase(split[0]) : split[0]);
        for (int i=1; i<split.length;i++) {
            String s = StringUtils.firstLetterUpperCase(split[i]);
            sb.append(s);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        getTables();
    }
}
