package com.wechat.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.wechat.entity.config.AppConfig;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.MessageSendDto;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.enums.BeautyAccountEnums;
import com.wechat.entity.enums.JoinTypeEnums;
import com.wechat.entity.enums.MessageTypeEnums;
import com.wechat.entity.enums.UserContactStatusEnums;
import com.wechat.entity.enums.UserContactTypeEnums;
import com.wechat.entity.enums.UserStatusEnums;
import com.wechat.entity.po.ChatSessionUser;
import com.wechat.entity.po.UserContact;
import com.wechat.entity.po.UserInfo;
import com.wechat.entity.po.UserInfoBeauty;
import com.wechat.entity.query.UserInfoQuery;
import com.wechat.entity.query.ChatSessionUserQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.query.UserContactQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.entity.vo.UserInfoVO;
import com.wechat.exception.BussinessException;
import com.wechat.service.UserContactService;
import com.wechat.service.UserInfoService;
import com.wechat.utils.ToolUtils;
import com.wechat.websocket.MessageHandler;

import jakarta.validation.constraints.NotEmpty;

import com.wechat.mappers.ChatSessionUserMapper;
import com.wechat.mappers.UserContactMapper;
import com.wechat.mappers.UserInfoBeautyMapper;
import com.wechat.mappers.UserInfoMapper;
import com.wechat.redis.RedisCompent;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: 用户信息Service实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Resource
	private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;

    @Resource
    private UserInfoBeautyMapper<UserInfoBeauty,UserInfoQuery> userInfoBeautyMapper;

    @Resource
    private UserContactMapper<UserContact,UserContactQuery> userContactMapper;

    @Resource
    private UserContactService userContactService;

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisCompent redisCompent;

    @Resource
    private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private MessageHandler messageHandler;

    /**
     * 根据条件查询列表
     */
	@Override
    public List<UserInfo> findListByParam(UserInfoQuery query){
        return this.userInfoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(UserInfoQuery query){
        return this.userInfoMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<UserInfo> findCountByPage(UserInfoQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<UserInfo> list = this.findListByParam(query);
		PaginationResultVO<UserInfo> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(UserInfo bean){
        return this.userInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<UserInfo> beans){
		return this.userInfoMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<UserInfo> beans){
		return this.userInfoMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据UseId查询用户信息
     */
	@Override
    public UserInfo getByUseId(String useId ){
        return this.userInfoMapper.selectByUseId(useId);
    }

    /**
     * 根据UseId更新用户信息
     */
	@Override
    public Integer updateByUseId( UserInfo t,String useId ){
        return this.userInfoMapper.updateByUseId(t,useId);
    }

    /**
     * 根据UseId删除用户信息
     */
	@Override
    public Integer deleteByUseId(String useId ){
        return this.userInfoMapper.deleteByUseId(useId);
    }

    /**
     * 根据Email查询用户信息
     */
	@Override
    public UserInfo getByEmail(String email ){
        return this.userInfoMapper.selectByEmail(email);
    }

    /**
     * 根据Email更新用户信息
     */
	@Override
    public Integer updateByEmail( UserInfo t,String email ){
        return this.userInfoMapper.updateByEmail(t,email);
    }

    /**
     * 根据Email删除用户信息
     */
	@Override
    public Integer deleteByEmail(String email ){
        return this.userInfoMapper.deleteByEmail(email);
    }

    /**
     * 注册
     * @throws BussinessException 
     */
    @SuppressWarnings("null")
    @Override
    public void register( String email, String password, String nickname) throws Exception  {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if(userInfo != null){
            throw new BussinessException("邮箱已被注册");
        }
        String useId = ToolUtils.getUserId();
        
        UserInfoBeauty beautyAccount = userInfoBeautyMapper.selectByEmail(email);

        Boolean useBeautyAccout = beautyAccount!=null && BeautyAccountEnums.NO_USE.getCode().equals(beautyAccount.getStaus());
        if(useBeautyAccout){
            useId = beautyAccount.getUseId();
        }

        Date now = new Date(System.currentTimeMillis());
        userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setUseId(useId);
        userInfo.setNickName(nickname);
        userInfo.setPassword( ToolUtils.encodeMD5(password) );
        userInfo.setCreateTime(now);
        userInfo.setJoinType(JoinTypeEnums.APPAY.getType());
        userInfo.setStatus(UserStatusEnums.ENABLE.getCode());
        userInfo.setLastOffTime(now.getTime());
        userInfoMapper.insert(userInfo);
        if(useBeautyAccout){
            beautyAccount.setStaus(BeautyAccountEnums.USED.getCode());
            userInfoBeautyMapper.updateByUseId(beautyAccount,useId);
        }

        //创建机器人好友
        userContactService.addContact4Robot(useId);

    }

    /**
     * 登录
     * @throws BussinessException 
     */
    @Override
    public UserInfoVO login(String email, String password) throws Exception {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if(userInfo == null || !password.equals(userInfo.getPassword())){
            throw new BussinessException("账号密码错误");
        }
        if(UserStatusEnums.DISABLE.getCode().equals(userInfo.getStatus())){
            throw new BussinessException("账号已禁用");
        }
        Long lastHeartBeat = redisCompent.getUserHeartBeat(userInfo.getUseId());
        if(lastHeartBeat != null){
            throw new BussinessException("用户已登录");
        }
        redisCompent.saveUserHeartBeat(userInfo.getUserId());
        //查询我的好友
        UserContactQuery cInfoQuery = new UserContactQuery();
        cInfoQuery.setUserId(userInfo.getUseId());
        cInfoQuery.setStatus(UserContactStatusEnums.FRIEND.getStatus());
        List<UserContact> contactList = userContactMapper.selectList(cInfoQuery);
        List<String> contactIdList = contactList.stream().map(item->item.getContactId()).collect(Collectors.toList());
        redisCompent.cleanUserContactBatch(userInfo.getUserId());
        if(!contactIdList.isEmpty())
            redisCompent.addUserContactBatch(userInfo.getUserId(), contactIdList);
        //查询我的群组
        TokenUserInfo tokenUserInfo = getTokenUserInfo(userInfo);
        //保存信息到redis
        String token = ToolUtils.encodeMD5(tokenUserInfo.getUserId() + ToolUtils.getRandStr(Constans.LENGHT_20));
        tokenUserInfo.setToken(token);
        redisCompent.saveTokenUserInfo(tokenUserInfo);
        

        UserInfoVO userInfoVO = ToolUtils.copy(userInfo, UserInfoVO.class);
        userInfoVO.setUserId(userInfo.getUseId());
        userInfoVO.setToken(tokenUserInfo.getToken());
        userInfoVO.setAdmin(tokenUserInfo.getAdmin());
        userInfoVO.setNickName(userInfo.getNickName());
        
        return userInfoVO;
    }

    private TokenUserInfo getTokenUserInfo(UserInfo userInfo){
        TokenUserInfo tokenUserInfo = new TokenUserInfo();
        tokenUserInfo.setUserId(userInfo.getUseId());
        tokenUserInfo.setNickName(userInfo.getNickName());
        String adminEmails = appConfig.getAdminEmail();
        String[] adminEmailsArr = adminEmails.split(",");
        boolean admin = adminEmailsArr.length > 0 && ArrayUtils.contains(adminEmailsArr, userInfo.getEmail());
        tokenUserInfo.setAdmin(admin);
        return tokenUserInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInfo(UserInfo userInfo, MultipartFile avatarFile,MultipartFile avatarCover) throws Exception {
        if(avatarFile!=null){
            String baseFloder = appConfig.getProjectFolder()+Constans.FILE_FOLDER_AVATAR_NAME;
            File targetFloder = new File(baseFloder);
            if(!targetFloder.exists()){
                targetFloder.mkdirs();
            }
            String fileName = targetFloder.getPath()+"/"+userInfo.getUseId()+Constans.IMAGE_SUFFIX;
            avatarFile.transferTo(new File(fileName));
            avatarCover.transferTo(new File(targetFloder.getPath()+"/"+userInfo.getUseId()+Constans.COVER_IMAGE_SUFFIX));
        }
        UserInfo oldUserInfo = userInfoMapper.selectByUseId(userInfo.getUseId());
        userInfoMapper.updateByUseId(userInfo,userInfo.getUseId());
        String contactNameUpdate = null;
        if(oldUserInfo.getNickName()!=null && !oldUserInfo.getNickName().equals(userInfo.getNickName())){
            contactNameUpdate = userInfo.getNickName();
        }
        if(contactNameUpdate==null) return;
        //更新redis中的用户信息
        TokenUserInfo tokenUserInfo = redisCompent.getTokenUserInfoByuserId(userInfo.getUseId());
        tokenUserInfo.setNickName(contactNameUpdate);
        //更新会话信息中的昵称信息
        ChatSessionUser updateInfo = new ChatSessionUser();
        updateInfo.setContactName(contactNameUpdate);

        ChatSessionUserQuery chatSessionUserQuery = new ChatSessionUserQuery();
        chatSessionUserQuery.setContactId(userInfo.getUserId());
        chatSessionUserMapper.updateByParam(updateInfo, chatSessionUserQuery);

        UserContactQuery userContactQuery = new UserContactQuery();
        userContactQuery.setContactType(UserContactTypeEnums.USER.getCode());
        userContactQuery.setContactId(userInfo.getUserId());
        userContactQuery.setStatus(UserContactStatusEnums.FRIEND.getStatus());
        List<UserContact> userContactList = userContactMapper.selectList(userContactQuery);
        for(UserContact userContact:userContactList){
            //修改群昵称发送ws消息
            @SuppressWarnings("rawtypes")
            MessageSendDto messageSendDto = new MessageSendDto<>();
            messageSendDto.setMessageType(MessageTypeEnums.CONTACT_NAME_UPDATE.getType());
            messageSendDto.setContactId(userContact.getUserId());
            messageSendDto.setExtendData(contactNameUpdate);
            messageSendDto.setSendUserId(userInfo.getUserId());
            messageSendDto.setSendUserNickName(contactNameUpdate);
            messageSendDto.setContactType(UserContactTypeEnums.USER.getCode());
            messageHandler.sendMessage(messageSendDto);
        }
        
        
    }

    /**
     * 修改状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(String useId, Integer status) throws Exception {
        UserStatusEnums userStatusEnums = UserStatusEnums.getByStatus(status);
        if(userStatusEnums == null){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setStatus(status);
        userInfoMapper.updateByUseId(userInfo,useId);
    }

    /**
     * 强制下线
     */
    @Override
    public void forceOffline(@NotEmpty String userId) throws Exception {
        //强制下线
        @SuppressWarnings("rawtypes")
        MessageSendDto messageSendDto = new MessageSendDto<>();
        messageSendDto.setMessageType(MessageTypeEnums.FORCE_OFF_LINE.getType());
        messageSendDto.setContactType(UserContactTypeEnums.USER.getCode());
        messageSendDto.setContactId(userId);
        messageHandler.sendMessage(messageSendDto);
        
    }

}
