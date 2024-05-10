package com.easyjava.bean;

import com.easyjava.utils.PropertiesUtils;

public class Constants {
    public static Boolean IGNORE_TABLE_PREFIX;
    public static String SUFFIX_BEAN_PARAM;

    // path.base=src/main/java
    public static String PATH_BASE;

    // path.base.resources
    public static String PATH_BASE_RESOURCES;

    // package.base=com.easyjava
    public static String PACKAGE_BASE;
    public static String PATH__PAKAGE;

    // package.param=entity.param
    public static String PACKAGE_PARAM;
    public static String PATH_PARAM;

    // package.po=entity.po
    public static String PACKAGE_PO;
    public static String PATH_PO;

    // package.vo=entity.vo
    public static String PACKAGE_VO;
    public static String PATH_VO;
    
    // package.utils=utils
    public static String PACKAGE_UTILS;
    public static String PATH_UTILS;

    // package.mappers=mappers
    public static String PACKAGE_MAPPERS;
    public static String PATH_MAPPERS;

    // package.servece=service
    public static String PACKAGE_SERVICE;
    public static String PATH_SERVICE;
    // package.servece.impl=service.impl
    public static String PACKAGE_SERVICE_IMPL;
    public static String PATH_SERVICE_IMPL;
    // package.controller=controller
    public static String PACKAGE_CONTROLLER;
    public static String PATH_CONTROLLER;

    // package.exception=exception
    public static String PACKAGE_EXCEPTION;
    public static String PATH_EXCEPTION;
    
    public static String AUTHOR;

    // 参数模糊搜索后缀
    public static String SUFFIX_BEAN_PARAM_FUZZY;
    // 参数日期起始于结束后缀
    public static String SUFFIX_BEAN_PARAM_TIME_START;
    public static String SUFFIX_BEAN_PARAM_TIME_END;
    // Mappers后缀
    public static String SUFFIX_MAPPERS;

        
    //需要忽略的属性 ignore.bean.tojson.fields=createTime,updateTime
    public static String[] IGNORE_BEAN_TOJSON_FIELDS;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASSES;

    //日期格式序列化
    public static String DATE_SERIALIZATION_EXPRESSION;
    public static String DATE_SERIALIZATION_CLASSES;

    //日期格式反序列化
    public static String DATE_DESERIALIZATION_EXPRESSION;
    public static String DATE_DESERIALIZATION_CLASSES;
    
    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getProperty("db.table.prefix"));
        SUFFIX_BEAN_PARAM = PropertiesUtils.getProperty("suffix.bean.param");
        AUTHOR = PropertiesUtils.getProperty("author");
        PATH_BASE = PropertiesUtils.getProperty("path.base");
        PATH_BASE_RESOURCES = PropertiesUtils.getProperty("path.base.resources");
        PACKAGE_BASE = PropertiesUtils.getProperty("package.base");


        SUFFIX_BEAN_PARAM_FUZZY = PropertiesUtils.getProperty("suffix.bean.param.fuzzy");
        SUFFIX_BEAN_PARAM_TIME_START = PropertiesUtils.getProperty("suffix.bean.param.time.start");
        SUFFIX_BEAN_PARAM_TIME_END = PropertiesUtils.getProperty("suffix.bean.param.time.end");
        SUFFIX_MAPPERS = PropertiesUtils.getProperty("suffix.mappers");


        PATH__PAKAGE = PACKAGE_BASE.replace(".", "/");
        PACKAGE_PO = PropertiesUtils.getProperty("package.po");
        PATH_PO = PACKAGE_PO.replace(".", "/");

        PACKAGE_VO = PropertiesUtils.getProperty("package.vo");
        PATH_VO = PACKAGE_VO.replace(".", "/");

        PACKAGE_PARAM = PropertiesUtils.getProperty("package.param");
        PATH_PARAM = PACKAGE_PARAM.replace(".", "/");
        
        PACKAGE_UTILS= PropertiesUtils.getProperty("package.utils");
        PATH_UTILS = PACKAGE_UTILS.replace(".", "/");

        PACKAGE_MAPPERS = PropertiesUtils.getProperty("package.mappers");
        PATH_MAPPERS = PACKAGE_MAPPERS.replace(".", "/");

        PACKAGE_SERVICE = PropertiesUtils.getProperty("package.service");
        PATH_SERVICE = PACKAGE_SERVICE.replace(".", "/");

        PACKAGE_SERVICE_IMPL = PropertiesUtils.getProperty("package.service.impl");
        PATH_SERVICE_IMPL = PACKAGE_SERVICE_IMPL.replace(".", "/");

        PACKAGE_CONTROLLER = PropertiesUtils.getProperty("package.controller");
        PATH_CONTROLLER = PACKAGE_CONTROLLER.replace(".", "/");

        PACKAGE_EXCEPTION = PropertiesUtils.getProperty("package.exception");
        PATH_EXCEPTION = PACKAGE_EXCEPTION.replace(".", "/");


        IGNORE_BEAN_TOJSON_FIELDS = PropertiesUtils.getProperty("ignore.bean.tojson.fields").split(",");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getProperty("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASSES = PropertiesUtils.getProperty("ignore.bean.tojson.classes");

        DATE_SERIALIZATION_EXPRESSION = PropertiesUtils.getProperty("bean.date.serialization.expression");
        DATE_SERIALIZATION_CLASSES = PropertiesUtils.getProperty("bean.date.serialization.classes");

        DATE_DESERIALIZATION_EXPRESSION = PropertiesUtils.getProperty("bean.date.deserialization.expression");
        DATE_DESERIALIZATION_CLASSES = PropertiesUtils.getProperty("bean.date.deserialization.classes");
    }

    public static String get_package_base() {
        return PACKAGE_BASE;
    }

    public static String get_package_po() {
        return PACKAGE_BASE +'.'+PACKAGE_PO;
    }

    public static String get_package_vo() {
        return PACKAGE_BASE +'.'+PACKAGE_VO;
    }

    public static String get_package_utils() {
        return PACKAGE_BASE +'.'+PACKAGE_UTILS;
    }

    public static String get_package_param() {
        return PACKAGE_BASE +'.'+PACKAGE_PARAM;
    }

    public static String get_package_mappers() {
        return PACKAGE_BASE +'.'+PACKAGE_MAPPERS;
    }

    public static String get_package_service() {
        return PACKAGE_BASE +'.'+PACKAGE_SERVICE;
    }

    public static String get_package_service_impl() {
        return PACKAGE_BASE +'.'+PACKAGE_SERVICE_IMPL;
    }

    public static String get_package_controller() {
        return PACKAGE_BASE +'.'+PACKAGE_CONTROLLER;
    }

    public static String get_package_exception() {
        return PACKAGE_BASE +'.'+PACKAGE_EXCEPTION;
    }

    public static String get_package_path() {
        return PATH_BASE + '/' + PATH__PAKAGE;
    }

    public static String get_po_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_PO;
    }

    public static String get_vo_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_VO;
    }

    public static String get_param_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_PARAM;
    }

    public static String get_utils_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_UTILS;
    }

    public static String get_mappers_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_MAPPERS;
    }

    public static String get_service_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_SERVICE;
    }

    public static String get_service_impl_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_SERVICE_IMPL;
    }

    public static String get_controller_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_CONTROLLER;
    }
    
    public static String get_exception_path() {
        return PATH_BASE + '/' + PATH__PAKAGE + "/" + PATH_EXCEPTION;
    }

    public static String get_xml_path() {
        return PATH_BASE_RESOURCES + '/' + PATH__PAKAGE +'/' + PACKAGE_MAPPERS;
    }

    // 数据库类型
    public static String[] SQL_DATA_TIME_TYPES = {"datetime", "timestamp", "time"};
    public static String[] SQL_DATA_TYPES = {"date"};
    public static String[] SQL_DECIMAL_TYPES = {"decimal","double","float"};
    public static String[] SQL_STRING_TYPES = {"char", "varchar", "text", "longtext", "mediumtext", "tinytext"};
    public static String[] SQL_INTERGER_TYPES = {"int", "tinyint"};
    public static String[] SQL_LONG_TYPES = {"bigint"};

}
