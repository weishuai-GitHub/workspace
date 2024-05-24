package com.wechat.entity.dto;

import java.io.Serializable;

public class TokenUserInfo implements Serializable{
    private static final long serialVersionUID = -3244953187037580876L;

    private String token;
    private String userId;
    private String nickName;
    private Boolean admin;

    public TokenUserInfo() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    
}
