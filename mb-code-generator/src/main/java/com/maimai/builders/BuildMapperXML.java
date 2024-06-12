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
    private static final String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";
    private static final String QUERY_CONDITION = "query_condition";

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPERS_XMLS);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;

        File poFile = new File(folder, className + ".xml");

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
            bw.write("<mapper namespace=\""+Constants.PACKAGE_MAPPERS + "." + className +"\">");
            bw.newLine();
            bw.write("\t<!-- entity mapping -->");
            bw.newLine();
            String poClass = Constants.PACKAGE_PO + "." +tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"base_result_map\" type=\""+poClass+"\">");
            bw.newLine();

            FieldInfo idField = null;
            Set<Map.Entry<String, List<FieldInfo>>> keyIndexMap = tableInfo.getKeyIndexMap().entrySet();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap) {
                if("PRIMARY".equals(entry.getKey())){
                    List<FieldInfo> fieldInfoList = entry.getValue();
                    if(fieldInfoList.size()==1){
                        idField = fieldInfoList.get(0);
                        break;
                    }
                }
            }

            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                bw.write("\t<!--"+fieldInfo.getComment()+"-->");
                bw.newLine();
                // primary key id
                String key = "";
                if(idField!=null && fieldInfo.getPropertyName().equals(idField.getPropertyName())){
                    key = "id";
                }
                else{
                    key = "result";
                }
                bw.write("\t\t<"+key+" column=\""+fieldInfo.getFieldName()+"\" property=\""+fieldInfo.getPropertyName()+"\"/>");
                bw.newLine();
            }

            bw.write("\t</resultMap>");
            bw.newLine();
            bw.newLine();

            bw.write("\t<!-- base column list -->");
            bw.newLine();
            bw.write("\t<sql id=\""+BASE_COLUMN_LIST+"\">");
            bw.newLine();
            StringBuilder columnBuilder = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                columnBuilder.append(fieldInfo.getFieldName()).append(",");
            }
            String columnBuilderStr = columnBuilder.substring(0,columnBuilder.lastIndexOf(","));
            bw.write("\t\t"+columnBuilderStr);
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            bw.write("<!-- base conditional query -->");
            bw.newLine();
            bw.write("\t<sql id=\""+BASE_QUERY_CONDITION+"\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                String stringQuery = "";
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPE,fieldInfo.getSqlType())){
                    stringQuery = " and query." + fieldInfo.getPropertyName() + " != ''";
                }

                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null"+stringQuery+"\">");
                bw.newLine();
                bw.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}");
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            bw.write("<!-- base extended conditional query-->");
            bw.newLine();
            bw.write("\t<sql id=\""+BASE_QUERY_CONDITION_EXTEND+"\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getExtendFieldInfoList()) {
                String andWhere = "";
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPE,fieldInfo.getSqlType())){
                    andWhere = "and "+ fieldInfo.getFieldName() + " like concat('%', #{query."+fieldInfo.getPropertyName()+"},'%')" + " != ''";
                }else if(ArrayUtils.contains(Constants.SQL_DATE_TYPES,fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType())){
                    if(fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_TIME_START)){
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query."+ fieldInfo.getPropertyName() + "}, '%Y-%m-%d') ]]>";
                    }
                    else if(fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_TIME_END)){
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query."+ fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), interval -1 day) ]]>";
                    }
                }

                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + " != ''\">");
                bw.newLine();
                bw.write("\t\t\t"+andWhere);
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();


            bw.write("<!-- conditional query -->");
            bw.newLine();
            bw.write("\t<sql id=\""+QUERY_CONDITION+"\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\""+BASE_QUERY_CONDITION+"\"/>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\""+BASE_QUERY_CONDITION_EXTEND+"\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            bw.write("<!-- select query -->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tSELECT <include refid=\"" + BASE_COLUMN_LIST + "\"/> FROM " + tableInfo.getTableName() + " <include refid=\""+QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.orderBy!=null\"> order by ${query.orderBy} </if>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.paginator!=null\"> limit ${query.paginator.start},${query.paginator.end} </if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();

            bw.write("<!-- count query -->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Long\">");
            bw.newLine();
            bw.write("\t\tselect count(1) FROM "+ tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<include refid=\""+QUERY_CONDITION+"\"/>");
            bw.newLine();
            bw.write("\t</select>");
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
}
