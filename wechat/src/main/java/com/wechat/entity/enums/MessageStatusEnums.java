
package com.wechat.entity.enums;

public enum MessageStatusEnums {
    SENDING(0,"发送中"),
    SENDED(1,"已发送");

    private Integer status;
    private String desc;

    MessageStatusEnums(Integer status,String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MessageStatusEnums getByStatus(Integer status){
        for(MessageStatusEnums item: MessageStatusEnums.values()){
            if(item.getStatus()==status) return item;
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
}
