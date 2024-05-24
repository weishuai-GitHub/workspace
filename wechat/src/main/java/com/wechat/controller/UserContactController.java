package com.wechat.controller;

import org.springframework.web.bind.annotation.RestController;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.entity.vo.UserInfoVO;
import com.wechat.exception.BussinessException;
import com.wechat.service.GroupInfoService;
import com.wechat.service.UserContactApplyService;
import com.wechat.service.UserContactService;
import com.wechat.service.UserInfoService;
import com.wechat.utils.ToolUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.dto.UserContactSearchResultDto;
import com.wechat.entity.enums.UserContactStatusEnums;
import com.wechat.entity.enums.UserContactTypeEnums;
import com.wechat.entity.po.GroupInfo;
import com.wechat.entity.po.UserContact;
import com.wechat.entity.po.UserInfo;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.query.UserContactApplyQuery;
import com.wechat.entity.query.UserContactQuery;
@RestController
@RequestMapping("/contact")
public class UserContactController extends ABaseController{

    @Resource
    private UserContactService userContactService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private GroupInfoService groupInfoService;

    @Resource
    private UserContactApplyService userContactApplyService;

    @SuppressWarnings("rawtypes")
    @RequestMapping("search")
    @GlobalIntercepter
    public ResponseVO search(HttpServletRequest request,
    @RequestParam("contactId") @NotEmpty String contactId){
        UserContactSearchResultDto userContactSearchResultDto = userContactService.searchContact(getTokenUserInfo(request).getUserId(), contactId);
        return getSuccessResponseVO(userContactSearchResultDto);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("applyAdd")
    @GlobalIntercepter
    public ResponseVO applyAdd(HttpServletRequest request,
    @RequestParam("contactId") @NotEmpty String contactId,@RequestParam("applyInfo") String applyInfo) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        Integer result = userContactService.applyAdd(tokenUserInfo, contactId, applyInfo);
        return getSuccessResponseVO(result);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("loadApply")
    @GlobalIntercepter
    public ResponseVO loadApply(HttpServletRequest request,
    @RequestParam("pageNo") Integer pageNo) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        UserContactApplyQuery query = new UserContactApplyQuery();
        query.setOrderBy("last_apply_time desc");
        query.setPageNo(pageNo);
        query.setReceiveUserId(tokenUserInfo.getUserId());
        query.setPageSize(SimplePage.SIZE15);
        query.setQueryContactInfo(true);
        PaginationResultVO result = userContactApplyService.findCountByPage(query);
        return getSuccessResponseVO(result);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("dealWithApply")
    @GlobalIntercepter
    public ResponseVO dealWithApply(HttpServletRequest request,
    @RequestParam("applyId")  @NotNull Integer applyId,
    @RequestParam("status")  @NotNull Integer status) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        this.userContactApplyService.dealWithApply(tokenUserInfo.getUserId(), applyId, status);
        return getSuccessResponseVO(null);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("loadContact")
    @GlobalIntercepter
    public ResponseVO loadContact(HttpServletRequest request,
    @RequestParam("contactType")  @NotNull String contactType) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        UserContactTypeEnums  userContactTypeEnums= UserContactTypeEnums.getEnumByName(contactType);
        if(userContactTypeEnums == null){
            throw new BussinessException("参数错误");
        }
        UserContactQuery query = new UserContactQuery();
        query.setUserId(tokenUserInfo.getUserId());
        query.setContactType(userContactTypeEnums.getCode());
        if(userContactTypeEnums == UserContactTypeEnums.USER){
            query.setQueryContactInfo(true);
        }
        else if(userContactTypeEnums == UserContactTypeEnums.GROUP){
            query.setQueryGroupInfo(true);
            query.setExcludeMyGroup(true);
        }
        query.setOrderBy("last_update_time desc");
        query.setStatusArr(new Integer[]{
            UserContactStatusEnums.FRIEND.getStatus(), 
            UserContactStatusEnums.BLACKLIST_BE.getStatus(),
            UserContactStatusEnums.DEL_BE.getStatus()});
        List<UserContact> userContactList = userContactService.findListByParam(query);
        return getSuccessResponseVO(userContactList);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("getContactInfo")
    @GlobalIntercepter
    public ResponseVO getContactInfo(HttpServletRequest request,
    @RequestParam("contactId")  @NotNull String contactId) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        UserContact userContact = userContactService.getByUserIdAndContactId(tokenUserInfo.getUserId(), contactId);
        UserContactTypeEnums userContactTypeEnums = UserContactTypeEnums.getEnumByPrefix(contactId.substring(0,1)); 
        if(userContactTypeEnums==UserContactTypeEnums.USER)
        {
            UserInfo userInfo = userInfoService.getByUseId( contactId);
            UserInfoVO userInfoVO = ToolUtils.copy(userInfo, UserInfoVO.class);
            userInfoVO.setUserId(userInfo.getUseId());
            userInfoVO.setContactStaus(UserContactStatusEnums.NOT_FRIEDND.getStatus());
            if(userContact != null){
                userInfoVO.setContactStaus(UserContactStatusEnums.FRIEND.getStatus());
            }  
            return getSuccessResponseVO(userInfoVO);
        }
        else if(userContactTypeEnums==UserContactTypeEnums.GROUP)
        {
            GroupInfo groupInfo = groupInfoService.getByGroupId(contactId);
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setUserId(groupInfo.getGroupId());
            userInfoVO.setNickName(groupInfo.getGroupName());
            userInfoVO.setContactStaus(UserContactStatusEnums.NOT_FRIEDND.getStatus());
            if(userContact != null){
                userInfoVO.setContactStaus(UserContactStatusEnums.FRIEND.getStatus());
            }
            // userInfoVO.setAreaName(groupInfo.get);
            return getSuccessResponseVO(userInfoVO);
        }
        return getSuccessResponseVO(null); 
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("getContactUserInfo")
    @GlobalIntercepter
    public ResponseVO getContactUserInfo(HttpServletRequest request,
    @RequestParam("contactId")  @NotNull String contactId) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        UserContact userContact = userContactService.getByUserIdAndContactId(tokenUserInfo.getUserId(), contactId);
        if(userContact == null|| !ArrayUtils.contains(new Integer[]{
            UserContactStatusEnums.DEL_BE.getStatus(),
            UserContactStatusEnums.BLACKLIST_BE.getStatus(),
            UserContactStatusEnums.FRIEND.getStatus()
        } , userContact.getStatus())){
            throw new BussinessException("没有权限查看");
        }
        UserInfo userInfo = userInfoService.getByUseId( contactId);
        UserInfoVO userInfoVO = ToolUtils.copy(userInfo, UserInfoVO.class);
        userInfoVO.setUserId(userInfo.getUseId());
        userInfoVO.setContactStaus(UserContactStatusEnums.NOT_FRIEDND.getStatus());
        if(userContact != null){
            userInfoVO.setContactStaus(UserContactStatusEnums.FRIEND.getStatus());
        }  
        return getSuccessResponseVO(userInfoVO); 
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("delContact")
    @GlobalIntercepter
    public ResponseVO delContact(HttpServletRequest request,
    @RequestParam("contactId")  @NotNull String contactId) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        userContactService.deleteContact(tokenUserInfo.getUserId(), contactId, UserContactStatusEnums.DEL);
        return getSuccessResponseVO(null);
       
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("addContact2BlackList")
    @GlobalIntercepter
    public ResponseVO addContact2BlackList(HttpServletRequest request,
    @RequestParam("contactId")  @NotNull String contactId) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        userContactService.deleteContact(tokenUserInfo.getUserId(), contactId, UserContactStatusEnums.BLACKLIST);
        return getSuccessResponseVO(null);
       
    }
}
