package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import com.maimai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class BuildMapperXML {

    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITION = "base_query_condition";
    private static final String QUERY_CONDITION = "query_condition";

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPERS_XMLS);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        File poFile = new File(folder, className + ".xml");

        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            out = Files.newOutputStream(poFile.toPath());
            outputStreamWriter = new OutputStreamWriter(out);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            // generate xml header
            bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            bufferedWriter.newLine();
            bufferedWriter.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bufferedWriter.newLine();
            bufferedWriter.write("        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bufferedWriter.newLine();
            bufferedWriter.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPERS + "." + className + "\">");
            bufferedWriter.newLine();

            // generate entity mapping
            bufferedWriter.write("\t<!-- entity mapping -->");
            bufferedWriter.newLine();
            String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
            bufferedWriter.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">");
            bufferedWriter.newLine();

            FieldInfo idField = null;
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
                bufferedWriter.write("\t\t<!-- " + fieldInfo.getComment() + " -->");
                bufferedWriter.newLine();
                String key;
                if (idField != null && fieldInfo.getPropertyName().equals(idField.getPropertyName())) {
                    key = "id";
                } else {
                    key = "result";
                }
                bufferedWriter.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                bufferedWriter.newLine();
            }

            bufferedWriter.write("\t</resultMap>");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            // base sql query:
            // base column list
            bufferedWriter.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\t");
            StringBuilder columnBuilder = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                columnBuilder.append(fieldInfo.getFieldName()).append(", ");
            }
            columnBuilder = columnBuilder.deleteCharAt(columnBuilder.length() - 2);
            bufferedWriter.write(String.valueOf(columnBuilder));
            bufferedWriter.newLine();
            bufferedWriter.write("\t</sql>");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            // base query condition
            bufferedWriter.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
            bufferedWriter.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                String stringQuery = "";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    stringQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
                }
                bufferedWriter.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + stringQuery + "\">");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\t\tand " + fieldInfo.getPropertyName() + " = " + "#{query."+fieldInfo.getPropertyName()+"}");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\t</if>");
                bufferedWriter.newLine();
            }

            bufferedWriter.write("\t</sql>");
            bufferedWriter.newLine();

            // select expression
            bufferedWriter.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bufferedWriter.newLine();
            bufferedWriter.write("\t\tSELECT <include refid=\"" + BASE_COLUMN_LIST + "\"/> FROM " + tableInfo.getTableName() + " <include refid=\"" + QUERY_CONDITION + "\"/>");
            bufferedWriter.newLine();
            bufferedWriter.write("\t</select>");
            bufferedWriter.newLine();

            // end mapper tag
            bufferedWriter.write("</mapper>");
            bufferedWriter.flush();

        } catch (Exception e) {
            log.info("create mapper xml error: " + e);
        } finally {
            if (Objects.isNull(bufferedWriter)) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    log.info("buffered writer closing error: " + e);
                }
            }
            if (Objects.isNull(outputStreamWriter)) {
                try {
                    outputStreamWriter.close();
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
}
