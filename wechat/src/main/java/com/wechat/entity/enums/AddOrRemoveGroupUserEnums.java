package com.wechat.entity.enums;

public enum AddOrRemoveGroupUserEnums {
    REMOVE(0, "移除"),
    ADD(1, "添加");
    
    private Integer code;
    private String desc;

    AddOrRemoveGroupUserEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AddOrRemoveGroupUserEnums getByCode(Integer code) {
        for (AddOrRemoveGroupUserEnums value : AddOrRemoveGroupUserEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
