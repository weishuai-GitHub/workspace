package com.easyjava.utils;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtils {
    private static Properties props = new Properties();
    private static Map<String, String> PROPER_MAP = new ConcurrentHashMap<>();

    static {
        try {
            props.load(PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties"));
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                PROPER_MAP.put((String) entry.getKey(), (String) entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return PROPER_MAP.get(key);
    }

    public static void print()
    {
        for (Map.Entry<String, String> entry : PROPER_MAP.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
