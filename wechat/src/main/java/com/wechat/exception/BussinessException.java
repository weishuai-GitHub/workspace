package com.wechat.exception;

import com.wechat.entity.vo.ResponseCodeEnum;

public class BussinessException extends Exception{
    private  ResponseCodeEnum CodeEnum;

    private Integer code;

    private String msg;

    public BussinessException(String msg, Throwable e) {
        super(msg,e);
        this.msg = msg;
    }

    public BussinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BussinessException(Throwable e) {
        super(e);
    }

    public BussinessException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getmsg());
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getmsg();
    }

    public BussinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ResponseCodeEnum getCodeEnum() {
        return CodeEnum;
    }

    public void setCodeEnum(ResponseCodeEnum codeEnum) {
        CodeEnum = codeEnum;
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
}  
