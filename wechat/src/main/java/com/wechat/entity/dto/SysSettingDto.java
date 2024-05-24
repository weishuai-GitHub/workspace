package com.wechat.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wechat.entity.constants.Constans;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingDto implements Serializable{

    private static final long serialVersionUID = -3244953187037580876L;

    private Integer maxGroupCount = 5;
    private Integer maxGroupMemberCount = 500;
    private Integer maxImageSize =2;
    private Integer maxVideoSize = 5;
    private Integer maxFileSize = 2;
    private String robotUid = Constans.ROBOT_UID;
    private String robotNickName = Constans.ROBOT_NICK_NAME;
    private String robotWelcome = "欢迎使用wechat";
    // public static long getSerialversionuid() {
    //     return serialVersionUID;
    // }
    public Integer getMaxGroupCount() {
        return maxGroupCount;
    }
    public void setMaxGroupCount(Integer maxGroupCount) {
        this.maxGroupCount = maxGroupCount;
    }
    public Integer getMaxGroupMemberCount() {
        return maxGroupMemberCount;
    }
    public void setMaxGroupMemberCount(Integer maxGroupMemberCount) {
        this.maxGroupMemberCount = maxGroupMemberCount;
    }
    public Integer getMaxImageSize() {
        return maxImageSize;
    }
    public void setMaxImageSize(Integer maxImageSize) {
        this.maxImageSize = maxImageSize;
    }
    public Integer getMaxVideoSize() {
        return maxVideoSize;
    }
    public void setMaxVideoSize(Integer maxVideoSize) {
        this.maxVideoSize = maxVideoSize;
    }
    public Integer getMaxFileSize() {
        return maxFileSize;
    }
    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    public String getRobotUid() {
        return robotUid;
    }
    public void setRobotUid(String robotUid) {
        this.robotUid = robotUid;
    }
    public String getRobotNickName() {
        return robotNickName;
    }
    public void setRobotNickName(String robotNickName) {
        this.robotNickName = robotNickName;
    }
    public String getRobotWelcome() {
        return robotWelcome;
    }
    public void setRobotWelcome(String robotWelcome) {
        this.robotWelcome = robotWelcome;
    }
    
    

}
