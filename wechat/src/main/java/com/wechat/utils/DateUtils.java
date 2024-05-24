package com.wechat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String _yyyy_MM_dd = "yyyy/MM/dd";
    public static final String YYYYMM = "yyyyMM";
    private static final Object lock = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>>  sdfMap = new HashMap<>() ;

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        if (tl == null) {
            synchronized (lock) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    public static String format(Date date,String patten) {
        return getSdf(patten).format(date);
    }

    public static Date parse(String date,String pattern) {
        try{
            new SimpleDateFormat(pattern).parse(date);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
