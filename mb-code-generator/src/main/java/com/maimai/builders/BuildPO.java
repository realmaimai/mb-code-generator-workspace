package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import com.maimai.utils.DateUtils;
import com.maimai.utils.ResourceUtils;
import com.maimai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

@Slf4j
public class BuildPO {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName() + ".java");

        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            out = Files.newOutputStream(poFile.toPath());
            outputStreamWriter = new OutputStreamWriter(out);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("package " + Constants.PACKAGE_PO + ";");
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
                bufferedWriter.write("import " + Constants.BEAN_DATE_FORMAT_SERIALIZATION_CLASS + ";");
                bufferedWriter.newLine();
                bufferedWriter.write("import " + Constants.BEAN_DATE_FORMAT_DESERIALIZATION_CLASS + ";");
                bufferedWriter.newLine();
            }
            bufferedWriter.write("import java.io.Serializable;");
            bufferedWriter.newLine();

            // add jsonignore class
            boolean haveIgnore = false;
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), fieldInfo.getPropertyName())) {
                    haveIgnore = true;
                }
            }
            if (haveIgnore) {
                bufferedWriter.write(Constants.IGNORE_BEAN_TOJSON_CLASS + ";");
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();

            BuildComment.createClassComment(bufferedWriter, tableInfo.getComment());
            // add lombok
            bufferedWriter.write("@Data");
            bufferedWriter.newLine();
            bufferedWriter.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bufferedWriter.newLine();

            // loop field info list, set field in class
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                // create comment
                BuildComment.createFieldComment(bufferedWriter, fieldInfo.getComment());
                // check if it has specific data type
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    bufferedWriter.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_SERIALIZATION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bufferedWriter.newLine();
                    bufferedWriter.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_DESERIALIZATION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bufferedWriter.newLine();
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    bufferedWriter.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_SERIALIZATION, DateUtils.YYYY_MM_DD));
                    bufferedWriter.newLine();
                    bufferedWriter.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_DESERIALIZATION, DateUtils.YYYY_MM_DD));
                    bufferedWriter.newLine();
                }

                // add @JsonIgnore annotation
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), fieldInfo.getPropertyName())) {
                    bufferedWriter.write("\t" + String.format(Constants.IGNORE_BEAN_TOJSON_EXPRESSION));
                    bufferedWriter.newLine();
                }

                bufferedWriter.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            bufferedWriter.write("}");
            bufferedWriter.flush();

        } catch (Exception e) {
            log.info("build po error: " + e);
        } finally {
            ResourceUtils.closeQuietly(out, outputStreamWriter, bufferedWriter, log);
        }

    }
}
