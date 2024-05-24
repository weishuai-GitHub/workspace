package com.wechat.entity.vo;

import java.io.Serializable;

import java.util.List;

public class AppUpdateVO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    /**
     * 版本号
     */
    private String version;

    /**
     * 更新内容
     */
    private List<String> updateList;

    private Long size;

    private String fileName;

    private String outerLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<String> updateList) {
        this.updateList = updateList;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOuterLink() {
        return outerLink;
    }

    public void setOuterLink(String outerLink) {
        this.outerLink = outerLink;
    }

    
}
