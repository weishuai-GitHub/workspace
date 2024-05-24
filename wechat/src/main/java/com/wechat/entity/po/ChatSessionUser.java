package com.wechat.entity.po;

import java.io.Serializable;

import com.wechat.entity.enums.UserContactTypeEnums;


/**
 * @Description: 会话用户
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public class ChatSessionUser implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 联系人ID
     */
    private String contactId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 联系人名称
     */
    private String contactName;

    private String lastMessage;

    private Long lastReceiveTime;

    private Integer memberCount;

    @SuppressWarnings("unused")
    private Integer contactType;

    public Integer getContactType() {
        // if(contactId == null)
        //     contactType = UserContactTypeEnums.getEnumByPrefix(contactId.substring(0,1)).getCode();
        return UserContactTypeEnums.getEnumByPrefix(contactId.substring(0,1)).getCode();
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
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

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return this.contactName;
    }
    
    public String getLastMassage() {
        return lastMessage;
    }

    public void setLastMassage(String lastMassege) {
        this.lastMessage = lastMassege;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastReceiveTime() {
        return lastReceiveTime;
    }



    public void setLastReceiveTime(Long lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }

    @Override
    public String toString() {
        return "ChatSessionUser {" +
                " \"用户ID\": " + userId + ","+
                " \"联系人ID\": " + contactId + ","+
                " \"会话ID\": " + sessionId + ","+
                " \"联系人名称\": " + contactName + ","+
                "}";
    }
}
