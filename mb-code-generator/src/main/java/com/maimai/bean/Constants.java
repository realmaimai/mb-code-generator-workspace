package com.maimai.bean;

import com.maimai.utils.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;

@Slf4j
public class Constants {
    public static String PRIVATE = "private";
    public static String PUBLIC = "public";
    public static String STATIC = "static";
    public static String PATH_JAVA = "java";
    public static String PATH_RESOURCE = "resources";
    public static String COMMENT_AUTHOR;
    // if we want to ignore the prefix
    // when the table is started with "tb_" like "tb_order_info"
    public static boolean IGNORE_TABLE_PREFIX;
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_BEAN_FUZZY;
    public static String SUFFIX_BEAN_TIME_START;
    public static String SUFFIX_BEAN_TIME_END;
    public static String SUFFIX_MAPPERS;
    public static String PATH_BASE;

    //  base package
    public static String PACKAGE_BASE;

    //  bean
    public static String PATH_PO;
    public static String PACKAGE_PO;

    //  vo
    public static String PATH_VO;
    public static String PACKAGE_VO;

    //  query
    public static String PATH_QUERY;
    public static String PACKAGE_QUERY;

    //  utils
    public static String PACKAGE_UTILS;
    public static String PATH_UTILS;

    //  enums
    public static String PATH_ENUMS;
    public static String PACKAGE_ENUMS;

    //  mapper package
    public static String PATH_MAPPERS;
    public static String PACKAGE_MAPPERS;

    //  service package
    public static String PATH_SERVICE;
    public static String PACKAGE_SERVICE;

    //  service implementation package
    public static String PATH_SERVICEIMPL;
    public static String PACKAGE_SERVICEIMPL;

    //  controller package
    public static String PATH_CONTROLLER;
    public static String PACKAGE_CONTROLLER;
    public static String PATH_EXCEPTION;
    public static String PACKAGE_EXCEPTION;
    public static String PATH_MAPPERS_XMLS;
    public static String IGNORE_BEAN_TOJSON_FIELD;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;
    public static String BEAN_DATE_FORMAT_SERIALIZATION;
    public static String BEAN_DATE_FORMAT_SERIALIZATION_CLASS;
    public static String BEAN_DATE_FORMAT_DESERIALIZATION;
    public static String BEAN_DATE_FORMAT_DESERIALIZATION_CLASS;

    //  mapping of sql type to java object
    public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public final static String[] SQL_DATE_TYPES = new String[]{"date"};
    public final static String[] SQL_DECIMAL_TYPE = new String[]{"decimal","double","float"};
    public final static String[] SQL_STRING_TYPE = new String[]{"char","varchar","text","mediumtext","longtext"};
    public final static String[] SQL_INTEGER_TYPE = new String[]{"int","tinyint"};
    public final static String[] SQL_LONG_TYPE = new String[]{"bigint"};

    static {
        try {
            COMMENT_AUTHOR =  PropertiesUtils.getString("comment", "author");
            IGNORE_TABLE_PREFIX = Boolean.parseBoolean(PropertiesUtils.getString("ignore", "table", "prefix"));
            SUFFIX_BEAN_QUERY= PropertiesUtils.getString("suffix", "bean", "query");
            SUFFIX_BEAN_FUZZY = PropertiesUtils.getString("suffix", "bean", "param", "fuzzy");
            SUFFIX_BEAN_TIME_START = PropertiesUtils.getString("suffix","bean","time","start");
            SUFFIX_BEAN_TIME_END = PropertiesUtils.getString("suffix", "bean","time","end");
            SUFFIX_MAPPERS = PropertiesUtils.getString("suffix", "mapper");


            PACKAGE_BASE = PropertiesUtils.getString("package", "base");
            PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package", "po");
            PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtils.getString("package", "vo");
            PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package","query");
            PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package","utils");
            PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package","enums");
            PACKAGE_MAPPERS = PACKAGE_BASE + "." + PropertiesUtils.getString("package","mappers");
            PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtils.getString("package","service");
            PACKAGE_SERVICEIMPL = PACKAGE_BASE + "." + PropertiesUtils.getString("package","serviceImpl");
            PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtils.getString("package","controller");
            PACKAGE_EXCEPTION = PACKAGE_BASE + "." + PropertiesUtils.getString("package","exception");


            PATH_BASE = PropertiesUtils.getString("path","base") + "/" + PATH_JAVA + "/" + PACKAGE_BASE.replace('.','/') + "/";
            PATH_PO = PATH_BASE + PropertiesUtils.getString("package", "po").replace('.','/') + "/";
            PATH_VO = PATH_BASE +  PropertiesUtils.getString("package", "vo").replace('.','/') + "/";
            PATH_QUERY = PATH_BASE +  PropertiesUtils.getString("package","query").replace('.','/') + "/";
            PATH_UTILS = PATH_BASE +  PropertiesUtils.getString("package","utils").replace('.','/') + "/";
            PATH_ENUMS = PATH_BASE +  PropertiesUtils.getString("package","enums").replace('.','/') + "/";
            PATH_MAPPERS = PATH_BASE + PropertiesUtils.getString("package","mappers").replace('.','/') + "/";
            PATH_MAPPERS_XMLS = PropertiesUtils.getString("path","base") + "/" +  PATH_RESOURCE + "/" + PACKAGE_MAPPERS.replace('.','/')+"/";
            PATH_SERVICE = PATH_BASE + PropertiesUtils.getString("package","service") + "/";
            PATH_SERVICEIMPL = PATH_BASE + StringUtils.replace(PropertiesUtils.getString("package","serviceImpl"), ".", "/") + "/" ;
//            PATH_CONTROLLER = PATH_BASE + PropertiesUtils.getString("package","controller") + "/" ;
//            PATH_EXCEPTION = PATH_BASE + PropertiesUtils.getString("package","exception") + "/" ;

            IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore", "bean", "tojson", "field");
            IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore", "bean", "tojson", "expression");
            IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore", "bean", "tojson", "class");

            BEAN_DATE_FORMAT_SERIALIZATION = PropertiesUtils.getString("bean", "date", "format", "serialization");
            BEAN_DATE_FORMAT_SERIALIZATION_CLASS = PropertiesUtils.getString("bean", "date", "format", "serialization-class");

            BEAN_DATE_FORMAT_DESERIALIZATION = PropertiesUtils.getString("bean", "date", "format", "deserialization");
            BEAN_DATE_FORMAT_DESERIALIZATION_CLASS = PropertiesUtils.getString("bean", "date", "format", "deserialization-class");
        } catch (Exception e) {
            log.info("constants cannot init: {}", e.getMessage());
        }


    }
    public static void main(String[] args) {
        System.out.println(PATH_BASE);
        System.out.println(PATH_MAPPERS_XMLS);
        System.out.println(PATH_SERVICE);
        System.out.println(PATH_SERVICEIMPL);
    }
}