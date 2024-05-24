package com.wechat.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.wechat.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @Description: 联系人信息
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public class UserContact implements Serializable {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 联系人ID或群组ID
     */
    private String contactId;

    /**
     * 联系人类型 0：好友 1：群组
     */
    private Integer contactType;

    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 状态0：非好友 1：好友 2：已删除好友 3：被好友删除 4：已拉黑好友 5：别好友拉黑
     */
    private Integer status;

    /**
     * 最后更新时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**
     * 联系人名称
     */
    private String contactName;

    /**
     * 性别
     */
    private Integer sex;

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getSex() {
        return sex;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getContactType() {
        return this.contactType;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    @Override
    public String toString() {
        return "UserContact {" +
                " \"用户ID\": " + userId + ","+
                " \"联系人ID或群组ID\": " + contactId + ","+
                " \"联系人类型 0：好友 1：群组\": " + contactType + ","+
                " \"创建时间\": " + DateUtils.format(createTime, DateUtils.yyyy_MM_dd_HH_mm_ss) + ","+
                " \"状态0：非好友 1：好友 2：已删除好友 3：被好友删除 4：已拉黑好友 5：别好友拉黑\": " + status + ","+
                " \"最后更新时间\": " + DateUtils.format(lastUpdateTime, DateUtils.yyyy_MM_dd_HH_mm_ss) + ","+
                "}";
    }
}
