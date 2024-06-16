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
public class BuildController {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_CONTROLLER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + "Controller";
        File poFile = new File(folder, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);

            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            String serviceName = tableInfo.getBeanName() + "Service";
            String serviceBeanName = StringUtils.firstLetterLowerCase(serviceName);
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + serviceName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".ResponseVO;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestMapping;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RestController;");
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.RequestBody;");
            bw.newLine();
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getBeanName() + "Controller");
            bw.write("@RestController");
            bw.newLine();
            bw.write("@RequestMapping(\"/" + StringUtils.firstLetterLowerCase(tableInfo.getBeanName()) + "\")");
            bw.newLine();
            bw.write("public class " + className + " extends BaseController {");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate " + serviceName + " " +  serviceBeanName +";");
            bw.newLine();

            BuildComment.createFieldComment(bw, "get data list by pages");
            bw.write("\t@RequestMapping(\"loadDataList\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO" +  " loadDataList(" + tableInfo.getBeanParamName()+ " query) {");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(" + serviceBeanName + ".getListByPage(query));");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw, "insert new data");
            bw.write("\t@RequestMapping(\"insert\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO insert(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\t" + serviceBeanName + ".insert(bean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw, "insert or update");
            bw.write("\t@RequestMapping(\"insertOrUpdate\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO insertOrUpdate(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\t" + serviceBeanName + ".insertOrUpdate(bean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw, "batch insert");
            bw.write("\t@RequestMapping(\"insertBatch\")");
            bw.newLine();
            bw.write("\tpublic ResponseVO insertBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\t" + serviceBeanName + ".insertBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createFieldComment(bw, "batch insert or update");
            bw.write("\t@RequestMapping(\"insertOrUpdateBatch\")");
            bw.newLine();
            bw.write("\t public ResponseVO insertOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\t" + serviceBeanName + ".insertOrUpdateBatch(listBean);");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVO(null);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                int index = 0;
                StringBuffer methodName = new StringBuffer();
                StringBuffer methodParams = new StringBuffer();
                StringBuffer methodParamsWithoutJavaType = new StringBuffer();
                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    if (index != 0 ) {
                        methodName.append("And");
                        methodParams.append(", ");
                        methodParamsWithoutJavaType.append(", ");
                    }
                    methodName.append(StringUtils.firstLetterUpperCase(fieldInfo.getPropertyName()));
                    methodParams.append(fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    methodParamsWithoutJavaType.append( fieldInfo.getPropertyName());
                    index++;
                }
                BuildComment.createFieldComment(bw, "query based on" + methodName);
                bw.write("\t@RequestMapping(\"" + "get" + tableInfo.getBeanName() + "By" + methodName +  "\")");
                bw.newLine();
                bw.write("\tpublic  ResponseVO get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(" + serviceBeanName + ".get"+tableInfo.getBeanName()+"By" + methodName + "(" + methodParamsWithoutJavaType + "));");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "update by " + methodName);
                bw.write("\t@RequestMapping(\"" + "update" + tableInfo.getBeanName() + "By" + methodName +  "\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean, " + methodParams + ") {");
                bw.newLine();
                bw.write("\t\t" + serviceBeanName + ".update"+tableInfo.getBeanName()+"By" + methodName + "(bean, " + methodParamsWithoutJavaType + ");");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(null);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "delete by" + methodName);
                bw.write("\t@RequestMapping(\"" + "delete" + tableInfo.getBeanName() + "By" + methodName +  "\")");
                bw.newLine();
                bw.write("\tpublic ResponseVO delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ") {");
                bw.newLine();
                bw.write("\t\t" + serviceBeanName + ".delete"+tableInfo.getBeanName()+"By" + methodName + "(" + methodParamsWithoutJavaType + ");");
                bw.newLine();
                bw.write("\t\treturn getSuccessResponseVO(null);");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            bw.flush();

        } catch (Exception e) {
            log.info("build controller error: {}", e.getMessage(), e);
        } finally {
            ResourceUtils.closeQuietly(out, outw, bw, log);
        }


    }
}
