package com.wechat.entity.query;



/**
 * @Description: 会话ID
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public class ChatSessionQuery extends BaseQuery {
    /**
     * 会话ID
     */
    private String sessionId;

    private String sessionIdFuzzy;

    /**
     * 最后接受的消息
     */
    private String lastMassage;

    private String lastMassageFuzzy;

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

    public void setSessionIdFuzzy(String sessionIdFuzzy) {
        this.sessionIdFuzzy = sessionIdFuzzy;
    }

    public String getSessionIdFuzzy() {
        return this.sessionIdFuzzy;
    }

    public void setLastMassageFuzzy(String lastMassageFuzzy) {
        this.lastMassageFuzzy = lastMassageFuzzy;
    }

    public String getLastMassageFuzzy() {
        return this.lastMassageFuzzy;
    }

}
