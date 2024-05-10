package com.easyjava.utils;

import java.util.logging.Logger;

public class JsonUtils {
    private static final Logger logger = Logger.getLogger(JsonUtils.class.getName());

    public static String toJson(Object obj) {
        String json = null;
        try {
            json = com.alibaba.fastjson.JSON.toJSONString(obj);
        } catch (Exception e) {
            logger.warning("JsonUtils.toJson error:" + e.getMessage());
        }
        return json;
    }
}
