package com.wechat.controller;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.enums.GroupStatusEnums;
import com.wechat.entity.enums.MessageTypeEnums;
import com.wechat.entity.enums.UserContactStatusEnums;
import com.wechat.entity.po.GroupInfo;
import com.wechat.entity.po.UserContact;
import com.wechat.entity.query.GroupInfoQuery;
import com.wechat.entity.query.UserContactQuery;
import com.wechat.entity.vo.GroupInfoVo;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.exception.BussinessException;
import com.wechat.service.GroupInfoService;
import com.wechat.service.UserContactService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;


/**
 * @Description: 群组信息Controller
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
@RestController
@RequestMapping("/group")
public class GroupInfoController extends ABaseController {

    @Resource
    private GroupInfoService groupInfoService;

    @Resource
    private UserContactService userContactService;

    @SuppressWarnings("rawtypes")
    @RequestMapping("saveGroup")
    @GlobalIntercepter
    public ResponseVO saveGroup(
            HttpServletRequest request,
            @RequestParam("groupName") @NotNull String groupName,
            @RequestParam("groupNotice") String groupNotice,
            @RequestParam("joinType") @NotNull Integer joinType,
            @RequestParam(value = "groupId",required = false,defaultValue = "") String groupId,
            @RequestParam(value = "avatarFile",required = false) MultipartFile avatarFile,
            @RequestParam(value = "avatarCover",required = false) MultipartFile avatarCover) throws Exception {
        
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupName(groupName);
        groupInfo.setGroupOwnerId(tokenUserInfo.getUserId());
        groupInfo.setGroupNotice(groupNotice);
        groupInfo.setJoinType(joinType);
        groupInfo.setGroupId(groupId);
        this.groupInfoService.saveGroup(groupInfo, avatarFile, avatarCover);
        return getSuccessResponseVO(null);
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping("loadMyGroup")
    @GlobalIntercepter
    public ResponseVO getGroupList(HttpServletRequest request) {
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        GroupInfoQuery query = new GroupInfoQuery();
        query.setGroupOwnerId(tokenUserInfo.getUserId());
        query.setOrderBy("create_time desc");
        List<GroupInfo> groupList = this.groupInfoService.findListByParam(query);
        return getSuccessResponseVO(groupList);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("getGroupInfo")
    @GlobalIntercepter
    public ResponseVO getGroupInfo(HttpServletRequest request,
    @RequestParam("groupId") @NotEmpty String groupId) throws BussinessException {
        GroupInfo groupInfo = getGroupDetailCommon(request, groupId);
        UserContactQuery query = new UserContactQuery();
        query.setContactId(groupId);
        Integer memberCount = this.userContactService.findCountByParam(query);
        groupInfo.setMemberCount(memberCount);

        return getSuccessResponseVO(groupInfo);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("dissolutionGroup")
    @GlobalIntercepter
    public ResponseVO dissolutionGroup(HttpServletRequest request,
    @RequestParam("groupId") @NotEmpty String groupId) throws Exception {
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        groupInfoService.dissolutionGroup(tokenUserInfo.getUserId(), groupId);
        return getSuccessResponseVO(null);
    }

    private GroupInfo getGroupDetailCommon(HttpServletRequest request, String groupId) throws BussinessException {
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        UserContact userContact = this.userContactService.getByUserIdAndContactId(tokenUserInfo.getUserId(), groupId);
        if(userContact == null || !UserContactStatusEnums.FRIEND.getStatus().equals(userContact.getStatus())){
            throw new BussinessException("你不在该群组中或该群聊不存在");
        }
        GroupInfo groupInfo = this.groupInfoService.getByGroupId(groupId);
        if(groupInfo == null || GroupStatusEnums.DEL.getStatus().equals(groupInfo.getStatus())){
            throw new BussinessException("该群聊不存在或已被删除");
        }
        return groupInfo;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("getGroupInfo4Chat")
    @GlobalIntercepter
    public ResponseVO getGroupInfo4Chat(HttpServletRequest request,
    @RequestParam("groupId") @NotEmpty String groupId) throws BussinessException {
        GroupInfo groupInfo = getGroupDetailCommon(request, groupId);
        UserContactQuery query = new UserContactQuery();
        query.setContactId(groupId);
        query.setQueryUserInfo(true);
        query.setOrderBy("create_time desc");
        query.setStatus(UserContactStatusEnums.FRIEND.getStatus());
        List<UserContact> userContactList = this.userContactService.findListByParam(query);
        GroupInfoVo groupInfoVo = new GroupInfoVo();
        groupInfoVo.setGroupInfo(groupInfo);
        groupInfoVo.setUserInfoList(userContactList);
        return getSuccessResponseVO(groupInfoVo);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("addOrRemoveGroupUser")
    @GlobalIntercepter
    public ResponseVO addOrRemoveGroupUser(HttpServletRequest request,
    @RequestParam("groupId") @NotEmpty String groupId,
    @RequestParam("selectContacts") @NotEmpty String selectContacts,
    @RequestParam("opType") @NotNull Integer opType) throws Exception {
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        groupInfoService.addOrRemoveGroupUser(tokenUserInfo, groupId, selectContacts, opType);
        return getSuccessResponseVO(null);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("leaveGroup")
    @GlobalIntercepter
    public ResponseVO leaveGroup(HttpServletRequest request,
    @RequestParam("groupId") @NotEmpty String groupId) throws Exception {
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        groupInfoService.leafGroup(tokenUserInfo.getUserId(), groupId, MessageTypeEnums.LEAVE_GROUP);
        return getSuccessResponseVO(null);
    }


}
