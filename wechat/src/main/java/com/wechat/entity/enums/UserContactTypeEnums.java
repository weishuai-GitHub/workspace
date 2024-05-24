package com.wechat.entity.enums;

import org.apache.commons.lang3.StringUtils;

public enum UserContactTypeEnums {
    USER(0,"U","用户"),
    GROUP(1,"G","群组");

    private Integer code;
    private String prefix;
    private String msg;

    UserContactTypeEnums(Integer code, String type, String msg) {
        this.code = code;
        this.prefix = type;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static UserContactTypeEnums getEnumByCode(Integer code){
        for(UserContactTypeEnums enums : UserContactTypeEnums.values()){
            if(enums.getCode().equals(code)){
                return enums;
            }
        }
        return null;
    }

    public static UserContactTypeEnums getEnumByName(String name){
        for(UserContactTypeEnums enums : UserContactTypeEnums.values()){
            if(enums.name().equals(name)){
                return enums;
            }
        }
        return null;
    }

    public static UserContactTypeEnums getEnumByPrefix(String prefix){
        try {
            if(StringUtils.isEmpty(prefix) || prefix.trim().length() == 0){
                return null;
            }
            for(UserContactTypeEnums enums : UserContactTypeEnums.values()){
                if(enums.getPrefix().equals(prefix)){
                    return enums;
                }
            }
        } catch (Exception e) {
            return null;
        }
        
        return null;
    }
    
}
