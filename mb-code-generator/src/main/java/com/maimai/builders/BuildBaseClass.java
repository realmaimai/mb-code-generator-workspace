package com.maimai.builders;

import com.maimai.bean.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildBaseClass {
    public static void execute() {
        List<String> headerInfo  = new ArrayList<>();

        // create enums template
        headerInfo.clear();
        headerInfo.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headerInfo, "DateTimePatternEnum", Constants.PATH_ENUMS);

        // create date utils template
        headerInfo.clear();
        headerInfo.add("package " + Constants.PACKAGE_UTILS + ";");
        build(headerInfo, "DateUtils", Constants.PATH_UTILS);

        // create base mapper
        headerInfo.clear();
        headerInfo.add("package " + Constants.PACKAGE_MAPPERS + ";");
        build(headerInfo, "BaseMapper", Constants.PATH_MAPPERS);

//        headerInfo.clear();
//        headerInfo.add("package " + Constants.PACKAGE_EXCEPTION + ";");
//        headerInfo.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
////        生成businessException
//        build(headerInfo, "BusinessException", Constants.PATH_EXCEPTION);
//
        // create page size enums file
        headerInfo.clear();
        headerInfo.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headerInfo, "PageSize", Constants.PATH_ENUMS);

        // create Paginator file
        headerInfo.clear();
        headerInfo.add("package " + Constants.PACKAGE_QUERY + ";");
        headerInfo.add("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
        build(headerInfo, "Paginator", Constants.PATH_QUERY);

//        headerInfo.clear();
//        headerInfo.add("package " + Constants.PACKAGE_CONTROLLER + ";");
//        headerInfo.add("import " + Constants.PACKAGE_VO + ".ResponseVO;");
//        headerInfo.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
////        生成ABaseController
//        build(headerInfo, "ABaseController", Constants.PATH_CONTROLLER);
//
//        headerInfo.clear();
//        headerInfo.add("package " + Constants.PACKAGE_CONTROLLER + ";");
//        headerInfo.add("import " + Constants.PACKAGE_VO + ".ResponseVO;");
//        headerInfo.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
//        headerInfo.add("import " + Constants.PACKAGE_EXCEPTION + ".BusinessException;");
////        生成AGlobalExceptionHandlerController
//        build(headerInfo, "AGlobalExceptionHandlerController", Constants.PATH_CONTROLLER);
//
        // create BaseQuery file
        headerInfo.clear();
        headerInfo.add("package " + Constants.PACKAGE_QUERY + ";");
        build(headerInfo , "BaseQuery", Constants.PATH_QUERY);

        // create VO file
        headerInfo.clear();
        headerInfo.add("package " + Constants.PACKAGE_VO + ";");
        // paginationResultVO
        build(headerInfo , "PaginationResultVO", Constants.PATH_VO);
//        // responseVO
//        build(headerInfo , "ResponseVO", Constants.PATH_VO);
    }

    private static void build(List<String> headerInfo, String fileName, String outputPath) {
        File folder = new File(outputPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File javaFile = new File(outputPath, fileName + ".java");

        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {

            os = new FileOutputStream(javaFile);
            osw = new OutputStreamWriter(os, "utf-8");
            bw = new BufferedWriter(osw);

            String templatePath = BuildBaseClass.class.getClassLoader().getResource("templates/" + fileName + ".txt").getPath();

            is = new FileInputStream(templatePath);
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);

            for (String s : headerInfo) {
                bw.write(s);
                bw.newLine();
                bw.newLine();
            }


            String lineInfo = null;
            while ((lineInfo = br.readLine()) != null) {
                bw.write(lineInfo);
                bw.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

}