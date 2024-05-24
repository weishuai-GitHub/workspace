package com.wechat.service.impl;

import java.util.Date;
import java.util.List;

import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.MessageSendDto;
import com.wechat.entity.dto.SysSettingDto;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.dto.UserContactSearchResultDto;
import com.wechat.entity.enums.JoinTypeEnums;
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
import com.wechat.entity.query.UserContactQuery;
import com.wechat.entity.query.UserInfoQuery;
import com.wechat.entity.query.ChatSessionQuery;
import com.wechat.entity.query.ChatSessionUserQuery;
import com.wechat.entity.query.GroupInfoQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.query.UserContactApplyQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.exception.BussinessException;
import com.wechat.service.UserContactApplyService;
import com.wechat.service.UserContactService;
import com.wechat.utils.ToolUtils;
import com.wechat.websocket.MessageHandler;
import com.wechat.mappers.ChatMeaasgeMapper;
import com.wechat.mappers.ChatSessionMapper;
import com.wechat.mappers.ChatSessionUserMapper;
import com.wechat.mappers.GroupInfoMapper;
import com.wechat.mappers.UserContactApplyMapper;
import com.wechat.mappers.UserContactMapper;
import com.wechat.mappers.UserInfoMapper;
import com.wechat.redis.RedisCompent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 联系人信息Service实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
@Service("userContactService")
public class UserContactServiceImpl implements UserContactService {

	@Resource
	private UserContactMapper<UserContact,UserContactQuery> userContactMapper;

    @Resource
    private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;

    @Resource
    private GroupInfoMapper<GroupInfo,GroupInfoQuery> groupInfoMapper;

    @Resource
    private UserContactApplyMapper<UserContactApply,UserContactApplyQuery> userContactApplyMapper;

    @Resource
    private UserContactApplyService userContactApplyService;

    @Resource
    private RedisCompent redisCompent;

    @Resource
    private ChatSessionMapper<ChatSession,ChatSessionQuery> chatSessionMapper;

    @Resource
    private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private ChatMeaasgeMapper<ChatMeaasge,ChatSessionQuery> chatMeaasgeMapper;

    @Resource
    private MessageHandler messageHandler;

    /**
     * 根据条件查询列表
     */
	@Override
    public List<UserContact> findListByParam(UserContactQuery query){
        return this.userContactMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(UserContactQuery query){
        return this.userContactMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<UserContact> findCountByPage(UserContactQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<UserContact> list = this.findListByParam(query);
		PaginationResultVO<UserContact> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(UserContact bean){
        return this.userContactMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<UserContact> beans){
		return this.userContactMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<UserContact> beans){
		return this.userContactMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据UserIdAndContactId查询联系人信息
     */
	@Override
    public UserContact getByUserIdAndContactId(String userId,String contactId ){
        return this.userContactMapper.selectByUserIdAndContactId(userId,contactId);
    }

    /**
     * 根据UserIdAndContactId更新联系人信息
     */
	@Override
    public Integer updateByUserIdAndContactId( UserContact t,String userId,String contactId ){
        return this.userContactMapper.updateByUserIdAndContactId(t,userId,contactId);
    }

    /**
     * 根据UserIdAndContactId删除联系人信息
     */
	@Override
    public Integer deleteByUserIdAndContactId(String userId,String contactId ){
        return this.userContactMapper.deleteByUserIdAndContactId(userId,contactId);
    }

    /**
     * 根据UserId查询联系人信息
     */
    @Override
    public UserContactSearchResultDto searchContact(String userId, String contactId){
        UserContactTypeEnums userContactTypeEnums = UserContactTypeEnums.getEnumByPrefix(contactId.substring(0,1 ));
        if(userContactTypeEnums == null){
            return null;
        }

        UserContactSearchResultDto userContactSearchResultDto = new UserContactSearchResultDto();

        switch (userContactTypeEnums) {
            case USER:
                UserInfo userInfo = this.userInfoMapper.selectByUseId(contactId);
                if(userInfo == null){
                    return null;
                }
                userContactSearchResultDto  = ToolUtils.copy(userInfo, UserContactSearchResultDto.class);
                break;
        
            case GROUP:
                GroupInfo groupInfo = this.groupInfoMapper.selectByGroupId(contactId);
                if(groupInfo == null){
                    return null;
                }
                userContactSearchResultDto.setNickName(groupInfo.getGroupName());
                break;
        }
        userContactSearchResultDto.setContactType(userContactTypeEnums.toString());
        userContactSearchResultDto.setContactId(contactId);
        if(userId.equals(userContactSearchResultDto.getContactId())){
            userContactSearchResultDto.setStatus(UserContactStatusEnums.FRIEND.getStatus());
            return userContactSearchResultDto;
        }

        //查询是否是好友
        UserContact userContact = this.getByUserIdAndContactId(userId,contactId);
        userContactSearchResultDto.setStatus(userContact==null? UserContactStatusEnums.NOT_FRIEDND.getStatus():userContact.getStatus());
        return userContactSearchResultDto;
    }   

    /**
     * 申请添加联系人
     * @throws BussinessException 
     */
    @SuppressWarnings("null")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer applyAdd(TokenUserInfo tokenUserInfo, String contactId, String applyInfo) throws Exception
    {
        UserContactTypeEnums userContactTypeEnums = UserContactTypeEnums.getEnumByPrefix(contactId.substring(0,1 ));
        if(userContactTypeEnums==null){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        String appayUserId = tokenUserInfo.getUserId();

        //默认申请信息
        if(StringUtils.isEmpty(applyInfo)){
            applyInfo = String.format(Constans.APPLY_INFO_TEMPLATE, tokenUserInfo.getNickName());
        }
        Long curTime = System.currentTimeMillis();

        Integer joinType = null;
        String receiveUseid = contactId;
        UserContact userContact = userContactMapper.selectByUserIdAndContactId(appayUserId, contactId);

        //判断是否已经被拉黑
        if(userContact!=null && UserContactStatusEnums.BLACKLIST_BE.getStatus().equals(userContact.getStatus())){
            throw new BussinessException("对方已将你拉黑");
        }
        if(UserContactTypeEnums.GROUP==userContactTypeEnums){
            GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
            if(groupInfo!=null && UserContactStatusEnums.DEL.getStatus().equals(groupInfo.getStatus())){
                throw new BussinessException("群组不存在或已解散");
            }
            // if(UserContactStatusEnums.FRIEND.getStatus().equals(userContact.getStatus())){
            //     throw new BussinessException("你已经是该群成员");
            // }
            joinType = groupInfo.getJoinType();
            receiveUseid = groupInfo.getGroupOwnerId();
        }
        else{
            UserInfo userInfo = userInfoMapper.selectByUseId(contactId);
            if(userInfo!=null && UserContactStatusEnums.DEL.getStatus().equals(userInfo.getStatus())){
                throw new BussinessException(ResponseCodeEnum.CODE_600);
            }
            // if(UserContactStatusEnums.FRIEND.getStatus().equals(userContact.getStatus())){
            //     throw new BussinessException("你已经是该用户好友");
            // }
            joinType = userInfo.getJoinType();
        }

        //直接加入不用申请记录
        if(JoinTypeEnums.JOIN.getType().equals(joinType)){
            userContactApplyService.addContact(appayUserId,contactId,receiveUseid, userContactTypeEnums.getCode(),applyInfo);
            return joinType;
        }
        UserContactApply userContactApply = userContactApplyMapper.selectByApplyUserIdAndContactId(appayUserId, contactId);
        if(userContactApply==null){
            UserContactApply apply = new UserContactApply();
            apply.setApplyUserId(appayUserId);
            apply.setReceiveUserId(receiveUseid);
            apply.setContactId(contactId);
            apply.setApplyInfo(applyInfo);
            apply.setLastApplyTime(curTime);
            apply.setContactType(userContactTypeEnums.getCode());
            apply.setStatus(UserContactApplyEnums.INIT.getStatus());
            userContactApplyMapper.insert(apply);
        }else{
            userContactApply.setApplyInfo(applyInfo);
            userContactApply.setLastApplyTime(curTime);
            userContactApply.setStatus(UserContactApplyEnums.INIT.getStatus());
            userContactApplyMapper.updateByApplyId(userContactApply, userContactApply.getApplyId());
        }

        if(userContactApply==null ||!UserContactApplyEnums.INIT.getStatus().equals(userContactApply.getStatus())){
            //发送ws消息
            @SuppressWarnings("rawtypes")
            MessageSendDto messageSendDto = new MessageSendDto<>();
            messageSendDto.setMessageType(MessageTypeEnums.CONTACT_APPLY.getType());
            messageSendDto.setMessageContent(applyInfo);
            messageSendDto.setContactId(receiveUseid);
            messageHandler.sendMessage(messageSendDto);
        }
        return joinType;
    }


    /**
     * 删除联系人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteContact(String userId, String contactId, UserContactStatusEnums statusEnums) {
        Date curTime = new Date();
        //移除好友
        UserContact userContact = this.getByUserIdAndContactId(userId, contactId);
        if(userContact==null){
            return 0;
        }
        userContact.setStatus(statusEnums.getStatus());
        
        userContact.setLastUpdateTime(curTime);
        userContactMapper.updateByUserIdAndContactId(userContact, userId, contactId);

        //移除对方好友
        UserContact userContact2 = this.getByUserIdAndContactId(contactId, userId);
        if(userContact2==null){
            return 0;
        }
        if(UserContactStatusEnums.DEL==statusEnums){
            userContact2.setStatus(UserContactStatusEnums.DEL_BE.getStatus());
        
        }else if(UserContactStatusEnums.BLACKLIST==statusEnums){
            userContact2.setStatus(UserContactStatusEnums.BLACKLIST_BE.getStatus());
        }
        
        userContact2.setLastUpdateTime(curTime);
        userContactMapper.updateByUserIdAndContactId(userContact2, contactId, userId);

        //从我的好友列表删除好友
        redisCompent.removeUserContact(contactId, userId);
        //从对方好友列表删除好友
        redisCompent.removeUserContact(userId, contactId);
        return 1;
        
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addContact4Robot(String userId) {
        Date cuData  = new Date();

        SysSettingDto sysSettingDto = redisCompent.getSysSetting();
        if(sysSettingDto==null || sysSettingDto.getRobotUid()==null){
            return;
        }
        String contactId = sysSettingDto.getRobotUid();
        String contatcName = sysSettingDto.getRobotNickName();
        String sendMessage = sysSettingDto.getRobotWelcome();
        sendMessage = ToolUtils.cleanHtmlTag(sendMessage);

        //添加机器人为好友
        UserContact userContact = new UserContact();
        userContact.setUserId(userId);
        userContact.setContactId(contactId);
        userContact.setContactType(UserContactTypeEnums.USER.getCode());
        userContact.setCreateTime(cuData);
        userContact.setLastUpdateTime(cuData);
        userContact.setStatus(UserContactStatusEnums.FRIEND.getStatus());
        userContactMapper.insert(userContact);

        //增加会话信息
        String sessionId = ToolUtils.getChatSessionId4User(userId,contactId);
        ChatSession chatSession = new ChatSession();
        chatSession.setSessionId(sessionId);
        chatSession.setLastMassage(sendMessage);
        chatSession.setLastReceiveTime(cuData.getTime());
        this.chatSessionMapper.insert(chatSession);

        //增加会话人员信息
        ChatSessionUser chatSessionUser = new ChatSessionUser();
        chatSessionUser.setSessionId(sessionId);
        chatSessionUser.setUserId(userId);
        chatSessionUser.setContactId(contactId);    
        chatSessionUser.setContactName(contatcName);
        this.chatSessionUserMapper.insert(chatSessionUser);

        //增加聊天消息
        ChatMeaasge chatMeaasge = new ChatMeaasge();
        chatMeaasge.setSessionId(sessionId);
        chatMeaasge.setMessageType(MessageTypeEnums.CHAT.getType());
        chatMeaasge.setSendUserId(contactId);
        chatMeaasge.setMessageContent(sendMessage);
        chatMeaasge.setSendUserNickName(contatcName);
        chatMeaasge.setSendTime(cuData.getTime());
        chatMeaasge.setContactId(userId);
        chatMeaasge.setContactType(UserContactTypeEnums.USER.getCode());
        chatMeaasge.setStatus(MessageStatusEnums.SENDED.getStatus());
        chatMeaasgeMapper.insert(chatMeaasge);
    }

}
