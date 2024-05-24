package com.wechat.entity.enums;

import org.apache.commons.lang3.StringUtils;

public enum UserContactStatusEnums{
    NOT_FRIEDND(0,"陌生人"),
    FRIEND(1,"好友"),
    DEL(2,"已删除"),
    DEL_BE(3,"被删除"),
    BLACKLIST(4,"已拉黑"),
    BLACKLIST_BE(5,"被拉黑");

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer status;
    private String msg;

    UserContactStatusEnums(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static UserContactStatusEnums getEnumByStatus(Integer code){
        for(UserContactStatusEnums enums : UserContactStatusEnums.values()){
            if(enums.getStatus().equals(code)){
                return enums;
            }
        }
        return null;
    }

    public static UserContactStatusEnums getEnumByStatus(String code){
        try {
            if(StringUtils.isEmpty(code) || code.trim().length() == 0){
                return null;
            }
            return UserContactStatusEnums.valueOf(code.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    

}