package com.maimai.utils;


import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class PropertiesUtils {
    private static Map<String, Object> yamlProperties;
    private static Map<String, String> PROP_MAP = new ConcurrentHashMap();


    static {
        InputStream is = null;
        Yaml yaml = new Yaml();
        try {
            // load properties from resources
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.yml");
            yamlProperties = yaml.load(is);
        } catch (Exception e) {
            log.info("yaml loading error: " + e.getMessage());
        } finally {
            if (!Objects.isNull(is)) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.info("error: " + e);
                }
            }
        }
    }

    public static String getString(String... keys) {
        Map<String, Object> currentMap = yamlProperties;
        for (String key : keys) {
            Object value = currentMap.get(key);
            if (value instanceof Map) {
                currentMap = (Map<String, Object>) value;
            }
        }
        // return the last key from the parameter
        // currentMap is the deepest map
        return (String) currentMap.get(keys[keys.length-1]);
    }

    public static void main(String[] args) {
        String string = getString("bean", "date", "format", "serialization");
        System.out.println(string);
    }
}
