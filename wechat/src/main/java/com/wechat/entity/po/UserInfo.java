package com.wechat.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.wechat.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @Description: 用户信息
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
public class UserInfo implements Serializable {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 加入方式 0：直接加入 1：同意后加好友
     */
    private Integer joinType;

    /**
     * 性别 0：女 1：男
     */
    private Integer sex;

    /**
     * 密码
     */
    private String password;

    /**
     * 个性签名
     */
    private String personalSignature;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 最后登陆时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 地区
     */
    private String areaName;

    /**
     * 地区编号
     */
    private String areaCode;

    /**
     * 最后离线时间
     */
    private Long lastOffTime;

    @SuppressWarnings("unused")
    private Integer onlineType;

    public Integer getOnlineType() {
        if(lastLoginTime!=null &&lastLoginTime.getTime()>lastOffTime){
            return 1;
        }
        return 0;
    }

    public void setOnlineType(Integer onLineType) {
        this.onlineType = onLineType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUseId(String useId) {
        this.userId = useId;
    }

    public String getUseId() {
        return this.userId;
    }

    public String getUserId(){
        return this.userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public Integer getJoinType() {
        return this.joinType;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getPersonalSignature() {
        return this.personalSignature;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setLastOffTime(Long lastOffTime) {
        this.lastOffTime = lastOffTime;
    }

    public Long getLastOffTime() {
        return this.lastOffTime;
    }

    @Override
    public String toString() {
        return "UserInfo {" +
                " \"用户ID\": " + userId + ","+
                " \"邮箱\": " + email + ","+
                " \"昵称\": " + nickName + ","+
                " \"加入方式 0：直接加入 1：同意后加好友\": " + joinType + ","+
                " \"性别 0：女 1：男\": " + sex + ","+
                " \"密码\": " + password + ","+
                " \"个性签名\": " + personalSignature + ","+
                " \"状态\": " + status + ","+
                " \"创建时间\": " + DateUtils.format(createTime, DateUtils.yyyy_MM_dd_HH_mm_ss) + ","+
                " \"最后登陆时间\": " + DateUtils.format(lastLoginTime, DateUtils.yyyy_MM_dd_HH_mm_ss) + ","+
                " \"地区\": " + areaName + ","+
                " \"地区编号\": " + areaCode + ","+
                " \"最后离线时间\": " + lastOffTime + ","+
                "}";
    }
}
