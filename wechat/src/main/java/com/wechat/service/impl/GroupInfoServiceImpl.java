package com.wechat.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.wechat.entity.config.AppConfig;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.MessageSendDto;
import com.wechat.entity.dto.SysSettingDto;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.enums.AddOrRemoveGroupUserEnums;
import com.wechat.entity.enums.GroupStatusEnums;
import com.wechat.entity.enums.MessageStatusEnums;
import com.wechat.entity.enums.MessageTypeEnums;
import com.wechat.entity.enums.UserContactStatusEnums;
import com.wechat.entity.enums.UserContactTypeEnums;
import com.wechat.entity.po.ChatMeaasge;
import com.wechat.entity.po.ChatSession;
import com.wechat.entity.po.ChatSessionUser;
import com.wechat.entity.po.GroupInfo;
import com.wechat.entity.po.UserContact;
import com.wechat.entity.po.UserInfo;
import com.wechat.entity.query.ChatMeaasgeQuery;
import com.wechat.entity.query.ChatSessionQuery;
import com.wechat.entity.query.ChatSessionUserQuery;
import com.wechat.entity.query.GroupInfoQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.query.UserContactQuery;
import com.wechat.entity.query.UserInfoQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.exception.BussinessException;
import com.wechat.service.GroupInfoService;
import com.wechat.service.UserContactApplyService;
import com.wechat.utils.ToolUtils;
import com.wechat.websocket.ChannelContextUtils;
import com.wechat.websocket.MessageHandler;

import com.wechat.mappers.ChatMeaasgeMapper;
import com.wechat.mappers.ChatSessionMapper;
import com.wechat.mappers.ChatSessionUserMapper;
import com.wechat.mappers.GroupInfoMapper;
import com.wechat.mappers.UserContactMapper;
import com.wechat.mappers.UserInfoMapper;
import com.wechat.redis.RedisCompent;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: 群组信息Service实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
@Service("groupInfoService")
public class GroupInfoServiceImpl implements GroupInfoService {

    private static final Logger logger = LoggerFactory.getLogger(GroupInfoServiceImpl.class);

	@Resource
	private GroupInfoMapper<GroupInfo,GroupInfoQuery> groupInfoMapper;

    @Resource
    private UserContactMapper<UserContact,UserContactQuery>  userContactMapper;

    @Resource
    private UserContactApplyService userContactApplyService;
    
    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisCompent redisCompent;

    @Resource
    private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private ChatMeaasgeMapper<ChatMeaasge,ChatMeaasgeQuery> chatMeaasgeMapper;

    @Resource
    private ChatSessionMapper<ChatSession,ChatSessionQuery> chatSessionMapper;
    
    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private ChannelContextUtils channelContextUtils;

    @Resource
    @Lazy
    private GroupInfoService groupInfoService;


    /**
     * 根据条件查询列表
     */
	@Override
    public List<GroupInfo> findListByParam(GroupInfoQuery query){
        return this.groupInfoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(GroupInfoQuery query){
        return this.groupInfoMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<GroupInfo> findCountByPage(GroupInfoQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<GroupInfo> list = this.findListByParam(query);
		PaginationResultVO<GroupInfo> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(GroupInfo bean){
        return this.groupInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<GroupInfo> beans){
		return this.groupInfoMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<GroupInfo> beans){
		return this.groupInfoMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据GroupId查询群组信息
     */
	@Override
    public GroupInfo getByGroupId(String groupId ){
        return this.groupInfoMapper.selectByGroupId(groupId);
    }

    /**
     * 根据GroupId更新群组信息
     */
	@Override
    public Integer updateByGroupId( GroupInfo t,String groupId ){
        return this.groupInfoMapper.updateByGroupId(t,groupId);
    }

    /**
     * 根据GroupId删除群组信息
     */
	@Override
    public Integer deleteByGroupId(String groupId ){
        return this.groupInfoMapper.deleteByGroupId(groupId);
    }

    /**
     * 保存群组信息
     * @throws BussinessException 
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGroup(GroupInfo groupInfo, 
                MultipartFile avatarFile, 
                MultipartFile avatarCover) throws Exception {
            Date date = new Date();
            //新增
            UserContact userContact = new UserContact();
            if(StringUtils.isEmpty(groupInfo.getGroupId())){
                GroupInfoQuery query = new GroupInfoQuery();
                SysSettingDto  SysSettingDto = redisCompent.getSysSetting();
                query.setGroupOwnerId(groupInfo.getGroupOwnerId());
                Integer count = this.findCountByParam(query);
                if(count >= SysSettingDto.getMaxGroupMemberCount()){
                    throw new BussinessException("最多只能创建"+SysSettingDto.getMaxGroupMemberCount()+"个群组");
                }
                if (avatarFile == null) {
                    throw new BussinessException(ResponseCodeEnum.CODE_600);
                }
                groupInfo.setCreateTime(date);
                groupInfo.setGroupId(ToolUtils.getGroupId());
                this.add(groupInfo);

                //将群组添加为群主的好友
                userContact.setStatus(UserContactStatusEnums.FRIEND.getStatus());
                userContact.setContactType(UserContactTypeEnums.GROUP.getCode());
                userContact.setContactId(groupInfo.getGroupId());
                userContact.setUserId(groupInfo.getGroupOwnerId());
                userContact.setCreateTime(date);
                userContact.setLastUpdateTime(date);
                this.userContactMapper.insert(userContact);

                // 创建会话
                String sessionId = ToolUtils.getChatSessionId4Group(groupInfo.getGroupId());
                ChatSession chatSession = new ChatSession();
                chatSession.setSessionId(sessionId);
                chatSession.setLastMassage(MessageTypeEnums.GROUP_CREATE.getInitMessage());
                chatSession.setLastReceiveTime(date.getTime());
                chatSessionMapper.insert(chatSession);

                ChatSessionUser chatSessionUser = new ChatSessionUser();
                chatSessionUser.setSessionId(sessionId);
                chatSessionUser.setUserId(groupInfo.getGroupOwnerId());
                chatSessionUser.setContactId(groupInfo.getGroupId());
                chatSessionUser.setContactName(groupInfo.getGroupName());
                chatSessionUserMapper.insert(chatSessionUser);

                //创建消息
                ChatMeaasge chatMeaasge = new ChatMeaasge();
                chatMeaasge.setSessionId(sessionId);
                chatMeaasge.setMessageType(MessageTypeEnums.GROUP_CREATE.getType());
                chatMeaasge.setMessageContent(MessageTypeEnums.GROUP_CREATE.getInitMessage());
                chatMeaasge.setSendTime(date.getTime());
                chatMeaasge.setContactType(UserContactTypeEnums.GROUP.getCode());
                chatMeaasge.setContactId(groupInfo.getGroupId());
                chatMeaasge.setStatus(MessageStatusEnums.SENDED.getStatus());
                chatMeaasgeMapper.insert(chatMeaasge);

                //添加群组缓存
                redisCompent.addUserContact(groupInfo.getGroupOwnerId(), groupInfo.getGroupId());
                //添加群组成员通道
                channelContextUtils.addUser2Group(groupInfo.getGroupOwnerId(), groupInfo.getGroupId());

                
                //发送ws消息
                chatSessionUser.setLastMassage(MessageTypeEnums.GROUP_CREATE.getInitMessage());
                chatSessionUser.setLastReceiveTime(date.getTime());
                chatSessionUser.setMemberCount(1);

                @SuppressWarnings("rawtypes")
                MessageSendDto messageSendDto = ToolUtils.copy(chatMeaasge, MessageSendDto.class);
                messageSendDto.setExtendData(chatSessionUser);
                messageSendDto.setLastMessage(chatSessionUser.getLastMassage());
                messageHandler.sendMessage(messageSendDto);
            }else{
                GroupInfo dbInfo = this.getByGroupId(groupInfo.getGroupId());
                if(!groupInfo.getGroupOwnerId().equals(dbInfo.getGroupOwnerId())){
                    throw new BussinessException(ResponseCodeEnum.CODE_600);
                }
                this.updateByGroupId(groupInfo,groupInfo.getGroupId());

                //更新相关表冗余信息
                String contactNameUpdate = null;
                if(!dbInfo.getGroupName().equals(groupInfo.getGroupName())){
                    contactNameUpdate = groupInfo.getGroupName();
                }
                if(contactNameUpdate!=null){
                    ChatSessionUser updateInfo = new ChatSessionUser();
                    updateInfo.setContactName(contactNameUpdate);

                    ChatSessionUserQuery chatSessionUserQuery = new ChatSessionUserQuery();
                    chatSessionUserQuery.setContactId(groupInfo.getGroupId());
                    Integer updateCount = chatSessionUserMapper.updateByParam(updateInfo, chatSessionUserQuery);
                    logger.info("更新群组名称，更新会话数量："+updateCount);
                    //修改群昵称发送ws消息
                    @SuppressWarnings("rawtypes")
                    MessageSendDto messageSendDto = new MessageSendDto<>();
                    messageSendDto.setMessageType(MessageTypeEnums.CONTACT_NAME_UPDATE.getType());
                    messageSendDto.setContactId(groupInfo.getGroupId());
                    messageSendDto.setExtendData(contactNameUpdate);
                    messageSendDto.setContactType(UserContactTypeEnums.GROUP.getCode());
                    messageHandler.sendMessage(messageSendDto);
                }
                
            }
            if(avatarFile == null){
                return;
            }
            String baseFolder = appConfig.getProjectFolder()+ Constans.FILE_FOLDER_AVATAR_NAME;

            File folder = new File(baseFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String avatarPath = folder.getPath() +"/"+ groupInfo.getGroupId() + Constans.IMAGE_SUFFIX;
            String avatarCoverPath = folder.getPath() +"/"+ groupInfo.getGroupId() + Constans.COVER_IMAGE_SUFFIX;
           
            avatarFile.transferTo(new File(avatarPath));
            avatarCover.transferTo(new File(avatarCoverPath));

        }

    /**
     * 解散群组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dissolutionGroup(String groupOwerId,String groupId) throws Exception {
        GroupInfo groupInfo = this.getByGroupId(groupId);
        if(groupInfo == null || !groupInfo.getGroupOwnerId().equals(groupOwerId)){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        //解散群组
        groupInfo.setStatus(GroupStatusEnums.DEL.getStatus());
        this.updateByGroupId(groupInfo, groupId);
        
        UserContactQuery userContactQuery = new UserContactQuery();
        userContactQuery.setContactId(groupId);
        userContactQuery.setContactType(UserContactTypeEnums.GROUP.getCode());

        UserContact updateContact = new UserContact();
        updateContact.setStatus(UserContactStatusEnums.DEL.getStatus());
        this.userContactMapper.updateByParam(updateContact, userContactQuery);
        List<UserContact> userContacts = this.userContactMapper.selectList(userContactQuery);
        //移除相关群员的联系人缓存
        //发消息 1. 更新会话消息 2. 记录群消息 3. 发送解散消息
        for(UserContact userContact : userContacts){
            redisCompent.removeUserContact(userContact.getUserId(), groupId);
            
        }
        String sessionId = ToolUtils.getChatSessionId4Group(groupId);
        Date date = new Date();
        ChatSession chatSession = new ChatSession();
        String chatContent = MessageTypeEnums.DISSOLUTION_GROUP.getInitMessage();
        chatSession.setSessionId(sessionId);
        chatSession.setLastMassage(chatContent);
        chatSession.setLastReceiveTime(date.getTime());
        chatSessionMapper.updateBySessionId(chatSession, sessionId);

        ChatMeaasge chatMeaasge = new ChatMeaasge();
        chatMeaasge.setSessionId(sessionId);
        chatMeaasge.setMessageType(MessageTypeEnums.DISSOLUTION_GROUP.getType());
        chatMeaasge.setMessageContent(chatContent);
        chatMeaasge.setSendTime(date.getTime());
        chatMeaasge.setContactType(UserContactTypeEnums.GROUP.getCode());
        chatMeaasge.setStatus(MessageStatusEnums.SENDED.getStatus());
        chatMeaasge.setContactId(groupId);
        chatMeaasgeMapper.insert(chatMeaasge);
        //发送ws消息
        @SuppressWarnings("rawtypes")
        MessageSendDto messageSendDto = ToolUtils.copy(chatMeaasge, MessageSendDto.class);
        messageHandler.sendMessage(messageSendDto);
        
    }

    @Override
    public void addOrRemoveGroupUser(TokenUserInfo tokenUserInfo, String groupId, String selectContacts, Integer opType)
            throws Exception {
        
        GroupInfo groupInfo = this.getByGroupId(groupId);
        if(groupInfo == null|| !groupInfo.getGroupOwnerId().equals(tokenUserInfo.getUserId())){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        AddOrRemoveGroupUserEnums addOrRemoveGroupUserEnums = AddOrRemoveGroupUserEnums.getByCode(opType);
        if(addOrRemoveGroupUserEnums == null){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        String[] selectContactArr = selectContacts.split(",");
        for(String contactId:selectContactArr){
            
            if(addOrRemoveGroupUserEnums==AddOrRemoveGroupUserEnums.ADD){
                userContactApplyService.addContact(contactId, groupId, null,UserContactTypeEnums.GROUP.getCode(), null);
            }else{
                groupInfoService.leafGroup(contactId, groupId, MessageTypeEnums.REMOVE_GROUP);
            }
        }
        return ;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leafGroup(String contactId, String groupId, MessageTypeEnums messageTypeEnums) throws Exception {
        GroupInfo groupInfo = this.getByGroupId(groupId);
        if(groupInfo == null){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        if(contactId.equals(groupInfo.getGroupOwnerId())){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        Integer count = userContactMapper.deleteByUserIdAndContactId(contactId, groupId);
        if(count == 0){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        UserInfo userInfo = userInfoMapper.selectByUseId(contactId);    
        String sessionId = ToolUtils.getChatSessionId4Group(groupId);
        Date date = new Date();
        String messageContent =String.format(messageTypeEnums.getInitMessage(), userInfo.getNickName());
        ChatSession chatSession = new ChatSession();
        chatSession.setSessionId(sessionId);
        chatSession.setLastMassage(messageContent);
        chatSession.setLastReceiveTime(date.getTime());
        chatSessionMapper.updateBySessionId(chatSession, sessionId);

        ChatMeaasge chatMeaasge = new ChatMeaasge();
        chatMeaasge.setSessionId(sessionId);
        chatMeaasge.setMessageType(messageTypeEnums.getType());
        chatMeaasge.setMessageContent(messageContent);
        chatMeaasge.setSendTime(date.getTime());
        chatMeaasge.setStatus(MessageStatusEnums.SENDED.getStatus());
        chatMeaasge.setContactId(groupId);
        chatMeaasge.setContactType(UserContactTypeEnums.GROUP.getCode());
        chatMeaasgeMapper.insert(chatMeaasge);

        UserContactQuery userContactQuery = new UserContactQuery();
        userContactQuery.setContactId(groupId);
        userContactQuery.setStatus(UserContactStatusEnums.FRIEND.getStatus());
        Integer memberCount = userContactMapper.selectCount(userContactQuery);

        //发送ws消息
        MessageSendDto<ChatMeaasge> messageSendDto = ToolUtils.copy(chatMeaasge, MessageSendDto.class);
        messageSendDto.setSendUserId(contactId);
        messageSendDto.setMemberCount(memberCount);
        messageSendDto.setExtendData(chatMeaasge);
        messageHandler.sendMessage(messageSendDto);
    }
    
}
