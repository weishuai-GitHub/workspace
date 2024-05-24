package com.wechat.entity.po;

import java.io.Serializable;

import com.wechat.entity.enums.UserContactStatusEnums;


/**
 * @Description: 联系人申请信息
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public class UserContactApply implements Serializable {
    /**
     * 自增ID
     */
    private Integer applyId;

    /**
     * 申请人ID
     */
    private String applyUserId;

    /**
     * 接受人ID
     */
    private String receiveUserId;

    /**
     * 联系人类型 0：好友 1：群组
     */
    private Integer contactType;

    /**
     * 联系人或群组ID
     */
    private String contactId;

    /**
     * 最后申请时间
     */
    private Long lastApplyTime;

    /**
     * 状态 0：待处理 1：已同意 2：已拒绝 3：已拉黑
     */
    private Integer status;

    /**
     * 申请信息
     */
    private String applyInfo;

    /**
     * 申请人名称
     */
    private String contactName;

    /**
     * 状态名称
     */
    @SuppressWarnings("unused")
    private String statusName;

    public String getStatusName() {
        UserContactStatusEnums userContactStatusEnums = UserContactStatusEnums.getEnumByStatus(status);

        return userContactStatusEnums==null?"":userContactStatusEnums.getMsg();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getApplyId() {
        return this.applyId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserId() {
        return this.applyUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUserId() {
        return this.receiveUserId;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getContactType() {
        return this.contactType;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setLastApplyTime(Long lastApplyTime) {
        this.lastApplyTime = lastApplyTime;
    }

    public Long getLastApplyTime() {
        return this.lastApplyTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setApplyInfo(String applyInfo) {
        this.applyInfo = applyInfo;
    }

    public String getApplyInfo() {
        return this.applyInfo;
    }

    @Override
    public String toString() {
        return "UserContactApply {" +
                " \"自增ID\": " + applyId + ","+
                " \"申请人ID\": " + applyUserId + ","+
                " \"接受人ID\": " + receiveUserId + ","+
                " \"联系人类型 0：好友 1：群组\": " + contactType + ","+
                " \"联系人或群组ID\": " + contactId + ","+
                " \"最后申请时间\": " + lastApplyTime + ","+
                " \"状态 0：待处理 1：已同意 2：已拒绝 3：已拉黑\": " + status + ","+
                " \"申请信息\": " + applyInfo + ","+
                "}";
    }
}
