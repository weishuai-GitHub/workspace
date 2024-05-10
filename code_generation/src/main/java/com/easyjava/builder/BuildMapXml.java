package com.easyjava.builder;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easyjava.bean.Constants;
import com.easyjava.bean.FieldInfo;
import com.easyjava.bean.TableInfo;


public class BuildMapXml {
    private static final Logger logger = LoggerFactory.getLogger(BuildBase.class);
    private static final String BASE_COLUMN_LIST = "base_column_list";
    private static final String BASE_QUERY_CONDITION = "base_query_condition";
    private static final String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";
    private static final String QUERY_CONDITION = "query_condition";
    public static String poClass;
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.get_xml_path());
        poClass = Constants.get_package_po() + "." + tableInfo.getBeanName();
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(Constants.get_xml_path() + "/" + tableInfo.getBeanName() + "Mapper.xml");
        try {
            if(!file.exists())
                file.createNewFile();
        } catch (Exception e) {
            logger.error("create xml failed", e);
        }
        OutputStream out = null;
        OutputStreamWriter ouw = null;
        BufferedWriter bw = null;
        try {
            out = new  FileOutputStream(file);
            ouw = new OutputStreamWriter(out, "UTF-8");
            bw = new BufferedWriter(ouw);
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
            bw.write("<mapper namespace=\"" + Constants.get_package_mappers() + "." + tableInfo.getBeanName() + "Mapper\">\n");
            
            //实体映射
            buildResultMap(bw, tableInfo);
            bw.write("\n");
            
            //通用查询列表
            buildGensequence(bw, tableInfo);
            bw.write("\n");

            //基础查询条件
            buildBaseQuery(bw, tableInfo);
            bw.write("\n");

            //扩展查询条件
            buildExtendQuery(bw, tableInfo);
            bw.write("\n");

            //通用查询条件
            buildQueryCondition(bw, tableInfo);
            bw.write("\n");

            //查询列表
            buildQueryList(bw, tableInfo);
            bw.write("\n");

            //查询总数
            buildQueryCount(bw, tableInfo);
            bw.write("\n");

            // 单挑插入
            buildInsert(bw, tableInfo);
            bw.write("\n");

            // 插入或更新
            buildInsertOrupdate(bw, tableInfo);
            bw.write("\n");

            // 批量插入
            buildBatchInsert(bw, tableInfo);
            bw.write("\n");

            //批量插入或更新
            buildBatchInsertOrUpdate(bw, tableInfo);
            bw.write("\n");

            //根据主键查询
            buildSelectBykey(bw, tableInfo);

            //根据主键删除
            buildDeleteBykey(bw, tableInfo);

            //根据主键更新
            buildUpdateBykey(bw, tableInfo);

            bw.write("\n");
            bw.write("</mapper>");
            bw.flush();
        } catch (Exception e) {
            logger.error("write file failed", e);
        }finally{
            try {
                if (bw != null) {
                    bw.close();
                }
                if (ouw != null) {
                    ouw.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                logger.error("close stream failed", e2);
            }
        }
    }

    public static void buildResultMap(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--实体映射-->\n");
        FieldInfo idFieldInfo = null;
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet())
        {
            
            if("PRIMARY".equals(entry.getKey()) && idFieldInfo == null) {
                List<FieldInfo> keyList = entry.getValue();
                if(keyList.size() == 1) {
                    idFieldInfo = keyList.get(0);
                    break;
                }
            }
        }
        bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">\n");
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            bw.write("\t\t<!--" + filedInfo.getComment() + "-->\n");
            if(filedInfo == idFieldInfo)
                bw.write("\t\t<id column=\"" + filedInfo.getFieldName() + "\" property=\"" + filedInfo.getPropertyName() + "\"/>\n");
            else
                bw.write("\t\t<result column=\"" + filedInfo.getFieldName() + "\" property=\"" + filedInfo.getPropertyName() + "\"/>\n");
        }
        bw.write("\t</resultMap>\n");
    }

    public static void buildGensequence(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--查询列表-->\n");
        bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">\n");
        StringBuffer sb = new StringBuffer();
        sb.append("\t\t");
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            sb.append(filedInfo.getFieldName() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\n");
        bw.write(sb.toString());
        bw.write("\t</sql>\n");
    }

    public static void buildBaseQuery(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--查询条件-->\n");
        bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">\n");
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType()))
            {
                bw.write("\t\t<if test=\"query." + filedInfo.getPropertyName() + " != null and query." + filedInfo.getPropertyName() + " != ''\">\n");
            
                bw.write("\t\t\tand " + filedInfo.getFieldName()+ "= #{query." + filedInfo.getPropertyName() + "}\n");
                
                bw.write("\t\t</if>\n");
            }
            else if(ArrayUtils.contains(Constants.SQL_DATA_TYPES, filedInfo.getSqlType()))
            {
                bw.write("\t\t<if test=\"query." + filedInfo.getPropertyName() + " != null and query." + filedInfo.getPropertyName() + " != ''\">\n");
                bw.write("\t\t\tand "+filedInfo.getFieldName()+ "= str_to_date(#{query." + filedInfo.getPropertyName() + "},'%Y-%m-%d')\n");
                bw.write("\t\t</if>\n");
            }
            else if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, filedInfo.getSqlType()))
            {
                bw.write("\t\t<if test=\"query." + filedInfo.getPropertyName() + " != null and query." + filedInfo.getPropertyName() + " != ''\">\n");
                bw.write("\t\t\tand "+filedInfo.getFieldName()+ "= str_to_date(#{query." + filedInfo.getPropertyName() + "},'%Y-%m-%d %H:%i:%s')\n");
                bw.write("\t\t</if>\n");
            }
            else
            {
                bw.write("\t\t<if test=\"query." + filedInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\tand "+filedInfo.getFieldName() + "= #{query." + filedInfo.getPropertyName() + "}\n");
                bw.write("\t\t</if>\n");
            }
            
        }
        bw.write("\t</sql>\n");
    }

    public static void buildExtendQuery(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--扩展查询条件-->\n");
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION_EXTEND + "\">\n");
            String andWhere = "";
            for(FieldInfo filedInfo : tableInfo.getExtendInfo()) {
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType()))
                {
                    andWhere = "and " + filedInfo.getFieldName() + " like concat('%', #{query." + filedInfo.getPropertyName() + "},'%')\n";
                }
                else if ( ArrayUtils.contains(Constants.SQL_DATA_TYPES, filedInfo.getSqlType()))
                {
                    if(filedInfo.getPropertyName().contains(Constants.SUFFIX_BEAN_PARAM_TIME_START))
                        andWhere = "<![CDATA[ and " + filedInfo.getFieldName() + " >= str_to_date(#{query." + filedInfo.getPropertyName() + "},'%Y-%m-%d')  ]]>\n";
                    else if(filedInfo.getPropertyName().contains(Constants.SUFFIX_BEAN_PARAM_TIME_END))
                        andWhere = "<![CDATA[ and " + filedInfo.getFieldName() + " < str_to_date(#{query." + filedInfo.getPropertyName() + "},'%Y-%m-%d') ]]>\n";
                }
                else if(ArrayUtils.contains(Constants.SQL_DATA_TIME_TYPES, filedInfo.getSqlType())){
                    if(filedInfo.getPropertyName().contains(Constants.SUFFIX_BEAN_PARAM_TIME_START))
                        andWhere = "<![CDATA[ and " + filedInfo.getFieldName() + " >= str_to_date(#{query." + filedInfo.getPropertyName() + "},'%Y-%m-%d %H:%i:%s')  ]]>\n";
                    else if(filedInfo.getPropertyName().contains(Constants.SUFFIX_BEAN_PARAM_TIME_END))
                        andWhere = "<![CDATA[ and " + filedInfo.getFieldName() + " < str_to_date(#{query." + filedInfo.getPropertyName() + "},'%Y-%m-%d %H:%i:%s') ]]>\n";
                }
                bw.write("\t\t<if test=\"query." + filedInfo.getPropertyName() + " != null and query." + filedInfo.getPropertyName() + " != ''\">\n");
                bw.write("\t\t\t" + andWhere);
                bw.write("\t\t</if>\n");
                
            }
            bw.write("\t</sql>\n");
    }

    public static void buildQueryCondition(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--通用查询条件-->\n");
        bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">\n");
        bw.write("\t\t<where>\n");
        bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>\n");
        bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION_EXTEND + "\"/>\n");
        bw.write("\t\t</where>\n");
        bw.write("\t</sql>\n");
    }

    public static void buildQueryList(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--查询列表-->\n");
        bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\" >\n");
        bw.write("\t\tSELECT\n");
        bw.write("\t\t<include refid=\"" + BASE_COLUMN_LIST + "\"/>\n");
        bw.write("\t\tFROM " + tableInfo.getTableName() + "\n");
        bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>\n");

        bw.write("\t\t<if test=\"query.orderBy != null\">\n");
        bw.write("\t\t\torder by ${query.orderBy}\n");
        bw.write("\t\t</if>\n");

        bw.write("\t\t<if test=\"query.simplePage != null\">\n");
        bw.write("\t\t\tlimit #{query.simplePage.start}, #{query.simplePage.pageSize}\n");
        bw.write("\t\t</if>\n");

        bw.write("\t</select>\n");
    }

    public static void buildQueryCount(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--查询总数-->\n");
        bw.write("\t<select id=\"selectCount\" resultType=\"Integer\">\n");
        bw.write("\t\tSELECT count(1)\n");
        bw.write("\t\tFROM " + tableInfo.getTableName() + "\n");
        bw.write("\t\t<include refid=\"" + QUERY_CONDITION + "\"/>\n");
        bw.write("\t</select>\n");
    }

    public static void buildInsert(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--插入匹配有值字段-->\n");
        bw.write("\t<insert id=\"insert\" parameterType=\"" + poClass + "\">\n");
        FieldInfo autoInceatementInfo = null;
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(filedInfo.isAutoIncrement()) {
                autoInceatementInfo = filedInfo;
                break;
            }
        }
        if(autoInceatementInfo != null) {
            bw.write("\t\t<selectKey keyProperty=\"bean." + autoInceatementInfo.getPropertyName() + "\" order=\"AFTER\" resultType=\""+autoInceatementInfo.getJavaType() +"\">\n");
            bw.write("\t\t\tSELECT LAST_INSERT_ID()\n");
            bw.write("\t\t</selectKey>\n");
        }
        bw.write("\t\tinsert into " + tableInfo.getTableName() + "\n");
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType())){
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null and bean." + filedInfo.getPropertyName() + " != ''\">\n");
                bw.write("\t\t\t\t" + filedInfo.getFieldName() + ",\n");
                bw.write("\t\t\t</if>\n");
            }
            else
            {
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t" + filedInfo.getFieldName() + ",\n");
                bw.write("\t\t\t</if>\n");
            }  
        }
        bw.write("\t\t</trim>\n");
        bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType())){
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null and bean." + filedInfo.getPropertyName() + " != ''\">\n");
                bw.write("\t\t\t\t#{bean." + filedInfo.getPropertyName() + "},\n");
                bw.write("\t\t\t</if>\n");
            }
            else
            {
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t#{bean." + filedInfo.getPropertyName() + "},\n");
                bw.write("\t\t\t</if>\n");
            }
        }
        bw.write("\t\t</trim>\n");
        bw.write("\t</insert>\n");
    }

    public static void buildInsertOrupdate(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--插入或更新-->\n");
        bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + poClass + "\">\n");
        bw.write("\t\tinsert into " + tableInfo.getTableName() + "\n");
        bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType())) {
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null and bean." + filedInfo.getPropertyName() + " != ''\">\n");
                bw.write("\t\t\t\t" + filedInfo.getFieldName() + ",\n");
                bw.write("\t\t\t</if>\n");
            }
            else
            {
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t" + filedInfo.getFieldName() + ",\n");
                bw.write("\t\t\t</if>\n");
            }
        }
        bw.write("\t\t</trim>\n");
        bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">\n");
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType())){
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null and bean." + filedInfo.getPropertyName() + " != ''\">\n");
                bw.write("\t\t\t\t#{bean." + filedInfo.getPropertyName() + "},\n");
                bw.write("\t\t\t</if>\n");
            }
            else
            {
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t#{bean." + filedInfo.getPropertyName() + "},\n");
                bw.write("\t\t\t</if>\n");
            }
        }
        bw.write("\t\t</trim>\n");
        bw.write("\t\tON DUPLICATE KEY UPDATE\n");
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        Map<String, String> keyMap = new HashMap<>();
        for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet())
        {
            List<FieldInfo> keyList = entry.getValue();
            for(FieldInfo key : keyList) {
                keyMap.put(key.getFieldName(), key.getPropertyName());
            }
        }
        bw.write("\t\t<trim prefix=\"\"  suffix=\"\" suffixOverrides=\",\">\n");
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(keyMap.containsKey(filedInfo.getFieldName())) {
                continue;
            }
            if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType())){
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null and bean." + filedInfo.getPropertyName() + " != ''\">\n");
                bw.write("\t\t\t\t" + filedInfo.getFieldName() + "=VALUES("+ filedInfo.getFieldName() +"),\n");
                bw.write("\t\t\t</if>\n");
            }
            else
            {
                bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null\">\n");
                bw.write("\t\t\t\t" + filedInfo.getFieldName() + "=VALUES("+filedInfo.getFieldName() +"),\n");
                bw.write("\t\t\t</if>\n");
            }
        }
        bw.write("\t\t</trim>\n");
        bw.write("\t</insert>\n");
    }

    public static void buildBatchInsert(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--批量插入-->\n");
        bw.write("\t<insert id=\"insertBatch\" parameterType=\""+poClass+"\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n");
        StringBuffer columnList = new StringBuffer();
        StringBuffer valueList = new StringBuffer();
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(filedInfo.isAutoIncrement())
                continue;
            columnList.append(filedInfo.getFieldName() + ",");
        }
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(filedInfo.isAutoIncrement())
                continue;
            valueList.append("#{item." + filedInfo.getPropertyName() + "},");
        }
        bw.write("\t\tinsert into " + tableInfo.getTableName() + "("+columnList.substring(0,columnList.length()-1) +") VALUES \n");
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\" open=\"(\" close=\")\" >\n");
        bw.write("\t\t\t");
        bw.write(valueList.substring(0,valueList.length()-1));
        bw.write(" \n");
        bw.write("\t\t</foreach>\n");
        bw.write("\t</insert>\n");
    }

    public static void buildBatchInsertOrUpdate(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        bw.write("\t<!--批量插入或更新-->\n");
        bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\""+poClass+"\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n");
        StringBuffer columnList = new StringBuffer();
        StringBuffer valueList = new StringBuffer();
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(filedInfo.isAutoIncrement())
                continue;
            columnList.append(filedInfo.getFieldName() + ",");
        }
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(filedInfo.isAutoIncrement())
                continue;
            valueList.append("#{item." + filedInfo.getPropertyName() + "},");
        }
        bw.write("\t\tinsert into " + tableInfo.getTableName() + " ( "+columnList.substring(0,columnList.length()-1) +" )\n");
        bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\" open=\"(\" close=\")\">\n");
        bw.write("\t\t\t");
        bw.write(valueList.substring(0,valueList.length()-1));
        bw.write(" \n");
        bw.write("\t\t</foreach>\n");
        bw.write("\t\tON DUPLICATE KEY UPDATE ");
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        Map<String, String> keyMap = new HashMap<>();
        for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet())
        {
            List<FieldInfo> keyList = entry.getValue();
            for(FieldInfo key : keyList) {
                keyMap.put(key.getFieldName(), key.getPropertyName());
            }
        }
        StringBuffer columnListUpdate = new StringBuffer();
        
        for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
            if(keyMap.containsKey(filedInfo.getFieldName())) {
                continue;
            }
            columnListUpdate.append(filedInfo.getFieldName() +" =VALUES( "+filedInfo.getFieldName() + " ),");
        }
        bw.write(columnListUpdate.substring(0,columnListUpdate.length()-1));
        bw.write(" \n");
        bw.write("\t</insert>\n");
    }

    public static void buildSelectBykey(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet())
        {
            List<FieldInfo> keyList = entry.getValue();
            StringBuffer methodName = new StringBuffer();
            StringBuffer where = new StringBuffer();
            String tmp;
            for(FieldInfo key : keyList) {
                tmp = key.getPropertyName().substring(0, 1).toUpperCase() + key.getPropertyName().substring(1);
                methodName.append(tmp).append("And");
                where.append(key.getFieldName() + "=#{").append(key.getPropertyName()).append("} and ");
            }
            methodName.delete(methodName.length() - 3, methodName.length());
            bw.write("\t<!--根据"+methodName+"查询-->\n");
            bw.write("\t<select id=\"selectBy"+methodName+"\" resultMap=\"base_result_map\">\n");
            bw.write("\t\tselect <include refid=\""+BASE_COLUMN_LIST+"\"/>\n");
            bw.write("\t\tfrom "+tableInfo.getTableName()+"\n");
            where.delete(where.length() - 4, where.length());
            bw.write("\t\twhere "+where+"\n");
            bw.write("\t</select>\n");
            bw.write("\n");
        }  
    }

    public static void buildDeleteBykey(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet())
        {
            List<FieldInfo> keyList = entry.getValue();
            StringBuffer methodName = new StringBuffer();
            StringBuffer where = new StringBuffer();
            String tmp;
            for(FieldInfo key : keyList) {
                tmp = key.getPropertyName().substring(0, 1).toUpperCase() + key.getPropertyName().substring(1);
                methodName.append(tmp).append("And");
                where.append(key.getFieldName() + "=#{").append(key.getPropertyName()).append("} and ");
            }
            methodName.delete(methodName.length() - 3, methodName.length());
            bw.write("\t<!--根据"+methodName+"删除-->\n");
            bw.write("\t<delete id=\"deleteBy"+methodName+"\">\n");
            bw.write("\t\tdelete from "+tableInfo.getTableName()+"\n");
            where.delete(where.length() - 4, where.length());
            bw.write("\t\twhere "+where+"\n");
            bw.write("\t</delete>\n");
            bw.write("\n");
        }
    }

    public static void buildUpdateBykey(BufferedWriter bw, TableInfo tableInfo) throws Exception{
        Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
        // Map<String, String> keyMap = new HashMap<>();
        // for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet())
        // {
        //     List<FieldInfo> keyList = entry.getValue();
        //     for(FieldInfo key : keyList) {
        //         keyMap.put(key.getFieldName(), key.getPropertyName());
        //     }
        // }
        for(Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet())
        {
            List<FieldInfo> keyList = entry.getValue();
            StringBuffer methodName = new StringBuffer();
            StringBuffer where = new StringBuffer();
            String tmp;
            for(FieldInfo key : keyList) {
                tmp = key.getPropertyName().substring(0, 1).toUpperCase() + key.getPropertyName().substring(1);
                methodName.append(tmp).append("And");
                where.append(key.getFieldName() + "=#{").append(key.getPropertyName()).append("} and ");
            }
            methodName.delete(methodName.length() - 3, methodName.length());
            bw.write("\t<!--根据"+methodName+"更新-->\n");
            bw.write("\t<update id=\"updateBy"+methodName+"\">\n");
            bw.write("\t\tupdate "+tableInfo.getTableName()+"\n");
            bw.write("\t\t<set>\n");
            for(FieldInfo filedInfo : tableInfo.getColumnInfo()) {
                if(keyList.contains(filedInfo))
                    continue;
                if(ArrayUtils.contains(Constants.SQL_STRING_TYPES, filedInfo.getSqlType())){
                    bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null and bean." + filedInfo.getPropertyName() + " != ''\">\n");
                    bw.write("\t\t\t\t" + filedInfo.getFieldName() + "=#{bean." + filedInfo.getPropertyName() + "},\n");
                    bw.write("\t\t\t</if>\n");
                }
                else
                {
                    bw.write("\t\t\t<if test=\"bean." + filedInfo.getPropertyName() + " != null\">\n");
                    bw.write("\t\t\t\t" + filedInfo.getFieldName() + "=#{bean." + filedInfo.getPropertyName() + "},\n");
                    bw.write("\t\t\t</if>\n");
                }
            }
            bw.write("\t\t</set>\n");
            where.delete(where.length() - 4, where.length());
            bw.write("\t\twhere "+where+"\n");
            bw.write("\t</update>\n");
            bw.write("\n");
        }
    }
}
