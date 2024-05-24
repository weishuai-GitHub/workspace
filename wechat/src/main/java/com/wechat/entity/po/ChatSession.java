package com.wechat.entity.po;

import java.io.Serializable;


/**
 * @Description: 会话ID
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public class ChatSession implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 最后接受的消息
     */
    private String lastMassage;

    /**
     * 最后接受消息时间
     */
    private Long lastReceiveTime;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setLastMassage(String lastMassage) {
        this.lastMassage = lastMassage;
    }

    public String getLastMassage() {
        return this.lastMassage;
    }

    public void setLastReceiveTime(Long lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }

    public Long getLastReceiveTime() {
        return this.lastReceiveTime;
    }

    @Override
    public String toString() {
        return "ChatSession {" +
                " \"会话ID\": " + sessionId + ","+
                " \"最后接受的消息\": " + lastMassage + ","+
                " \"最后接受消息时间\": " + lastReceiveTime + ","+
                "}";
    }
}
