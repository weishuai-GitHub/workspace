package com.wechat.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.wechat.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @Description: 群组信息
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public class GroupInfo implements Serializable {
    /**
     * 群ID
     */
    private String groupId;

    /**
     * 群组名
     */
    private String groupName;

    /**
     * 群主ID
     */
    private String groupOwnerId;

    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 群公告
     */
    private String groupNotice;

    /**
     * 0:直接加入 1：管理员同意后加入
     */
    private Integer joinType;

    /**
     * 状态 1：正常 0:解散
     */
    private Integer status;

    /**
     * 群成员数
     */
    private Integer memberCount;

    private String groupOwnerNickName;

    public String getGroupOwnerNickName() {
        return groupOwnerNickName;
    }

    public void setGroupOwnerNickName(String groupOwnerNickName) {
        this.groupOwnerNickName = groupOwnerNickName;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupOwnerId(String groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    public String getGroupOwnerId() {
        return this.groupOwnerId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }

    public String getGroupNotice() {
        return this.groupNotice;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public Integer getJoinType() {
        return this.joinType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "GroupInfo {" +
                " \"群ID\": " + groupId + ","+
                " \"群组名\": " + groupName + ","+
                " \"群主ID\": " + groupOwnerId + ","+
                " \"创建时间\": " + DateUtils.format(createTime, DateUtils.yyyy_MM_dd_HH_mm_ss) + ","+
                " \"群公告\": " + groupNotice + ","+
                " \"0:直接加入 1：管理员同意后加入\": " + joinType + ","+
                " \"状态 1：正常 0:解散\": " + status + ","+
                "}";
    }
}
