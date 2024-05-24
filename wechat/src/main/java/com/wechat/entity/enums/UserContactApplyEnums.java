package com.wechat.entity.enums;

import org.apache.commons.lang3.StringUtils;

public enum UserContactApplyEnums {
    INIT(0, "未处理"),
    PASS(1, "通过"),
    REJECT(2, "拒绝"),
    BLACKLIST(3, "已拉黑");

    private Integer status;
    private String msg;

    UserContactApplyEnums(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static UserContactApplyEnums getEnumByStatus(Integer code) {
        for (UserContactApplyEnums enums : UserContactApplyEnums.values()) {
            if (enums.getStatus().equals(code)) {
                return enums;
            }
        }
        return null;
    }

    public static UserContactApplyEnums getEnumByStatus(String code) {
        try {
            if (StringUtils.isEmpty(code) || code.trim().length() == 0) {
                return null;
            }
            return UserContactApplyEnums.valueOf(code.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
