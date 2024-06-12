package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

public class BuildComment {
    public static void createClassComment(BufferedWriter bw, String classComment) throws IOException {
        bw.write("/**");
        bw.newLine();
        bw.write(" * @Description: " + classComment);
        bw.newLine();
        bw.write(" *");
        bw.newLine();
        bw.write(" * @Author: " + Constants.COMMENT_AUTHOR);
        bw.newLine();
        bw.write(" * @Date: " + DateUtils.format(new Date(), DateUtils.YYYYMMDD));
        bw.newLine();
        bw.write(" */");
        bw.newLine();
    }

    public static void createFieldComment(BufferedWriter bw, String fieldComment) throws IOException {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + (fieldComment == null ? "" : fieldComment));
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    }

    public static void createMethodComment(BufferedWriter bw, String methodComment) throws IOException {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + (methodComment == null ? "" : methodComment));
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    }
}
