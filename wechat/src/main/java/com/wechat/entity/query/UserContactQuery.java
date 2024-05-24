package com.wechat.entity.query;



/**
 * @Description: 联系人信息
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public class UserContactQuery extends BaseQuery {
    /**
     * 用户ID
     */
    private String userId;

    private String userIdFuzzy;

    /**
     * 联系人ID或群组ID
     */
    private String contactId;

    private String contactIdFuzzy;

    /**
     * 联系人类型 0：好友 1：群组
     */
    private Integer contactType;

    /**
     * 创建时间
     */
    private String createTime;

    private String createTimeStart;

    private String createTimeEnd;

    /**
     * 状态0：非好友 1：好友 2：已删除好友 3：被好友删除 4：已拉黑好友 5：别好友拉黑
     */
    private Integer status;

    /**
     * 最后更新时间
     */
    private String lastUpdateTime;

    private String lastUpdateTimeStart;

    private String lastUpdateTimeEnd;

    /**
     * 是否查询用户信息
     */
    private Boolean queryUserInfo;

    private Boolean queryContactInfo;

    /**
     * 是否查询群组信息
     */
    private Boolean queryGroupInfo;

    private Boolean excludeMyGroup;

    private Integer[] statusArr;

    public Boolean getQueryContactInfo() {
        return queryContactInfo;
    }

    public void setQueryContactInfo(Boolean queryContactInfo) {
        this.queryContactInfo = queryContactInfo;
    }

    
    
    public Boolean getExcludeMyGroup() {
        return excludeMyGroup;
    }

    public void setExcludeMyGroup(Boolean excludeMyGroup) {
        this.excludeMyGroup = excludeMyGroup;
    }

    public Integer[] getStatusArr() {
        return statusArr;
    }

    public void setStatusArr(Integer[] statusArr) {
        this.statusArr = statusArr;
    }

    public Boolean getQueryGroupInfo() {
        return queryGroupInfo;
    }

    public void setQueryGroupInfo(Boolean queryGroupInfo) {
        this.queryGroupInfo = queryGroupInfo;
    }

    public Boolean getQueryUserInfo() {
        return queryUserInfo;
    }

    public void setQueryUserInfo(Boolean queryUserInfo) {
        this.queryUserInfo = queryUserInfo;
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setUserIdFuzzy(String userIdFuzzy) {
        this.userIdFuzzy = userIdFuzzy;
    }

    public String getUserIdFuzzy() {
        return this.userIdFuzzy;
    }

    public void setContactIdFuzzy(String contactIdFuzzy) {
        this.contactIdFuzzy = contactIdFuzzy;
    }

    public String getContactIdFuzzy() {
        return this.contactIdFuzzy;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeStart() {
        return this.createTimeStart;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getCreateTimeEnd() {
        return this.createTimeEnd;
    }

    public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
        this.lastUpdateTimeStart = lastUpdateTimeStart;
    }

    public String getLastUpdateTimeStart() {
        return this.lastUpdateTimeStart;
    }

    public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
        this.lastUpdateTimeEnd = lastUpdateTimeEnd;
    }

    public String getLastUpdateTimeEnd() {
        return this.lastUpdateTimeEnd;
    }

}
