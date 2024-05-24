package com.wechat.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wechat.entity.dto.MessageSendDto;
import com.wechat.entity.dto.SysSettingDto;
import com.wechat.entity.enums.MessageStatusEnums;
import com.wechat.entity.enums.MessageTypeEnums;
import com.wechat.entity.enums.UserContactApplyEnums;
import com.wechat.entity.enums.UserContactStatusEnums;
import com.wechat.entity.enums.UserContactTypeEnums;
import com.wechat.entity.po.ChatMeaasge;
import com.wechat.entity.po.ChatSession;
import com.wechat.entity.po.ChatSessionUser;
import com.wechat.entity.po.GroupInfo;
import com.wechat.entity.po.UserContact;
import com.wechat.entity.po.UserContactApply;
import com.wechat.entity.po.UserInfo;
import com.wechat.entity.query.UserContactApplyQuery;
import com.wechat.entity.query.UserContactQuery;
import com.wechat.entity.query.UserInfoQuery;
import com.wechat.entity.query.ChatMeaasgeQuery;
import com.wechat.entity.query.ChatSessionQuery;
import com.wechat.entity.query.ChatSessionUserQuery;
import com.wechat.entity.query.GroupInfoQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.exception.BussinessException;
import com.wechat.service.UserContactApplyService;
import com.wechat.utils.ToolUtils;
import com.wechat.websocket.ChannelContextUtils;
import com.wechat.websocket.MessageHandler;

import com.wechat.mappers.ChatMeaasgeMapper;
import com.wechat.mappers.ChatSessionMapper;
import com.wechat.mappers.ChatSessionUserMapper;
import com.wechat.mappers.GroupInfoMapper;
import com.wechat.mappers.UserContactApplyMapper;
import com.wechat.mappers.UserContactMapper;
import com.wechat.mappers.UserInfoMapper;
import com.wechat.redis.RedisCompent;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 联系人申请信息Service实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
@Service("userContactApplyService")
public class UserContactApplyServiceImpl implements UserContactApplyService {
    private static final Logger logger = LoggerFactory.getLogger(UserContactApplyServiceImpl.class);
	@Resource
	private UserContactApplyMapper<UserContactApply,UserContactApplyQuery> userContactApplyMapper;

    @Resource
    private UserContactMapper<UserContact,UserContactQuery> userContactMapper;

    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;

    @Resource
    private GroupInfoMapper<GroupInfo,GroupInfoQuery> groupInfoMapper;

    @Resource
    private RedisCompent redisCompent;

    @Resource
    private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private ChatMeaasgeMapper<ChatMeaasge,ChatMeaasgeQuery> chatMeaasgeMapper;

    @Resource
    private ChatSessionMapper<ChatSession,ChatSessionQuery> chatSessionMapper;
    

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private ChannelContextUtils channelContextUtils;

    

    /**
     * 根据条件查询列表
     */
	@Override
    public List<UserContactApply> findListByParam(UserContactApplyQuery query){
        if (query.getQueryContactInfo()==true){
            return this.userContactApplyMapper.selectJoinQuery(query);
        }
        return this.userContactApplyMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(UserContactApplyQuery query){
        return this.userContactApplyMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<UserContactApply> findCountByPage(UserContactApplyQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<UserContactApply> list = this.findListByParam(query);
		PaginationResultVO<UserContactApply> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(UserContactApply bean){
        return this.userContactApplyMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<UserContactApply> beans){
		return this.userContactApplyMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<UserContactApply> beans){
		return this.userContactApplyMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据ApplyId查询联系人申请信息
     */
	@Override
    public UserContactApply getByApplyId(Integer applyId ){
        return this.userContactApplyMapper.selectByApplyId(applyId);
    }

    /**
     * 根据ApplyId更新联系人申请信息
     */
	@Override
    public Integer updateByApplyId( UserContactApply t,Integer applyId ){
        return this.userContactApplyMapper.updateByApplyId(t,applyId);
    }

    /**
     * 根据ApplyId删除联系人申请信息
     */
	@Override
    public Integer deleteByApplyId(Integer applyId ){
        return this.userContactApplyMapper.deleteByApplyId(applyId);
    }

    
    /**
     * 根据ApplyUserIdAndContactId查询联系人申请信息
     */
	@Override
    public UserContactApply getByApplyUserIdAndContactId(String applyUserId,String contactId ){
        return this.userContactApplyMapper.selectByApplyUserIdAndContactId(applyUserId,contactId);
    }

    /**
     * 根据ApplyUserIdAndContactId更新联系人申请信息
     */
	@Override
    public Integer updateByApplyUserIdAndContactId( UserContactApply t,String applyUserId,String contactId ){
        return this.userContactApplyMapper.updateByApplyUserIdAndContactId(t,applyUserId,contactId);
    }

    /**
     * 根据ApplyUserIdAndContactId删除联系人申请信息
     */
	@Override
    public Integer deleteByApplyUserIdAndContactId(String applyUserId,String contactId ){
        return this.userContactApplyMapper.deleteByApplyUserIdAndContactId(applyUserId,contactId);
    }

    /**
     * 处理申请
     * @throws BussinessException 
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealWithApply(String userId, Integer applyId, Integer status) throws Exception {
        UserContactApplyEnums userContactApplyEnums = UserContactApplyEnums.getEnumByStatus(status);

        if(userContactApplyEnums == null || UserContactApplyEnums.INIT==userContactApplyEnums){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        
        UserContactApply updateInfo = this.getByApplyId(applyId);

        if(updateInfo == null ||!userId.equals(updateInfo.getReceiveUserId())){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }

        updateInfo.setStatus(status);
        updateInfo.setLastApplyTime(System.currentTimeMillis());

        UserContactApplyQuery query = new UserContactApplyQuery();
        query.setApplyId(applyId);
        query.setStatus(UserContactApplyEnums.INIT.getStatus());

        Integer count = userContactApplyMapper.selectCount(query);
        if(count == 0){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        userContactApplyMapper.updateByApplyId(updateInfo,applyId);
        if(UserContactApplyEnums.PASS==userContactApplyEnums){
            this.addContact(updateInfo.getApplyUserId(), updateInfo.getContactId(), updateInfo.getReceiveUserId(),updateInfo.getContactType(),updateInfo.getApplyInfo());
            return;
        }

        if(UserContactApplyEnums.BLACKLIST==userContactApplyEnums){
            Date curTime = new Date();
            UserContact userContact = new UserContact();
            userContact.setUserId(updateInfo.getApplyUserId());
            userContact.setContactId(updateInfo.getReceiveUserId());
            userContact.setContactType(updateInfo.getContactType());
            userContact.setLastUpdateTime(curTime);
            userContact.setCreateTime(curTime);
            userContact.setStatus(UserContactStatusEnums.BLACKLIST_BE.getStatus());
            userContactMapper.insertOrUpdate(userContact);
        }
        
    }

    /**
     * 添加联系人申请信息
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addContact(String applyUserId, String contactId, String receiveUserId,Integer contactType,String applyInfo)
            throws Exception {
        if(UserContactTypeEnums.GROUP.getCode().equals(contactType)){
            UserContactQuery userContactQuery = new UserContactQuery();
            userContactQuery.setContactId(contactId);
            userContactQuery.setStatus(UserContactStatusEnums.FRIEND.getStatus());
            Integer count = userContactMapper.selectCount(userContactQuery);
            SysSettingDto sysSettingDto = redisCompent.getSysSetting();
            if(count>=sysSettingDto.getMaxGroupCount()){
                throw new BussinessException("群组好友数量已达上限");
            }
        }
        Date curTime = new Date();

        //同意，双方添加好友
        List<UserContact> userContactList = new ArrayList<>();
        //申请人添加对方
        UserContact userContact = new UserContact();
        userContact.setUserId(applyUserId);
        userContact.setContactId(contactId);
        userContact.setContactType(contactType);
        userContact.setLastUpdateTime(curTime);
        userContact.setCreateTime(curTime);
        userContact.setStatus(UserContactStatusEnums.FRIEND.getStatus());
        userContactList.add(userContact);

        //对方添加申请人,群组不用添加对方好友
        if(UserContactTypeEnums.USER.getCode().equals(contactType)){
            UserContact receiveUserContact = new UserContact();
            receiveUserContact.setUserId(contactId);
            receiveUserContact.setContactId(applyUserId);
            receiveUserContact.setContactType(contactType);
            receiveUserContact.setLastUpdateTime(curTime);
            receiveUserContact.setCreateTime(curTime);
            receiveUserContact.setStatus(UserContactStatusEnums.FRIEND.getStatus());
            userContactList.add(receiveUserContact);
        }
        //批量添加
        Integer count = userContactMapper.insertOrUpdateBatch(userContactList);
        logger.info("addContact count:{}",count);
        //如果是好友，接收人也添加申请人为好友 添加缓存
        if(UserContactTypeEnums.USER.getCode().equals(contactType)){
            redisCompent.addUserContact(contactId, applyUserId);
        }
        redisCompent.addUserContact(applyUserId, contactId);

        //创建会话
        String sessionId = null;
        if(UserContactTypeEnums.USER.getCode().equals(contactType)){
            sessionId = ToolUtils.getChatSessionId4User(applyUserId, contactId);
        }else{
            sessionId = ToolUtils.getChatSessionId4Group(contactId);
        }
        List<ChatSessionUser> chatSessionUserList = new ArrayList<>();
        if(UserContactTypeEnums.USER.getCode().equals(contactType)){
            ChatSession chatSession = new ChatSession();
            chatSession.setSessionId(sessionId);
            chatSession.setLastMassage(applyInfo);
            chatSession.setLastReceiveTime(curTime.getTime());
            chatSessionMapper.insertOrUpdate(chatSession);
            
            //申请人session
            ChatSessionUser applySessionUser = new ChatSessionUser();
            applySessionUser.setSessionId(sessionId);
            applySessionUser.setUserId(applyUserId);
            applySessionUser.setContactId(receiveUserId);
            applySessionUser.setLastReceiveTime(curTime.getTime());
            applySessionUser.setLastMassage(applyInfo);
            UserInfo contactUserInfo = userInfoMapper.selectByUseId(receiveUserId);
            applySessionUser.setContactName(contactUserInfo.getNickName());
            chatSessionUserList.add(applySessionUser);

            //接收人session
            ChatSessionUser receiveSessionUser = new ChatSessionUser();
            receiveSessionUser.setSessionId(sessionId);
            receiveSessionUser.setUserId(contactId);
            receiveSessionUser.setContactId(applyUserId);
            // receiveSessionUser.setLastReceiveTime(curTime.getTime());
            // receiveSessionUser.setLastMassage(applyInfo);
            UserInfo applyUserInfo = userInfoMapper.selectByUseId(applyUserId);
            receiveSessionUser.setContactName(applyUserInfo.getNickName());
            chatSessionUserList.add(receiveSessionUser);

            chatSessionUserMapper.insertOrUpdateBatch(chatSessionUserList);

            //记录消息表
            ChatMeaasge chatMeaasge = new ChatMeaasge();
            chatMeaasge.setSessionId(sessionId);
            chatMeaasge.setMessageType(MessageTypeEnums.ADD_FRIEEND.getType());
            chatMeaasge.setMessageContent(applyInfo);
            chatMeaasge.setSendUserId(applyUserId);
            chatMeaasge.setSendUserNickName(applyUserInfo.getNickName());
            chatMeaasge.setSendTime(curTime.getTime());
            chatMeaasge.setContactId(contactId);
            chatMeaasge.setContactType(UserContactTypeEnums.USER.getCode());
            chatMeaasgeMapper.insert(chatMeaasge);

            //发送消息
            @SuppressWarnings("rawtypes")
            MessageSendDto messageSendDto = ToolUtils.copy(chatMeaasge, MessageSendDto.class);
            //发送给接受还有申请的人
            messageHandler.sendMessage(messageSendDto);
            
            //发送给申请人，发送人就是联系人,联系人就是申请人
            messageSendDto.setMessageType(MessageTypeEnums.ADD_FRIEEND_SELF.getType());
            messageSendDto.setSendUserId(contactId);
            messageSendDto.setExtendData(contactUserInfo);
            messageHandler.sendMessage(messageSendDto);
        }else{
            //加入群组
            ChatSessionUser chatSessionUser = new ChatSessionUser();
            chatSessionUser.setUserId(applyUserId);
            chatSessionUser.setContactId(contactId);
            GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
            chatSessionUser.setContactName(groupInfo.getGroupName());
            chatSessionUser.setSessionId(sessionId);
            chatSessionUserMapper.insertOrUpdate(chatSessionUser);

            UserInfo applyUserInfo = userInfoMapper.selectByUseId(applyUserId);
            String sendMessage =String.format(MessageTypeEnums.ADD_GROUP.getInitMessage(), applyUserInfo.getNickName()); 
            //添加session信息
            ChatSession chatSession = new ChatSession();
            chatSession.setSessionId(sessionId);
            chatSession.setLastMassage(sendMessage);
            chatSession.setLastReceiveTime(curTime.getTime());
            chatSessionMapper.insertOrUpdate(chatSession);
            //添加聊天消息
            ChatMeaasge chatMeaasge = new ChatMeaasge();
            chatMeaasge.setSessionId(sessionId);
            chatMeaasge.setMessageType(MessageTypeEnums.ADD_GROUP.getType());
            chatMeaasge.setMessageContent(sendMessage);
            chatMeaasge.setSendTime(curTime.getTime());
            chatMeaasge.setContactId(contactId);
            chatMeaasge.setContactType(UserContactTypeEnums.GROUP.getCode());
            chatMeaasge.setStatus(MessageStatusEnums.SENDED.getStatus());
            chatMeaasgeMapper.insert(chatMeaasge);

            //添加群组缓存
            redisCompent.addUserContact(applyUserId, groupInfo.getGroupId());
            //添加群组成员通道
            channelContextUtils.addUser2Group(applyUserId, groupInfo.getGroupId());
            
            //发送ws消息
            @SuppressWarnings("rawtypes")
            MessageSendDto messageSendDto = ToolUtils.copy(chatMeaasge, MessageSendDto.class);
            messageSendDto.setContactId(contactId);
            //获取群组数量
            UserContactQuery userContactQuery = new UserContactQuery();
            userContactQuery.setContactId(contactId);
            userContactQuery.setStatus(UserContactStatusEnums.FRIEND.getStatus());
            Integer memberCount = userContactMapper.selectCount(userContactQuery);
            messageSendDto.setMemberCount(memberCount);
            messageSendDto.setContactName(groupInfo.getGroupName());
            //发送消息
            messageHandler.sendMessage(messageSendDto);
        }
    }

}
