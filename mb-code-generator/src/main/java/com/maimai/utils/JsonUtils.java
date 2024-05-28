package com.maimai.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Objects;

public class JsonUtils {
    public static String convertObj2Json(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }
}
