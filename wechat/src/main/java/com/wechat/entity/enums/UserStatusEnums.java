package com.wechat.entity.enums;

public enum UserStatusEnums {
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private Integer code;
    private String msg;

    UserStatusEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static UserStatusEnums getByStatus(Integer status) {
        for (UserStatusEnums userStatusEnums : UserStatusEnums.values()) {
            if (userStatusEnums.getCode().equals(status)) {
                return userStatusEnums;
            }
        }
        return null;
    }
    
    
}
