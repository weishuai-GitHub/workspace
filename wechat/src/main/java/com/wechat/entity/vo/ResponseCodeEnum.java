package com.wechat.entity.vo;


public enum ResponseCodeEnum {
    
    CODE_200( 200, "请求成功"),
    CODE_404( 404, "资源不存在"),
    CODE_600( 600, "请求参数错误"),
    CODE_601( 601, "信息已经存在"),
    CODE_602( 602, "文件不存在"),
    CODE_901( 901, "登陆超时，请重新登陆"),
    CODE_902( 902, "你不是对方好友，请先发送好友验证申请"),
    CODE_903( 903, "你已经在群聊，请重新加入群聊"),
    CODE_500( 500, "服务器返回错误，请联系管理员");

    private Integer code;
    private String msg;

    ResponseCodeEnum( Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getmsg() {
        return msg;
    }
}
