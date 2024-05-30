package com.maimai.builders;

import com.maimai.bean.Constants;
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
            out = new FileInputStream();
        } catch (Exception e) {

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
