package com.wechat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.po.GroupInfo;
import com.wechat.entity.query.GroupInfoQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.exception.BussinessException;
import com.wechat.service.GroupInfoService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;

@RestController("adminGroupController")
@RequestMapping("/admin")
public class AdminGroupController extends ABaseController{
    @Resource
    private GroupInfoService groupInfoService;

    @SuppressWarnings("rawtypes")
    @RequestMapping("loadGroup")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO loadGroup(GroupInfoQuery groupInfoQuery) {
        // @SuppressWarnings("rawtypes")
        groupInfoQuery.setOrderBy("create_time desc");
        groupInfoQuery.setQueryGroupOwnerName(true);
        groupInfoQuery.setQueryMemberCount(true);
        PaginationResultVO gResultVO =  groupInfoService.findCountByPage(groupInfoQuery);
        return getSuccessResponseVO(gResultVO);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("dissolutionGroup")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO dissolutionGroup(HttpServletRequest request,
    @RequestParam("groupId") @NotEmpty String groupId) throws Exception {
        // @SuppressWarnings("rawtypes")
        GroupInfo groupInfo = groupInfoService.getByGroupId(groupId);
        if(groupInfo == null){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        groupInfoService.dissolutionGroup(groupInfo.getGroupOwnerId(),groupId);
        return getSuccessResponseVO(null);
    }
}
