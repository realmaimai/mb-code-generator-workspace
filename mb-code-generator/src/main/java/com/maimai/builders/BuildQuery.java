package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import com.maimai.utils.DateUtils;
import com.maimai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

@Slf4j
public class BuildQuery {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_QUERY);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;
        File queryFile = new File(folder, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            out = Files.newOutputStream(queryFile.toPath());
            outputStreamWriter = new OutputStreamWriter(out);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("package " + Constants.PACKAGE_QUERY + ";");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("import lombok.Data;");
            bufferedWriter.newLine();

            if (tableInfo.isHaveBigDecimal()) {
                bufferedWriter.write("import java.math.BigDecimal;");
                bufferedWriter.newLine();
            }
            if (tableInfo.isHaveDate() || tableInfo.isHaveDateTime()) {
                bufferedWriter.write("import java.util.Date;");
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();

            BuildComment.createClassComment(bufferedWriter, tableInfo.getComment());
            // add lombok
            bufferedWriter.write("@Data");
            bufferedWriter.newLine();
            bufferedWriter.write("public class " + className + " extends BaseQuery {");
            bufferedWriter.newLine();

            // loop field info list, set field in class
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                // create comment
                BuildComment.createFieldComment(bufferedWriter, fieldInfo.getComment());

                bufferedWriter.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    bufferedWriter.write(("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_FUZZY + ";"));
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) ) {
                    bufferedWriter.write(("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_TIME_START+ ";"));
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                    bufferedWriter.write(("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_TIME_END+ ";"));
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.write("}");
            bufferedWriter.flush();

        } catch (Exception e) {
            log.info("file execution error: " + e);
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
