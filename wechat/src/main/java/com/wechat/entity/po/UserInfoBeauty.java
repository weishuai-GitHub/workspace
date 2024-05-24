package com.wechat.entity.po;

import java.io.Serializable;


/**
 * @Description: 靓号信息
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
public class UserInfoBeauty implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 状态 0: 未使用 1：已使用
     */
    private Integer status;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUseId(String useId) {
        this.userId = useId;
    }

    public String getUseId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setStaus(Integer staus) {
        this.status = staus;
    }

    public Integer getStaus() {
        return this.status;
    }
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserInfoBeauty {" +
                " \"id\": " + id + ","+
                " \"邮箱\": " + email + ","+
                " \"用户ID\": " + userId + ","+
                " \"状态 0: 未使用 1：已使用\": " + status + ","+
                "}";
    }

    
}
