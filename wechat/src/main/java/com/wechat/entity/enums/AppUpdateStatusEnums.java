package com.wechat.entity.enums;

public enum AppUpdateStatusEnums {
    INIT(0,"未发布"),
    GRAYSACLE(1,"灰度发布"),
    all(2,"全网发布");

    private Integer status;
    private String desc;

    AppUpdateStatusEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static AppUpdateStatusEnums getByStatus(Integer status){
        for(AppUpdateStatusEnums enums : AppUpdateStatusEnums.values()){
            if(enums.getStatus().equals(status)){
                return enums;
            }
        }
        return null;
    }
}
