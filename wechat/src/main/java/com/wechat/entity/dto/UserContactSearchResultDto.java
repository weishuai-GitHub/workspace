package com.wechat.entity.dto;

public class UserContactSearchResultDto {
    private String contactId;
    private String contactType;
    private String nickName;

    private String statusName;
    private String sex;
    // private String avatarLastUpdate;
    private Integer status;
    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    private String areaName;

    public UserContactSearchResultDto() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    // public String getAvatarLastUpdate() {
    //     return avatarLastUpdate;
    // }

    // public void setAvatarLastUpdate(String avatarLastUpdate) {
    //     this.avatarLastUpdate = avatarLastUpdate;
    // }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
    
}
