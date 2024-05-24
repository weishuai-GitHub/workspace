package com.wechat.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.wechat.entity.constants.Constans;
import com.wechat.entity.enums.UserContactTypeEnums;

import java.util.ArrayList;
import java.util.List;



public class ToolUtils {
    public static String getUserId(){
        return UserContactTypeEnums.USER.getPrefix() + getRandNum(11);
    }
    public static String getGroupId(){
        return UserContactTypeEnums.GROUP.getPrefix() + getRandNum(11);
    }

    public static final String getRandNum(int num){
        return RandomStringUtils.random(Constans.LENGHT_11,false,true);
    }

    public static final String getRandStr(int num){
        return RandomStringUtils.random(num,true,true);
    }

    public static final String encodeMD5(String str){
        return StringUtils.isEmpty(str)? null : DigestUtils.md5Hex(str);
    }

    @SuppressWarnings("null")
    public static <T,S> List<T> copyList(List<S> sourceList, Class<T> classz){
        if(sourceList == null || sourceList.isEmpty()){
            return null;
        }
        List<T> targetList = new ArrayList<>();
        for(S source : sourceList){
            T t = null;
            try {
                t = classz.getDeclaredConstructor().newInstance();
            } catch (Exception e){
                e.printStackTrace();
            }
            BeanUtils.copyProperties(source,t);
            targetList.add(t);
        }
        return targetList;
    }

    @SuppressWarnings("null")
    public static <T,S> T copy(S source, Class<T> classz){
        if(source == null){
            return null;
        }
        T t = null;
        try {
            t = classz.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source,t);
        return t;
    }

    public static String cleanHtmlTag(String htmlStr){
        if(StringUtils.isEmpty(htmlStr)){
            return htmlStr;
        }
        htmlStr = htmlStr.replace("<", "&lt");
        htmlStr = htmlStr.replace("\t\n", "<br>");
        // htmlStr = htmlStr.replace(">", "&gt");
        htmlStr = htmlStr.replace("\n", "<br>");
        return htmlStr;
    }

    public static final String getChatSessionId4User(String userId, String contactId){
        String tmpString =  userId.compareTo(contactId) > 0 ? userId + contactId : contactId + userId;
        return encodeMD5(tmpString);
    }
    public static final String getChatSessionId4Group(String contactId) {
        return encodeMD5(contactId);
    }

    public static String getFileSuffix(String fileName){
        if(StringUtils.isEmpty(fileName)){
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if(index == -1){
            return null;
        }
        return fileName.substring(index);
    }

    public static Boolean isNumber(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return str.matches("^[0-9]+$");
    }
}
