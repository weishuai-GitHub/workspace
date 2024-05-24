package com.wechat.entity.dto;

import java.util.List;

import com.wechat.entity.po.ChatMeaasge;
import com.wechat.entity.po.ChatSessionUser;

public class WsInitData {
    private List<ChatSessionUser> chatSessionList;

    private List<ChatMeaasge> chatMessageList;

    private Integer applyCount;

    public Integer getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }

    public List<ChatSessionUser> getChatSessionList() {
        return chatSessionList;
    }

    public void setChatSessionList(List<ChatSessionUser> chatSessionList) {
        this.chatSessionList = chatSessionList;
    }

    public List<ChatMeaasge> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatMeaasge> chatMessageList) {
        this.chatMessageList = chatMessageList;
    } 
}
