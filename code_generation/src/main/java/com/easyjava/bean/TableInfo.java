package com.easyjava.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TableInfo {
    // 表名
    private String tableName;


    //bean名称
    private String beanName;
    private String beanParaName;

    // 表注释
    private String tableComment;

    // 表字段信息
    private List<FieldInfo> columnInfo;

    //扩张字段信息
    private List<FieldInfo> extendInfo;    

    //唯一索引集合
    private Map<String, List<FieldInfo>> keyIndexMap=new LinkedHashMap<>();

    //是否有data类型
    private boolean hasDate=false;

    //是否有datatime类型
    private boolean hasDateTime=false;

    //是否有bigdecimal类型
    private boolean hasBigDecimal=false;

    // //是否有自增长类型
    // private boolean hasAutoIncrement=false;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public List<FieldInfo> getColumnInfo() {
        return columnInfo;
    }

    public Map<String, List<FieldInfo>> getKeyIndexMap() {
        return keyIndexMap;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public void setColumnInfo(List<FieldInfo> columnInfo) {
        this.columnInfo = columnInfo;
    }

    public void setKeyIndexMap(Map<String, List<FieldInfo>> keyIndexMap) {
        this.keyIndexMap = keyIndexMap;
    }

    public String getBeanParaName() {
        return beanParaName;
    }

    public void setBeanParaName(String beanParaName) {
        this.beanParaName = beanParaName;
    }

    public boolean isHasDate() {
        return hasDate;
    }

    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    public boolean isHasDateTime() {
        return hasDateTime;
    }

    public void setHasDateTime(boolean hasDateTime) {
        this.hasDateTime = hasDateTime;
    }

    public boolean isHasBigDecimal() {
        return hasBigDecimal;
    }

    public void setHasBigDecimal(boolean hasBigDecimal) {
        this.hasBigDecimal = hasBigDecimal;
    }

    public List<FieldInfo> getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(List<FieldInfo> extendInfo) {
        this.extendInfo = extendInfo;
    }

    // public boolean isHasAutoIncrement() {
    //     return hasAutoIncrement;
    // }

    // public void setHasAutoIncrement(boolean hasAutoIncrement) {
    //     this.hasAutoIncrement = hasAutoIncrement;
    // }
    
}
