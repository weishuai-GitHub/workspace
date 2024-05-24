package com.wechat.redis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.SysSettingDto;
import com.wechat.entity.dto.TokenUserInfo;

import jakarta.annotation.Resource;

@Component("redisCompent")
public class RedisCompent {
    @Resource
    private RedisUtils redisUtil;

    /*
     * @Description: 获取用户心跳
     */
    public Long getUserHeartBeat(String userId) {
        return (Long)this.redisUtil.get(Constans.REDIS_KEY_WS_USER_HEART_BEAT+userId);
    }

    public void saveUserHeartBeat(String userId){
        this.redisUtil.set(Constans.REDIS_KEY_WS_USER_HEART_BEAT+userId, System.currentTimeMillis(),Constans.REDIS_KEY_EXPRESS_HEART_BEAT);
    }

    /**
     * @Description: 保存token信息
     * @param tokenUserInfo
     */
    public void saveTokenUserInfo(TokenUserInfo tokenUserInfo) {
        redisUtil.set(Constans.REDIS_KEY_WS_TOKEN + tokenUserInfo.getToken(), tokenUserInfo, Constans.REDIS_KEY_EXPRESS_DAT*2);
        redisUtil.set(Constans.REDIS_KEY_WS_TOKEN_USERID+ tokenUserInfo.getUserId(), tokenUserInfo.getToken(), Constans.REDIS_KEY_EXPRESS_DAT*2);
    }

    public void cleanTokenUserInfoByUserId(String userId){
        String token = (String)redisUtil.get(Constans.REDIS_KEY_WS_TOKEN_USERID + userId);
        if(StringUtils.isEmpty(token)) return;
        redisUtil.del(Constans.REDIS_KEY_WS_TOKEN + token);
    }

    public void removeHeatBeat(String userId) {
        redisUtil.del(Constans.REDIS_KEY_WS_USER_HEART_BEAT+userId);
     }

    public TokenUserInfo getTokenUserInfo(String token) {
        return (TokenUserInfo) redisUtil.get(Constans.REDIS_KEY_WS_TOKEN + token);
    }

    public TokenUserInfo getTokenUserInfoByuserId(String userId) {
        String token = (String)redisUtil.get(Constans.REDIS_KEY_WS_TOKEN_USERID + userId);
        return getTokenUserInfo(token);
    }

    /**
     * @Description: 得到系统设置
     * @param token
     * @return
     */
    public SysSettingDto getSysSetting() {
        SysSettingDto sysSettingDto = (SysSettingDto)redisUtil.get(Constans.REDIS_KEY_SYS_SETTING);
        if(sysSettingDto == null) {
            sysSettingDto = new SysSettingDto();
            redisUtil.set(Constans.REDIS_KEY_SYS_SETTING, sysSettingDto, Constans.REDIS_KEY_EXPRESS_DAT);
        }
        return sysSettingDto;
    }

    /**
     * @Description: 保存系统设置
     * @param sysSettingDto
     */
    public void saveSysSetting(SysSettingDto sysSettingDto) {
        redisUtil.set(Constans.REDIS_KEY_SYS_SETTING, sysSettingDto);
    }

    // 清空联系人
    public void cleanUserContactBatch(String userId){
        redisUtil.del(Constans.REDIS_KEY_USER_CONTACT+ userId);
    }

    //批量添加联系人
    public void addUserContactBatch(String userId,List<String> contactList){
        redisUtil.set(Constans.REDIS_KEY_USER_CONTACT+ userId, contactList,Constans.REDIS_KEY_EXPRESS_TOKEN);
    }
    
    //批量添加联系人
    @SuppressWarnings({ "unchecked" })
    public List<String>  getUserContactBatch(String userId){
        return (List<String>)redisUtil.get(Constans.REDIS_KEY_USER_CONTACT+ userId);
    }

    //添加联系人
    public void addUserContact(String userId,String contactId){
        List<String> contactList = getUserContactBatch(userId);
        if(contactList == null){
            contactList = new ArrayList<>();
        }
        if(contactList.contains(contactId)){
            return;
        }
        contactList.add(contactId);
        addUserContactBatch(userId, contactList);
    }

    //删除联系人
    public void removeUserContact(String userId,String contactId){
        List<String> contactList = getUserContactBatch(userId);
        if(contactList == null){
            return;
        }
        contactList.remove(contactId);
        addUserContactBatch(userId, contactList);
    }
    
}

