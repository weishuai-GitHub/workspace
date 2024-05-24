package com.wechat.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.wechat.entity.config.AppConfig;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.MessageSendDto;
import com.wechat.entity.dto.SysSettingDto;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.enums.MessageStatusEnums;
import com.wechat.entity.enums.MessageTypeEnums;
import com.wechat.entity.enums.UserContactStatusEnums;
import com.wechat.entity.enums.UserContactTypeEnums;
import com.wechat.entity.po.ChatMeaasge;
import com.wechat.entity.po.ChatSession;
import com.wechat.entity.po.UserContact;
import com.wechat.entity.query.ChatMeaasgeQuery;
import com.wechat.entity.query.ChatSessionQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.query.UserContactQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.exception.BussinessException;
import com.wechat.service.ChatMeaasgeService;
import com.wechat.utils.DateUtils;
import com.wechat.utils.ToolUtils;
import com.wechat.websocket.MessageHandler;

import com.wechat.mappers.ChatMeaasgeMapper;
import com.wechat.mappers.ChatSessionMapper;
import com.wechat.mappers.UserContactMapper;
import com.wechat.redis.RedisCompent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: 聊天消息表Service实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
@Service("chatMeaasgeService")
public class ChatMeaasgeServiceImpl implements ChatMeaasgeService {

    private static final Logger logger = LoggerFactory.getLogger(ChatMeaasgeServiceImpl.class);

    @Resource
    private UserContactMapper<UserContact,UserContactQuery> userContactMapper;

	@Resource
	private ChatMeaasgeMapper<ChatMeaasge,ChatMeaasgeQuery> chatMeaasgeMapper;

    @Resource
    private ChatSessionMapper<ChatSession,ChatSessionQuery> chatSessionMapper;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private RedisCompent redisCompent;

    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
	@Override
    public List<ChatMeaasge> findListByParam(ChatMeaasgeQuery query){
        return this.chatMeaasgeMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(ChatMeaasgeQuery query){
        return this.chatMeaasgeMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<ChatMeaasge> findCountByPage(ChatMeaasgeQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<ChatMeaasge> list = this.findListByParam(query);
		PaginationResultVO<ChatMeaasge> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(ChatMeaasge bean){
        return this.chatMeaasgeMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<ChatMeaasge> beans){
		return this.chatMeaasgeMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<ChatMeaasge> beans){
		return this.chatMeaasgeMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据MessageId查询聊天消息表
     */
	@Override
    public ChatMeaasge getByMessageId(Long messageId ){
        return this.chatMeaasgeMapper.selectByMessageId(messageId);
    }

    /**
     * 根据MessageId更新聊天消息表
     */
	@Override
    public Integer updateByMessageId( ChatMeaasge t,Long messageId ){
        return this.chatMeaasgeMapper.updateByMessageId(t,messageId);
    }

    /**
     * 根据MessageId删除聊天消息表
     */
	@Override
    public Integer deleteByMessageId(Long messageId ){
        return this.chatMeaasgeMapper.deleteByMessageId(messageId);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public MessageSendDto saveMessage(ChatMeaasge chatMeaasge, TokenUserInfo tokenUserInfo) throws Exception {
        //不是机器人回复，判断好友状态
        if(!Constans.ROBOT_UID.equals(tokenUserInfo.getUserId())){
            List<String> friendList = redisCompent.getUserContactBatch(tokenUserInfo.getUserId());
            if(!friendList.contains(chatMeaasge.getContactId())){
                UserContactTypeEnums userContactTypeEnums = UserContactTypeEnums.getEnumByPrefix(chatMeaasge.getContactId().substring(0,1));
                if(userContactTypeEnums == UserContactTypeEnums.USER){
                    throw new BussinessException(ResponseCodeEnum.CODE_902);
                }else if(userContactTypeEnums == UserContactTypeEnums.GROUP){
                    throw new BussinessException(ResponseCodeEnum.CODE_903);
                }else{
                    throw new BussinessException(ResponseCodeEnum.CODE_600);
                }
            }
        }
        String sessionId = null;
        String sendUserId = tokenUserInfo.getUserId();
        String contactId = chatMeaasge.getContactId();
        UserContactTypeEnums userContactTypeEnums = UserContactTypeEnums.getEnumByPrefix(contactId.substring(0,1));
        if(UserContactTypeEnums.USER == userContactTypeEnums){
            sessionId = ToolUtils.getChatSessionId4User(sendUserId, contactId);
        }
        else sessionId = ToolUtils.getChatSessionId4Group(contactId);
        Long curTime = System.currentTimeMillis();
        chatMeaasge.setSendTime(curTime);
        MessageTypeEnums messageTypeEnums = MessageTypeEnums.getByType(chatMeaasge.getMessageType());
        if(messageTypeEnums == null||!ArrayUtils.contains(new Integer[]{MessageTypeEnums.CHAT.getType(),
        MessageTypeEnums.MEDIA_CHAT.getType()}, messageTypeEnums.getType())){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        Integer stutus = MessageTypeEnums.MEDIA_CHAT==messageTypeEnums? MessageStatusEnums.SENDING.getStatus():MessageStatusEnums.SENDED.getStatus();
        chatMeaasge.setStatus(stutus);
        String messageContent = ToolUtils.cleanHtmlTag(chatMeaasge.getMessageContent());
        chatMeaasge.setMessageContent(messageContent);

        // 更新会话
        ChatSession chatSession = new ChatSession();
        chatSession.setLastMassage(messageContent);
        chatSession.setLastReceiveTime(curTime);
        if(UserContactTypeEnums.GROUP==userContactTypeEnums){
            chatSession.setLastMassage(tokenUserInfo.getNickName()+":"+ messageContent);
        }
        chatSessionMapper.updateBySessionId(chatSession, sessionId);

        // 记录消息表
        chatMeaasge.setSendUserId(sendUserId);
        chatMeaasge.setSendUserNickName(tokenUserInfo.getNickName());
        chatMeaasge.setSessionId(sessionId);
        chatMeaasge.setContactType(userContactTypeEnums.getCode());
        chatMeaasgeMapper.insert(chatMeaasge);

        //发送消息
        MessageSendDto messageSendDto = ToolUtils.copy(chatMeaasge, MessageSendDto.class);
        if(Constans.ROBOT_UID.equals(chatMeaasge.getContactId())){
            SysSettingDto sysSettingDto = redisCompent.getSysSetting();
            TokenUserInfo robot = new TokenUserInfo();
            robot.setUserId(sysSettingDto.getRobotUid());
            robot.setNickName(sysSettingDto.getRobotNickName());
            ChatMeaasge robotChatMeaasge = new ChatMeaasge();
            robotChatMeaasge.setContactId(sendUserId);
            //TODO 机器人回复
            robotChatMeaasge.setMessageContent("我是机器人,目前还在学习中,暂时无法回复您的消息");
            robotChatMeaasge.setMessageType(MessageTypeEnums.CHAT.getType());
            saveMessage(robotChatMeaasge, robot);
            //如果发送的是媒体消息，需要等待发送完成
            // if(stutus == MessageStatusEnums.SENDING.getStatus()){
            //     saveMessageFile(tokenUserInfo.getUserId(), chatMeaasge.getMessageId(), null, null);
            // }
        }
        else messageHandler.sendMessage(messageSendDto);
        return messageSendDto;
    }

    @Override
    public void saveMessageFile(String userId,Long messageId, MultipartFile file,
            MultipartFile cover) throws Exception {
        ChatMeaasge chatMeaasge = chatMeaasgeMapper.selectByMessageId(messageId);
        if(chatMeaasge == null){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        if(!chatMeaasge.getSendUserId().equals(userId)){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }

        SysSettingDto sysSettingDto = redisCompent.getSysSetting();
        String fileSuffix = ToolUtils.getFileSuffix(file.getOriginalFilename());
        if(StringUtils.isEmpty(fileSuffix)){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        if(ArrayUtils.contains(Constans.IMAGE_SUFFIX_LIST, fileSuffix.toLowerCase()) &&
            file.getSize()>sysSettingDto.getMaxImageSize()*Constans.FILE_ZISE_ME){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }else if(ArrayUtils.contains(Constans.VIDEO_SUFFIX_LIST, fileSuffix.toLowerCase())&&
            file.getSize()>sysSettingDto.getMaxVideoSize()*Constans.FILE_ZISE_ME){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }else if(file.getSize()>sysSettingDto.getMaxFileSize()*Constans.FILE_ZISE_ME){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }

        String fileName = file.getOriginalFilename();
        String fileExtName = ToolUtils.getFileSuffix(fileName);
        String fileRealName = messageId + fileExtName;
        String month = DateUtils.format(new Date(chatMeaasge.getSendTime()), DateUtils.YYYYMM);
        File folder  = new File(appConfig.getProjectFolder() + Constans.FILE_FOLDER_FILE + month);
        if(!folder.exists()){
            folder.mkdirs();
        }
        File fileSave = new File(folder, fileRealName);
        try {
            file.transferTo(fileSave);
            cover.transferTo(new File(fileSave.getPath() + Constans.COVER_IMAGE_SUFFIX));
        } catch (Exception e) {
            logger.error("文件保存失败", e);
            throw new BussinessException("文件保存失败");
        }

        ChatMeaasge upadateInfo = new ChatMeaasge();
        upadateInfo.setStatus(MessageStatusEnums.SENDED.getStatus());
        ChatMeaasgeQuery query = new ChatMeaasgeQuery();
        query.setMessageId(messageId);
        query.setStatus(MessageStatusEnums.SENDING.getStatus());
        chatMeaasgeMapper.updateByParam(upadateInfo, query);

        @SuppressWarnings("rawtypes")
        MessageSendDto messageSendDto = new MessageSendDto<>();
        messageSendDto.setMessageId(messageId);
        messageSendDto.setSendTime(chatMeaasge.getSendTime());
        messageSendDto.setStatus(MessageStatusEnums.SENDED.getStatus());
        messageSendDto.setMessageType(MessageTypeEnums.FILE_UPLOAD.getType());
        messageSendDto.setContactId(chatMeaasge.getContactId());
        messageHandler.sendMessage(messageSendDto);
    }

    @Override
    public File downloadFile(TokenUserInfo tokenUserInfo, long messageId,Boolean showCover) throws Exception {
        ChatMeaasge chatMeaasge = chatMeaasgeMapper.selectByMessageId(messageId);
        String contactId = chatMeaasge.getContactId();
        UserContactTypeEnums userContactTypeEnums = UserContactTypeEnums.getEnumByPrefix(contactId.substring(0,1));
        if(UserContactTypeEnums.USER == userContactTypeEnums && !tokenUserInfo.getUserId().equals(contactId)){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        if(UserContactTypeEnums.GROUP == userContactTypeEnums){
            UserContactQuery userContactQuery = new UserContactQuery();
            userContactQuery.setUserId(tokenUserInfo.getUserId());
            userContactQuery.setContactType(userContactTypeEnums.getCode());
            userContactQuery.setContactId(contactId);
            userContactQuery.setStatus(UserContactStatusEnums.FRIEND.getStatus());
            Integer count = userContactMapper.selectCount(userContactQuery);
            if(count == 0){
                throw new BussinessException(ResponseCodeEnum.CODE_600);
            }
        }
        String month = DateUtils.format(new Date(chatMeaasge.getSendTime()), DateUtils.YYYYMM);
        File folder  = new File(appConfig.getProjectFolder() + Constans.FILE_FOLDER_FILE + month);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileName = chatMeaasge.getFileName();
        String fileExtName = ToolUtils.getFileSuffix(fileName);
        String fileRealName = messageId + fileExtName;
        if(showCover!=null && showCover){
            fileRealName = fileRealName + Constans.COVER_IMAGE_SUFFIX;
        }
        File file = new File(folder, fileRealName);
        if(!file.exists()){
            logger.error("文件不存在,{}",file.getPath());
            throw new BussinessException(ResponseCodeEnum.CODE_602);
        }
        return file;
    }

}
