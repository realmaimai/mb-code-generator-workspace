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
import java.util.Objects;

@Slf4j
public class BuildMapper {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPERS);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS;
        File poFile = new File(folder, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            out = Files.newOutputStream(poFile.toPath());
            outputStreamWriter = new OutputStreamWriter(out);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("package " + Constants.PACKAGE_MAPPERS + ";");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.write("import org.apache.ibatis.annotations.Param;");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            BuildComment.createClassComment(bufferedWriter, tableInfo.getComment());
            bufferedWriter.write("public interface " + tableInfo.getBeanName() + Constants.SUFFIX_MAPPERS + "<T, P> extends BaseMapper {");
            bufferedWriter.newLine();


            // loop field info list, set field in class
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
                    // @Param("{propertyName}") {javaType} {propertyName}
                    methodParams.append("@Param(\"")
                            .append(fieldInfo.getPropertyName())
                            .append("\") ")
                            .append(fieldInfo.getJavaType())
                            .append(" ")
                            .append(fieldInfo.getPropertyName());

                    if (index < fieldInfoList.size()) {
                        methodName.append("And");
                        methodParams.append(", ");
                    }
                }

                BuildComment.createFieldComment(bufferedWriter, "based on " + methodName + " to query data");
                bufferedWriter.write("\tT selectBy" + methodName + "(" + methodParams + ");");
                bufferedWriter.newLine();
                bufferedWriter.newLine();

                BuildComment.createFieldComment(bufferedWriter, "based on " + methodName + " to update data");
                bufferedWriter.write("\tInteger updateBy" + methodName + "(@Param(\"bean\") T t, " + methodParams + ");");
                bufferedWriter.newLine();
                bufferedWriter.newLine();

                BuildComment.createFieldComment(bufferedWriter, "based on " + methodName + " to delete data");
                bufferedWriter.write("\tInteger deleteBy" + methodName + "(" + methodParams + ");");
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            bufferedWriter.write("}");
            bufferedWriter.flush();

        } catch (Exception e) {
            log.info("create mapper error: " + e);
        } finally {
            ResourceUtils.closeQuietly(out, outputStreamWriter, bufferedWriter, log);
        }

    }
}
