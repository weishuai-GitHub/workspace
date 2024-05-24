package com.wechat.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.wechat.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @Description: app发布
 *
 * @author: ShuaiWei
 * @date: 2024/05/19
 */
public class AppUpdate implements Serializable {
    /**
     * 自增
     */
    private Integer id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新描述
     */
    private String updateDesc;

    /**
     * 创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 0：未发布 1：灰度发布 2：全网发布
     */
    private Integer status;

    /**
     * 灰度uid
     */
    private String grayscaleUid;

    /**
     * 文件类型0：本地文件 1：外链
     */
    private Integer fileType;

    /**
     * 外链地址
     */
    private String outerLink;

    private String[] updateDescArray;

    private String fileName;

    

    public String[] getUpdateDescArray() {
        if(updateDesc != null){
            return updateDesc.split("\\|");
        }
        return updateDescArray;
    }

    public void setUpdateDescArray(String[] updateDescArray) {
        this.updateDescArray = updateDescArray;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public void setUpdateDesc(String updateDesc) {
        this.updateDesc = updateDesc;
    }

    public String getUpdateDesc() {
        return this.updateDesc;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setGrayscaleUid(String grayscaleUid) {
        this.grayscaleUid = grayscaleUid;
    }

    public String getGrayscaleUid() {
        return this.grayscaleUid;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getFileType() {
        return this.fileType;
    }

    public void setOuterLink(String outerLink) {
        this.outerLink = outerLink;
    }

    public String getOuterLink() {
        return this.outerLink;
    }

    @Override
    public String toString() {
        return "AppUpdate {" +
                " \"自增\": " + id + ","+
                " \"版本号\": " + version + ","+
                " \"更新描述\": " + updateDesc + ","+
                " \"创建时间\": " + DateUtils.format(createTime, DateUtils.yyyy_MM_dd_HH_mm_ss) + ","+
                " \"0：未发布 1：灰度发布 2：全网发布\": " + status + ","+
                " \"灰度uid\": " + grayscaleUid + ","+
                " \"文件类型0：本地文件 1：外链\": " + fileType + ","+
                " \"外链地址\": " + outerLink + ","+
                "}";
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
