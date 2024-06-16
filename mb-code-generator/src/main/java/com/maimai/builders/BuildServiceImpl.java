package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.FieldInfo;
import com.maimai.bean.TableInfo;
import com.maimai.utils.ResourceUtils;
import com.maimai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Slf4j
public class BuildServiceImpl {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICEIMPL);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName() + "ServiceImpl.java");
        String className = tableInfo.getBeanName() + "ServiceImpl";
        String interfaceName = tableInfo.getBeanName() + "Service";
        String mapperClass = tableInfo.getBeanName() + "Mapper";
        String mapperBeanName = StringUtils.firstLetterLowerCase(mapperClass);
        String queryClass = tableInfo.getBeanName() + "Query";

        OutputStream out = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bw = null;
        try {
            out = Files.newOutputStream(poFile.toPath());
            outputStreamWriter = new OutputStreamWriter(out);
            bw = new BufferedWriter(outputStreamWriter);

            bw.write("package " + Constants.PACKAGE_SERVICEIMPL + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_MAPPERS + "." + mapperClass + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + ".Paginator;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO" + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + tableInfo.getBeanName() + "Service" + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment() + " Service");
            bw.write("@Service(\"" + StringUtils.firstLetterLowerCase(interfaceName) + "\")");
            bw.newLine();
            bw.write("public class " + tableInfo.getBeanName() + "ServiceImpl implements " + tableInfo.getBeanName() + "Service {");
            bw.newLine();
            bw.newLine();
            bw.write("\t@Resource");
            bw.newLine();
            bw.write("\tprivate " + mapperClass + "<" + tableInfo.getBeanName() + "," + queryClass + "> " + StringUtils.firstLetterLowerCase(mapperClass) + ";");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic List<" + tableInfo.getBeanName() + "> getListByParam(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".selectList(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer getCountByParam(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".selectCount(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic PaginationResultVO<" + tableInfo.getBeanName() + "> getListByPage(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\tInteger count = this.getCountByParam(query);");
            bw.newLine();
            bw.write("\t\tInteger pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();");
            bw.newLine();
            bw.write("\t\tPaginator page = new Paginator(query.getPageNo(), count, pageSize);");
            bw.newLine();
            bw.write("\t\tquery.setPaginator(page);");
            bw.newLine();
            bw.write("\t\tList<" + tableInfo.getBeanName() + "> list = this.getListByParam(query);");
            bw.newLine();
            bw.write("\t\tPaginationResultVO<" + tableInfo.getBeanName() + "> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);");
            bw.newLine();
            bw.write("\t\treturn result;");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer insert(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insert(bean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer insertOrUpdate(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insertOrUpdate(bean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer insertBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif(listBean == null || listBean.isEmpty()){");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insertBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer insertOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif(listBean == null || listBean.isEmpty()){");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insertOrUpdateBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
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
                StringBuilder paramsBuilder = new StringBuilder();

                for (FieldInfo fieldInfo : fieldInfoList) {
                    ++index;
                    methodName.append(StringUtils.firstLetterUpperCase(fieldInfo.getPropertyName()));
                    //{javaType} {propertyName}
                    methodParams
                            .append(fieldInfo.getJavaType())
                            .append(" ")
                            .append(fieldInfo.getPropertyName());
                    paramsBuilder.append(fieldInfo.getPropertyName());

                    if (index < fieldInfoList.size()) {
                        methodName.append("And");
                        methodParams.append(", ");
                        paramsBuilder.append(", ");
                    }
                }

                BuildComment.createFieldComment(bw, "based on " + methodName + " to query data");
                bw.write("\tpublic " + tableInfo.getBeanName() + " get"+tableInfo.getBeanName()+"By" + methodName + "(" + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn " + mapperBeanName + ".selectBy" + methodName + "(" + paramsBuilder + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();


                BuildComment.createFieldComment(bw, "based on " + methodName + " to update data");
                bw.write("\tpublic Integer update"+tableInfo.getBeanName()+"By" + methodName + "(" + tableInfo.getBeanName() + " bean, " + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn " + mapperBeanName + ".updateBy" + methodName + "(bean, " + paramsBuilder + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "based on " + methodName + " to delete data");
                bw.write("\tpublic Integer delete"+tableInfo.getBeanName()+"By" + methodName + "(" + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn " + mapperBeanName + ".deleteBy" + methodName + "(" + paramsBuilder + ");");
                bw.newLine();
                bw.write("\t}");
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
