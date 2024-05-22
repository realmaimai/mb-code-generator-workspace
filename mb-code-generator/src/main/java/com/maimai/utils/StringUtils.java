package com.maimai.utils;

public class StringUtils {
    public static String firstLetterUpperCase(String name) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(name)) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String firstLetterLowerCase(String name) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(name)) {
            return name;
        }
        return name.substring(0,1).toLowerCase() + name.substring(1);
    }
}
