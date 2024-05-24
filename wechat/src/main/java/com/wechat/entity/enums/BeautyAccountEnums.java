package com.wechat.entity.enums;

public enum BeautyAccountEnums {
    NO_USE(0, "未使用"),
    USED(1, "已使用");

    private Integer code;
    private String msg;

    BeautyAccountEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
