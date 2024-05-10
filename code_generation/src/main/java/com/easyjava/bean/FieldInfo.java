package com.easyjava.bean;

public class FieldInfo {
    // 字段名
    private String FieldName;

    // bean属性名
    private String propertyName;

    // sql字段类型
    private String sqlType;

    // 对应的java字段类型
    private String javaType;

    // 字段注释
    private String comment;

    // field 是否自增
    private boolean isAutoIncrement;

    public String getFieldName() {
        return FieldName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getSqlType() {
        return sqlType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getComment() {
        return comment;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setFieldName(String fieldName) {
        FieldName = fieldName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

}
