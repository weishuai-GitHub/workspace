package com.wechat.websocket;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.MessageSendDto;
import com.wechat.entity.dto.WsInitData;
import com.wechat.entity.enums.MessageTypeEnums;
import com.wechat.entity.enums.UserContactApplyEnums;
import com.wechat.entity.enums.UserContactTypeEnums;
import com.wechat.entity.po.ChatMeaasge;
import com.wechat.entity.po.ChatSessionUser;
import com.wechat.entity.po.UserContactApply;
import com.wechat.entity.po.UserInfo;
import com.wechat.entity.query.ChatMeaasgeQuery;
import com.wechat.entity.query.ChatSessionUserQuery;
import com.wechat.entity.query.UserContactApplyQuery;
import com.wechat.entity.query.UserInfoQuery;
import com.wechat.mappers.ChatMeaasgeMapper;
import com.wechat.mappers.ChatSessionUserMapper;
import com.wechat.mappers.UserContactApplyMapper;
import com.wechat.mappers.UserInfoMapper;
import com.wechat.redis.RedisCompent;
import com.wechat.utils.JsonUtils;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

@Component("channelContextUtils")
public class ChannelContextUtils {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ChannelContextUtils.class);

    private static final ConcurrentHashMap<String,Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String,ChannelGroup> GROUP_CONTEXT_MAP = new ConcurrentHashMap<>();

    @Resource
    private RedisCompent redisCompent;

    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;

    @Resource
    private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private ChatMeaasgeMapper<ChatMeaasge,ChatMeaasgeQuery> chatMeaasgeMapper;

    @Resource
    private UserContactApplyMapper<UserContactApply,UserContactApplyQuery> userContactApplyMapper;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addContext(String userId,Channel channel){
        String channelId = channel.id().toString();
        AttributeKey attributeKey = null;;
        if(!AttributeKey.exists(channelId)){
            attributeKey = AttributeKey.newInstance(channelId);
        }else{

            attributeKey = AttributeKey.valueOf(channelId);
        }
        channel.attr(attributeKey).set(userId);
        
        List<String> contactIdList = redisCompent.getUserContactBatch(userId);
        for(String groupId:contactIdList)
        {
            if(groupId.startsWith(UserContactTypeEnums.GROUP.getPrefix()))
                add2Group(groupId, channel);
        }
        USER_CONTEXT_MAP.put(userId, channel);
        redisCompent.saveUserHeartBeat(userId);

        //更新用户最后连接时间
        UserInfo updateInfo = new UserInfo();
        updateInfo.setLastLoginTime(new Date());
        userInfoMapper.updateByUseId(updateInfo, userId);

        //给用户发送消息
        UserInfo userInfo  = userInfoMapper.selectByUseId(userId);
        Long sourceLastOffTime = userInfo.getLastOffTime();
        Long lastOffTime = sourceLastOffTime;
        if(sourceLastOffTime!=null && System.currentTimeMillis()-Constans.TIMEMILLIS_3DAYS_AGC>sourceLastOffTime){
            lastOffTime = Constans.TIMEMILLIS_3DAYS_AGC;
        }

        /**
         *  1. 查询会话信息，查询用户所有回话信息。保证换了设备同步
         */
        ChatSessionUserQuery chatSessionUserQuery = new ChatSessionUserQuery();
        chatSessionUserQuery.setUserId(userId);
        chatSessionUserQuery.setOrderBy("last_receive_time desc");
        List<ChatSessionUser> chatSessionUseList = chatSessionUserMapper.findListParam(chatSessionUserQuery);
        
        WsInitData wsInitData = new WsInitData();
        wsInitData.setChatSessionList(chatSessionUseList);

        /**
         * 2. 查询聊天消息
         */
        List<String> groupIdList = contactIdList.stream()
        .filter(item->item.endsWith(UserContactTypeEnums.GROUP.getPrefix()))
        .collect(Collectors.toList());
        groupIdList.add(userId);
        ChatMeaasgeQuery chatMeaasgeQuery = new ChatMeaasgeQuery();
        chatMeaasgeQuery.setContactIdList(groupIdList);
        chatMeaasgeQuery.setLastReciveTime(lastOffTime);
        List<ChatMeaasge> chatMeaasgeList = chatMeaasgeMapper.selectList(chatMeaasgeQuery);
        wsInitData.setChatMessageList(chatMeaasgeList);
        /**
         * 3. 查询好友申请
        */
        UserContactApplyQuery userContactApplyQuery = new UserContactApplyQuery();
        userContactApplyQuery.setReceiveUserId(userId);
        userContactApplyQuery.setStatus(UserContactApplyEnums.INIT.getStatus());
        userContactApplyQuery.setLastApplyTimestamp(lastOffTime);
        Integer applyCount = userContactApplyMapper.selectCount(userContactApplyQuery);
        wsInitData.setApplyCount(applyCount);

        //发送消息
        MessageSendDto mewMessageSendDto = new MessageSendDto<>();
        mewMessageSendDto.setMessageType(MessageTypeEnums.INIT.getType());
        mewMessageSendDto.setContactId(userId);
        mewMessageSendDto.setExtendData(wsInitData);
        sendMsg(mewMessageSendDto, userId);

    }


    private void add2Group(String groupId,Channel channel){
        ChannelGroup group = GROUP_CONTEXT_MAP.get(groupId);
        if(group==null){
            group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            GROUP_CONTEXT_MAP.put(groupId, group);
        }
        if(channel==null) return;
        group.add(channel);
    }

    public void addUser2Group(String userId,String groupId){
        Channel channel = USER_CONTEXT_MAP.get(userId);
        if(channel==null) return;
        add2Group(groupId, channel);
    }

    // public void send2Group(String massage){
    //     ChannelGroup group = GROUP_CONTEXT_MAP.get("100000");
    //     group.writeAndFlush(new TextWebSocketFrame(massage));
    // }

    public void removeContext(Channel channel){
        Attribute<Object> attributeKey = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = (String) attributeKey.get();
        if(!userId.isEmpty()){
            USER_CONTEXT_MAP.remove(userId);
        }
        redisCompent.removeHeatBeat(userId);
        //更新用户最后离线时间
        UserInfo userInfo  = userInfoMapper.selectByUseId(userId);
        userInfo.setLastOffTime(System.currentTimeMillis());
        userInfoMapper.updateByUseId(userInfo, userId);
    }

    public void sendMessage(@SuppressWarnings("rawtypes") MessageSendDto mewMessageSendDto){
        UserContactTypeEnums userContactTypeEnums = UserContactTypeEnums.getEnumByPrefix(mewMessageSendDto.getContactId().substring(0,1));
        switch (userContactTypeEnums) {
            case USER:
                send2User(mewMessageSendDto);
                break;
            case GROUP:
                send2Group(mewMessageSendDto);
                break;
            default:
                break;
        }
    }

    public void colseContext(String userId){
        if(StringUtils.isEmpty(userId)) return;
        redisCompent.cleanTokenUserInfoByUserId(userId);
        Channel channel = USER_CONTEXT_MAP.get(userId);
        if(channel!=null) channel.close();

    }

    //发送消息给用户
    public void send2User(@SuppressWarnings("rawtypes") MessageSendDto mewMessageSendDto){
        String contactId = mewMessageSendDto.getContactId();
        if(StringUtils.isEmpty(contactId)) return;
        sendMsg(mewMessageSendDto, contactId);
        //强制下线
        if(MessageTypeEnums.FORCE_OFF_LINE.getType().equals(mewMessageSendDto.getMessageType())){
           colseContext(contactId);
        }
    }

    //发送消息给群组
    public void send2Group(@SuppressWarnings("rawtypes") MessageSendDto mewMessageSendDto){
        String contactId = mewMessageSendDto.getContactId();
        if(StringUtils.isEmpty(contactId)) return;
        ChannelGroup group = GROUP_CONTEXT_MAP.get(mewMessageSendDto.getContactId());
        if(group==null) return;
        group.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(mewMessageSendDto)));
    
        //移除群组
        if(MessageTypeEnums.REMOVE_GROUP.getType().equals(mewMessageSendDto.getMessageType())||
        MessageTypeEnums.LEAVE_GROUP.getType().equals(mewMessageSendDto.getMessageType())){
            String userId = mewMessageSendDto.getSendUserId();
            redisCompent.removeUserContact(userId, mewMessageSendDto.getContactId());
            Channel channel = USER_CONTEXT_MAP.get(userId);
            if(channel==null) return;
            group.remove(channel);
        }
        //解散群组
        if(MessageTypeEnums.DISSOLUTION_GROUP.getType().equals(mewMessageSendDto.getMessageType())){
            GROUP_CONTEXT_MAP.remove(mewMessageSendDto.getContactId());
            group.close();
        }
    }

    // 发送消息
    @SuppressWarnings("unchecked")
    public void sendMsg(@SuppressWarnings("rawtypes") MessageSendDto mewMessageSendDto,String reciveId){
        if(reciveId==null || reciveId.isEmpty()) return;
        Channel sendChannel = USER_CONTEXT_MAP.get(reciveId);
        if(sendChannel==null) return;

        //相对与客户端而言，联系人就是发送人，所以转一下再发送
        if(MessageTypeEnums.ADD_FRIEEND_SELF.getType().equals(mewMessageSendDto.getMessageType())){
            UserInfo userInfo =(UserInfo)mewMessageSendDto.getExtendData();
            mewMessageSendDto.setMessageType(MessageTypeEnums.ADD_FRIEEND.getType());
            mewMessageSendDto.setContactId(userInfo.getUserId());
            mewMessageSendDto.setContactName(userInfo.getNickName());
            mewMessageSendDto.setExtendData(null);
        }else{
            mewMessageSendDto.setContactId(mewMessageSendDto.getSendUserId());
            mewMessageSendDto.setContactName(mewMessageSendDto.getSendUserNickName());
        }
        sendChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.toJson(mewMessageSendDto)));
    }
}
