package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
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
            out = new FileOutputStream(poFile);
            outputStreamWriter = new OutputStreamWriter(out);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("package " + Constants.PACKAGE_PO + ";");
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
            bufferedWriter.write("import java.io.Serializable;");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            //
            BuildComment.createClassComment(bufferedWriter, tableInfo.getComment());

            bufferedWriter.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bufferedWriter.newLine();

            // loop field info list
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                BuildComment.createFieldComment(bufferedWriter, fieldInfo.getComment());
                bufferedWriter.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
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
