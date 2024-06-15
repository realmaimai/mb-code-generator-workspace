package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.TableInfo;
import com.maimai.utils.ResourceUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;

@Slf4j
public class BuildService {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName() + "Service.java");

        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            out = Files.newOutputStream(poFile.toPath());
            outputStreamWriter = new OutputStreamWriter(out);
            bw = new BufferedWriter(outputStreamWriter);

            bw.write("package " + Constants.PACKAGE_SERVICE + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." +tableInfo.getBeanName()+ ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." +tableInfo.getBeanParamName()+ ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO+ ".PaginationResultVO"+ ";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");

            BuildComment.createClassComment(bw, tableInfo.getComment() + " Service");
            bw.write("public interface " + tableInfo.getBeanName() + "Service {");
            bw.newLine();
            bw.newLine();
            bw.write("\tList<" + tableInfo.getBeanName() + "> getListByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();
            bw.write("\tLong getCountByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();
            bw.write("\tPaginationResultVO<"+ tableInfo.getBeanName()+"> getListByPage(" + tableInfo.getBeanParamName() + " query);");

            bw.newLine();
            bw.write("}");
            bw.flush();

        } catch (Exception e) {
            log.info("build service error: " + e);
        } finally {
            ResourceUtils.closeQuietly(out, outputStreamWriter, bw, log);
        }

    }
}
