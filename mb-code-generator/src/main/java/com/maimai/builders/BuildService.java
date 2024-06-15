package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import com.maimai.utils.ResourceUtils;
import com.maimai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

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
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO" + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();

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
            bw.write("\tPaginationResultVO<" + tableInfo.getBeanName() + "> getListByPage(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();
            bw.write("\tLong insert(" + tableInfo.getBeanName() + " bean);");
            bw.newLine();
            bw.newLine();
            bw.write("\tLong insertBatch(List<" + tableInfo.getBeanName() + "> beanList);");
            bw.newLine();
            bw.newLine();
            bw.write("\tLong insertOrUpdateBatch(List<" + tableInfo.getBeanName() + "> beanList);");
            bw.newLine();
            bw.newLine();

            // get data from db indexes
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> fieldInfoList = entry.getValue();

                // variables for writing expressions
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();

                for (FieldInfo fieldInfo : fieldInfoList) {
                    ++index;
                    methodName.append(StringUtils.firstLetterUpperCase(fieldInfo.getPropertyName()));
                    //{javaType} {propertyName}
                    methodParams
                            .append(fieldInfo.getJavaType())
                            .append(" ")
                            .append(fieldInfo.getPropertyName());

                    if (index < fieldInfoList.size()) {
                        methodName.append("And");
                        methodParams.append(", ");
                    }
                }

                BuildComment.createFieldComment(bw, "based on " + methodName + " to query data");
                bw.write("\t" + tableInfo.getBeanName() + " getBy" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "based on " + methodName + " to update data");
                bw.write("\tLong updateBy" + methodName + "(" + tableInfo.getBeanName() + " bean, " + methodParams + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "based on " + methodName + " to delete data");
                bw.write("\tLong deleteBy" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
            }

            bw.newLine();
            bw.write("}");
            bw.flush();

        } catch (Exception e) {
            log.info("build service error: {}", e);
        } finally {
            ResourceUtils.closeQuietly(out, outputStreamWriter, bw, log);
        }

    }
}
