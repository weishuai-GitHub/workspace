package com.wechat.entity.vo;

import java.util.List;

import com.wechat.entity.po.GroupInfo;
import com.wechat.entity.po.UserContact;

public class GroupInfoVo {
    private GroupInfo groupInfo;
    private List<UserContact> userInfoList;
    public GroupInfo getGroupInfo() {
        return groupInfo;
    }
    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
    public List<UserContact> getUserInfoList() {
        return userInfoList;
    }
    public void setUserInfoList(List<UserContact> userInfoList) {
        this.userInfoList = userInfoList;
    }

}
