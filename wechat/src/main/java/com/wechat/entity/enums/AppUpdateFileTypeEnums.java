package com.wechat.entity.enums;

public enum AppUpdateFileTypeEnums {
    LOCAL_FILE(1,"本地文件"),
    NETWORK_FILE(2,"网络文件");

    private Integer type;
    private String desc;

    AppUpdateFileTypeEnums(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static AppUpdateFileTypeEnums getByType(Integer type){
        for(AppUpdateFileTypeEnums enums : AppUpdateFileTypeEnums.values()){
            if(enums.getType().equals(type)){
                return enums;
            }
        }
        return null;
    }
}
