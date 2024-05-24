package com.wechat.entity.query;



/**
 * @Description: 靓号信息
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
public class UserInfoBeautyQuery extends BaseQuery {
    /**
     * id
     */
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    private String emailFuzzy;

    /**
     * 用户ID
     */
    private String useId;

    private String useIdFuzzy;

    /**
     * 状态 0: 未使用 1：已使用
     */
    private Integer staus;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public String getUseId() {
        return this.useId;
    }

    public void setStaus(Integer staus) {
        this.staus = staus;
    }

    public Integer getStaus() {
        return this.staus;
    }

    public void setEmailFuzzy(String emailFuzzy) {
        this.emailFuzzy = emailFuzzy;
    }

    public String getEmailFuzzy() {
        return this.emailFuzzy;
    }

    public void setUseIdFuzzy(String useIdFuzzy) {
        this.useIdFuzzy = useIdFuzzy;
    }

    public String getUseIdFuzzy() {
        return this.useIdFuzzy;
    }

}
