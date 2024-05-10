package com.easyjava.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;
import com.easyjava.utils.PropertiesUtils;

public class BuildTable {
    private static Connection conn = null;
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    
    private static String SQL_SHOW_TABLES_STATUS = "SHOW TABLE STATUS";
    private static String SQL_SHOW_TABLE_FIELDS = "SHOW FULL FIELDS FROM %s";
    private static String SQL_SHOW_TABLE_INDEX = "SHOW INDEX FROM  %s";
    static {
        String driverName = PropertiesUtils.getProperty("db.driver.name");
        String url = PropertiesUtils.getProperty("db.url");
        String user = PropertiesUtils.getProperty("db.username");
        String password = PropertiesUtils.getProperty("db.password");
        // logger.info("driverName : " + driverName);
        // logger.info("url : " + url);
        // logger.info("user : " + user);
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("dataBase connect failed", e);
        }
    }
    public static List<TableInfo> getTables() {
        PreparedStatement  ps = null;
        ResultSet rs = null;
        List<TableInfo> tableInfos = new ArrayList<>();
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLES_STATUS);
            rs = ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("name");
                String comment = rs.getString("comment");
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = tableName.substring(tableName.indexOf("_") + 1);
                }
                beanName = processFiled(beanName,true);
                tableInfo.setBeanName(beanName);
                tableInfo.setTableComment(comment);
                tableInfo.setBeanParaName(beanName+Constants.SUFFIX_BEAN_PARAM);
                List<FieldInfo> fieldInfos = processFieldInfo(tableInfo);
                tableInfo.setColumnInfo(fieldInfos);
                getKeyIndexInfos(tableInfo);
                tableInfos.add(tableInfo);
            }
        } catch (Exception e) {
            logger.error("读取表失败", e);
        }finally{
            if(rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if(ps != null)
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        }
    
        return tableInfos;
    }

    private static String processFiled(String field,Boolean uperCaseFistLetter) {
        StringBuffer sb = new StringBuffer();
        String[] fields = field.split("_");
        sb.append(uperCaseFistLetter? 
        fields[0].substring(0, 1).toUpperCase() + fields[0].substring(1):fields[0]);
        for (int i = 1; i < fields.length; i++) {
            sb.append(fields[i].substring(0, 1).toUpperCase() + fields[i].substring(1));
        }
        return sb.toString();
    }


    // 处理字段信息
    private static List<FieldInfo> processFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        List<FieldInfo> fieldInfos = new ArrayList<>();
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");
                
                String propertyName = processFiled(field, false);
                if (type.indexOf('(')>0)
                    type = type.substring(0,type.indexOf('('));
                // System.out.println(field + " : " + type + " : " + extra + " : " + comment);
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(field);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setJavaType(processJavaType(type));
                if (ArrayUtils.contains(Constants.SQL_DATA_TYPES, type)) {
                    tableInfo.setHasDate(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, type)) {
                    tableInfo.setHasDateTime(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)) {
                    tableInfo.setHasBigDecimal(true);
                }
                // logger.info("propertyName : {} javaType : {}",fieldInfo.getPropertyName(),fieldInfo.getJavaType());

                fieldInfos.add(fieldInfo);
            }
        } catch (Exception e) {
            logger.error("读取表失败", e);
        }
        return fieldInfos;
    }


    // 处理java类型
    private static String processJavaType(String sqlType) {
        if (ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, sqlType)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DATA_TYPES, sqlType)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, sqlType)) {
            return "BigDecimal";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, sqlType)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_INTERGER_TYPES, sqlType)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPES, sqlType)) {
            return "Long";
        }else
            throw new RuntimeException("Unknow type : " + sqlType);
    }

    // 处理字段信息
    private static void getKeyIndexInfos(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        Map<String,FieldInfo> fieldInfoMap = new HashMap<>();
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            for (FieldInfo fieldInfo : tableInfo.getColumnInfo()) {
                fieldInfoMap.put(fieldInfo.getFieldName(), fieldInfo);
            }
            while (fieldResult.next()) {
                Integer non_unique = fieldResult.getInt("non_unique");
                String column_name = fieldResult.getString("column_name");
                String key_name = fieldResult.getString("key_name");


                if (non_unique == 1) {
                    continue;
                }
                if (tableInfo.getKeyIndexMap().get(key_name) == null) {
                    tableInfo.getKeyIndexMap().put(key_name, new ArrayList<>());
                }
                tableInfo.getKeyIndexMap().get(key_name).add(fieldInfoMap.get(column_name));
            }
        } catch (Exception e) {
            logger.error("读取索引失败", e);
        }
    }
}
