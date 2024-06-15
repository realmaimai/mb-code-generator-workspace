package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import com.maimai.utils.StringUtils;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

@Slf4j
public class BuildMapperXML {

    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITION = "base_query_condition";
    private static final String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";
    private static final String QUERY_CONDITION = "query_condition";

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPERS_XMLS);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;

        File poFile = new File(folder, className + ".xml");

        // all column fields without auto increment field
        StringBuilder columnBuilder = new StringBuilder();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            if (fieldInfo.isAutoIncrement()) {
                continue;
            }
            columnBuilder.append(fieldInfo.getFieldName()).append(",");
        }
        String columnBuilderStr = columnBuilder.substring(0, columnBuilder.lastIndexOf(","));

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write("        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPERS + "." + className + "\">");
            bw.newLine();
            bw.write("\t<!-- entity mapping -->");
            bw.newLine();
            String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">");
            bw.newLine();

            FieldInfo idField = null;
            // get key index map from table information
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                if ("PRIMARY".equals(entry.getKey())) {
                    List<FieldInfo> fieldInfoList = entry.getValue();
                    if (fieldInfoList.size() == 1) {
                        idField = fieldInfoList.get(0);
                        break;
                    }
                }
            }

            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                bw.write("\t<!--" + fieldInfo.getComment() + "-->");
                bw.newLine();
                // primary key id
                String key = "";
                if (idField != null && fieldInfo.getPropertyName().equals(idField.getPropertyName())) {
                    key = "id";
                } else {
                    key = "result";
                }
                bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                bw.newLine();
            }

            bw.write("\t</resultMap>");
            bw.newLine();
            bw.newLine();

            // base column list
            generateBaseColumnList(bw, columnBuilderStr);

            // base conditional query
            generateBaseConditionalQuery(tableInfo, bw);
            bw.newLine();
            bw.newLine();

            // base extended conditional query (date, datetime, big decimal type)
            generateBaseExtendedConditionalQuery(tableInfo, bw);
            bw.newLine();
            bw.newLine();

            // conditional query
            generateConditionalQuery(bw);
            bw.newLine();
            bw.newLine();

            // select query
            generateSelectQuery(tableInfo, bw);
            bw.newLine();
            bw.newLine();

            // select count query
            generateSelectCountQuery(tableInfo, bw);
            bw.newLine();
            bw.newLine();

            // insert query
            generateInsertQuery(tableInfo, bw);
            bw.newLine();
            bw.newLine();

            // insert or update data based on index in database
            generateInsertOrUpdateQuery(tableInfo, bw, poClass, keyIndexMap);
            bw.newLine();
            bw.newLine();

            // batch insert
            generateBatchInsert(tableInfo, bw, poClass, columnBuilderStr);
            bw.newLine();
            bw.newLine();

            // batch insert or update
            generateBatchInsertOrUpdate(tableInfo, bw, poClass, columnBuilderStr);
            bw.newLine();
            bw.newLine();

            // CRUD by id
            generateCRUDByPk(tableInfo, bw, poClass);
            bw.newLine();
            bw.newLine();

            // end mapper tag
            bw.write("</mapper>");
            bw.flush();

        } catch (Exception e) {
            log.info("create mapper xml error: " + e);
        } finally {
            if (Objects.isNull(bw)) {
                try {
                    bw.close();
                } catch (IOException e) {
                    log.info("buffered writer closing error: " + e);
                }
            }
            if (Objects.isNull(outw)) {
                try {
                    outw.close();
                } catch (IOException e) {
                    log.info("output stream writer closing error: " + e);
                }
            }
            if (Objects.isNull(out)) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.info("output stream closing error: " + e);
                }
            }
        }

    }

    private static void generateCRUDByPk(TableInfo tableInfo, BufferedWriter bw, String poClass) throws IOException {
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
            List<FieldInfo> fieldInfoList = entry.getValue();

            // variables for writing expressions
            Integer index = 0;
            StringBuilder paramExpressionBuilder = new StringBuilder();
            StringBuilder methodName = new StringBuilder();
            for (FieldInfo fieldInfo : fieldInfoList) {
                ++index;
                methodName.append(StringUtils.firstLetterUpperCase(fieldInfo.getPropertyName()));
                paramExpressionBuilder.append(fieldInfo.getFieldName()).append("=#{").append(fieldInfo.getPropertyName()).append("}");
                if (index < fieldInfoList.size()) {
                    methodName.append("And");
                    paramExpressionBuilder.append(" and ");
                }
            }

            // select query
            bw.write("\t<!-- select query based on "+methodName+" -->");
            bw.newLine();
            bw.write("\t<select id=\"selectBy" + methodName + "\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tselect");
            bw.newLine();
            bw.write("\t\t<include refid=\""+BASE_COLUMN_LIST+"\"/>");
            bw.newLine();
            bw.write("\t\tfrom " + tableInfo.getTableName() + " where " + paramExpressionBuilder);
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();

            // delete query
            bw.write("\t<!-- delete query based on "+methodName+" -->");
            bw.newLine();
            bw.write("\t<delete id=\"deleteBy" + methodName + "\">");
            bw.newLine();
            bw.write("\t\tdelete");
            bw.newLine();
            bw.write("\t\tfrom " + tableInfo.getTableName() + " where " + paramExpressionBuilder);
            bw.newLine();
            bw.write("\t</delete>");
            bw.newLine();

            // update query
            bw.write("\t<!-- update query based on "+methodName+" -->");
            bw.newLine();
            bw.write("\t<update id=\"updateBy" + methodName + "\" parameterType=\""+ poClass +"\">");
            bw.newLine();
            bw.write("\t\tUPDATE " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<set>");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+"!= null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + "= #{bean."+fieldInfo.getPropertyName()+"},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</set>");
            bw.newLine();
            bw.write("\t\twhere " + paramExpressionBuilder);
            bw.newLine();
            bw.write("\t</update>");
            bw.newLine();
            bw.newLine();
        }
    }

    private static void generateBatchInsertOrUpdate(TableInfo tableInfo, BufferedWriter bw, String poClass, String columnBuilderStr) throws IOException {
        bw.write("\t<!-- batch insert or update query -->");
        bw.newLine();
        bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + poClass + "\">");
        bw.newLine();
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + " (" + columnBuilderStr + ") VALUES");
        bw.newLine();
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
        bw.newLine();
        StringBuilder insertPropertyBuilder = new StringBuilder();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            if (fieldInfo.isAutoIncrement()) {
                continue;
            }
            insertPropertyBuilder.append("#{item.").append(fieldInfo.getPropertyName()).append("}").append(",");
        }
        String insertPropertyBuilderStr = insertPropertyBuilder.substring(0, insertPropertyBuilder.lastIndexOf(","));
        bw.write("\t\t\t(" + insertPropertyBuilderStr + ")");
        bw.newLine();
        bw.write("\t\t</foreach>");
        bw.newLine();
        bw.write("\t\ton DUPLICATE key update");
        bw.newLine();
        StringBuilder batchInsertOrUpdateBuilder = new StringBuilder();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            batchInsertOrUpdateBuilder.append(fieldInfo.getFieldName()).append(" = VALUES(").append(fieldInfo.getFieldName()).append("),");
        }
        String batchInsertOrUpdateStr = batchInsertOrUpdateBuilder.substring(0, batchInsertOrUpdateBuilder.lastIndexOf(","));
        bw.write("\t\t" + batchInsertOrUpdateStr);
        bw.newLine();
        bw.write("\t</insert>");
    }

    private static void generateBaseColumnList(BufferedWriter bw, String columnBuilderStr) throws IOException {
        bw.write("\t<!-- base column list -->");
        bw.newLine();
        bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
        bw.newLine();
        bw.write("\t\t" + columnBuilderStr);
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
    }

    private static void generateBatchInsert(TableInfo tableInfo, BufferedWriter bw, String poClass, String columnBuilderStr) throws IOException {
        bw.write("\t<!-- batch insert query -->");
        bw.newLine();
        bw.write("\t<insert id=\"insertBatch\" parameterType=\"" + poClass + "\">");
        bw.newLine();
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + " (" + columnBuilderStr + ") VALUES");
        bw.newLine();
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
        bw.newLine();
        StringBuilder insertPropertyBuilder = new StringBuilder();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            if (fieldInfo.isAutoIncrement()) {
                continue;
            }
            insertPropertyBuilder.append("#{item.").append(fieldInfo.getPropertyName()).append("}").append(",");
        }
        String insertPropertyBuilderStr = insertPropertyBuilder.substring(0, insertPropertyBuilder.lastIndexOf(","));
        bw.write("\t\t\t(" + insertPropertyBuilderStr + ")");
        bw.newLine();
        bw.write("\t\t</foreach>");
        bw.newLine();
        bw.write("\t</insert>");
    }

    private static void generateInsertOrUpdateQuery(TableInfo tableInfo, BufferedWriter bw, String poClass, Map<String, List<FieldInfo>> keyIndexMap) throws IOException {
        bw.write("\t<!-- insertOrUpdate query -->");
        bw.newLine();
        bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + poClass + "\">");
        bw.newLine();
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
        bw.newLine();
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        bw.newLine();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "\">");
            bw.newLine();
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.write("\t\t</trim>");
        bw.newLine();
        bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
        bw.newLine();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "!=null\">");
            bw.newLine();
            bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.write("\t\t</trim>");
        bw.newLine();
        bw.write("\t\ton DUPLICATE key update");

        // avoid modifying unique indexes to prevent disrupting the overall structure of the data table
        HashMap<String, String> indexTempMap = getIndexTempMap(keyIndexMap);

        bw.newLine();
        bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
        bw.newLine();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            // if this is an index then stop
            if (indexTempMap.get(fieldInfo.getFieldName()) != null) {
                continue;
            }
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "!=null\">");
            bw.newLine();
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + "),");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.write("\t\t</trim>");
        bw.newLine();

        bw.write("\t</insert>");
    }

    private static void generateInsertQuery(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!-- single insert query -->");
        bw.newLine();
        bw.write("\t<insert id=\"insert\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
        bw.newLine();

        FieldInfo autoIncrementField = null;
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            if (fieldInfo.isAutoIncrement()) {
                autoIncrementField = fieldInfo;
                break;
            }
        }

        if (autoIncrementField != null) {
            bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncrementField.getFieldName() + "\" resultType=\"" + autoIncrementField.getJavaType() + "\" order=\"AFTER\">");
            bw.newLine();
            bw.write("\t\t\tSELECT LAST_INSERT_ID()");
            bw.newLine();
            bw.write("\t\t</selectKey>");
        }

        bw.newLine();
        bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
        bw.newLine();
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        bw.newLine();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "\">");
            bw.newLine();
            bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.write("\t\t</trim>");
        bw.newLine();
        bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
        bw.newLine();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "!=null\">");
            bw.newLine();
            bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.write("\t\t</trim>");
        bw.newLine();
        bw.write("\t</insert>");
    }

    private static void generateSelectCountQuery(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!-- count query -->");
        bw.newLine();
        bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Long\">");
        bw.newLine();
        bw.write("\t\tselect count(1) FROM " + tableInfo.getTableName());
        bw.newLine();
        bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>");
        bw.newLine();
        bw.write("\t</select>");
    }

    private static void generateSelectQuery(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!-- select query -->");
        bw.newLine();
        bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
        bw.newLine();
        bw.write("\t\tSELECT <include refid=\"" + BASE_COLUMN_LIST + "\"/> FROM " + tableInfo.getTableName() + " <include refid=\"" + QUERY_CONDITION + "\"/>");
        bw.newLine();
        bw.write("\t\t<if test=\"query.orderBy!=null\"> order by ${query.orderBy} </if>");
        bw.newLine();
        bw.write("\t\t<if test=\"query.paginator!=null\"> limit ${query.paginator.start},${query.paginator.end} </if>");
        bw.newLine();
        bw.write("\t</select>");
    }

    private static void generateConditionalQuery(BufferedWriter bw) throws IOException {
        bw.write("\t<!-- conditional query -->");
        bw.newLine();
        bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">");
        bw.newLine();
        bw.write("\t\t<where>");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION_EXTEND + "\"/>");
        bw.newLine();
        bw.write("\t\t</where>");
        bw.newLine();
        bw.write("\t</sql>");
    }

    private static void generateBaseExtendedConditionalQuery(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!-- base extended conditional query-->");
        bw.newLine();
        bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION_EXTEND + "\">");
        bw.newLine();
        for (FieldInfo fieldInfo : tableInfo.getExtendFieldInfoList()) {
            String andWhere = "";
            if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                andWhere = "and " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "},'%')" + " != ''";
            } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_TIME_START)) {
                    andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>";
                } else if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_TIME_END)) {
                    andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), interval -1 day) ]]>";
                }
            }

            bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + " != ''\">");
            bw.newLine();
            bw.write("\t\t\t" + andWhere);
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
        }
        bw.write("\t</sql>");
    }

    private static void generateBaseConditionalQuery(TableInfo tableInfo, BufferedWriter bw) throws IOException {
        bw.write("\t<!-- base conditional query -->");
        bw.newLine();
        bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
        bw.newLine();
        for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            String stringQuery = "";
            if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                stringQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
            }

            bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + stringQuery + "\">");
            bw.newLine();
            bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}");
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
        }
        bw.write("\t</sql>");
    }

    /**
     * Constructs a temporary map of index field names from the provided key index map.
     *
     * <p>This method iterates over the entries of the given key index map, which is a map where the keys are strings
     * and the values are lists of {@link FieldInfo} objects. It extracts the field names from each {@link FieldInfo}
     * object and adds them to a new {@link HashMap}, using the field name as both the key and the value.</p>
     *
     * @param keyIndexMap the map containing lists of {@link FieldInfo} objects, which are also database's index
     * @return a {@link HashMap} where each key-value pair consists of a field name from the {@link FieldInfo} objects
     */
    private static HashMap<String, String> getIndexTempMap(Map<String, List<FieldInfo>> keyIndexMap) {
        HashMap<String, String> indexTempMap = new HashMap();
        for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
            // index list
            List<FieldInfo> fieldInfoList = entry.getValue();
            for (FieldInfo fieldInfo : fieldInfoList) {
                // index temp map
                indexTempMap.put(fieldInfo.getFieldName(), fieldInfo.getFieldName());
            }
        }
        return indexTempMap;
    }
}
