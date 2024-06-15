package com.maimai.utils;

import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class ResourceUtils {
    public static void closeQuietly(OutputStream out, OutputStreamWriter outputStreamWriter, BufferedWriter bufferedWriter, Logger log) {
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
