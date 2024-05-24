package com.wechat.utils;

import java.util.List;
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

    public static <T> T toObject(String json, Class<T> clazz) {
        T obj = null;
        try {
            obj = JSON.parseObject(json, clazz);
        } catch (Exception e) {
            logger.warning("JsonUtils.toObject error:" + e.getMessage());
        }
        return obj;
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        List<T> list = null;
        try {
            list = JSON.parseArray(json, clazz);
        } catch (Exception e) {
            logger.warning("JsonUtils.toList error:" + e.getMessage());
        }
        return list;
    }
}
