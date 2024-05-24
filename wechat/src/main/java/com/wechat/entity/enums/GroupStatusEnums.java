package com.wechat.entity.enums;

public enum GroupStatusEnums {
    NORMAL(1,"正常"),
    DEL(0,"已删除");

    private Integer status;
    private String msg;

    GroupStatusEnums(Integer status, String msg) {
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

    
}
