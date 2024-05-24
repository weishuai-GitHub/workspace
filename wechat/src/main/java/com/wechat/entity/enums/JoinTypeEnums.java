
package com.wechat.entity.enums;

public enum JoinTypeEnums {
    JOIN(0, "直接加入"),
    APPAY(1, "需要审核");

    private Integer type;
    private String msg;

    JoinTypeEnums(Integer type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public Integer getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public static JoinTypeEnums getJoinTypeEnums(Integer type) {
        for (JoinTypeEnums joinTypeEnums : JoinTypeEnums.values()) {
            if (joinTypeEnums.getType().equals(type)) {
                return joinTypeEnums;
            }
        }
        return null;
    }

    public static String getJoinTypeEnumsMsg(Integer type) {
        for (JoinTypeEnums joinTypeEnums : JoinTypeEnums.values()) {
            if (joinTypeEnums.getType().equals(type)) {
                return joinTypeEnums.getMsg();
            }
        }
        return null;
    }
}
