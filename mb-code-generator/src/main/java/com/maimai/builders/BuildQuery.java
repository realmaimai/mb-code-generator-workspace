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
            bufferedWriter.write("public class " + className + "{");
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
                }
            }

            // getter setter methods
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                String upperFieldName = StringUtils.firstLetterUpperCase(fieldInfo.getPropertyName());
                // getter
                bufferedWriter.write("\tpublic " + fieldInfo.getJavaType() + " get" + upperFieldName + "()" + "{");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\treturn this." + fieldInfo.getPropertyName() + ";");
                bufferedWriter.newLine();
                bufferedWriter.write("\t}");
                bufferedWriter.newLine();
                bufferedWriter.newLine();

                // setter
                bufferedWriter.write("\tpublic void set" + upperFieldName + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                bufferedWriter.newLine();
                bufferedWriter.write("\t\tthis." + fieldInfo.getPropertyName()+ " = " + fieldInfo.getPropertyName() + ";");
                bufferedWriter.newLine();
                bufferedWriter.write("\t}");
                bufferedWriter.newLine();
                bufferedWriter.newLine();
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
