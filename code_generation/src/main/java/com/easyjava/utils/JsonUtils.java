package com.easyjava.utils;

import java.util.logging.Logger;
import com.alibaba.fastjson.JSON;

public class JsonUtils {
    private static final Logger logger = Logger.getLogger(JsonUtils.class.getName());

    public static String toJson(Object obj) {
        String json = null;
        try {
            json = JSON.toJSONString(obj);
        } catch (Exception e) {
            logger.warning("JsonUtils.toJson error:" + e.getMessage());
        }
        return json;
    }
}
