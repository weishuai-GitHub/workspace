
package com.wechat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.po.UserInfo;
import com.wechat.entity.query.UserInfoQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.service.UserInfoService;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin")
public class AdminController extends ABaseController{

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Resource
    private UserInfoService userInfoService;

    @SuppressWarnings("rawtypes")
    @RequestMapping("loadUser")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO loadUser(UserInfoQuery userInfoQuery) {
        // @SuppressWarnings("rawtypes")
        userInfoQuery.setOrderBy("create_time desc");
        PaginationResultVO<UserInfo> user =  userInfoService.findCountByPage(userInfoQuery);
        return getSuccessResponseVO(user);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("updateUserStatus")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO updateUserStatus(
        @RequestParam("userId") @NotEmpty String userId,
        @RequestParam("status") @NotNull Integer status) throws Exception {
        // @SuppressWarnings("rawtypes")
        userInfoService.updateUserStatus(userId,status);
        return getSuccessResponseVO(null);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("forceOffLine")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO forceOffLine(@RequestParam("userId") @NotEmpty String userId) throws Exception {
        // @SuppressWarnings("rawtypes")
        userInfoService.forceOffline(userId);
        return getSuccessResponseVO(null);
    }
}
