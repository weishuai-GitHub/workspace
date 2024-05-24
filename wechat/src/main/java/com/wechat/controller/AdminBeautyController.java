package com.wechat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.po.UserInfoBeauty;
import com.wechat.entity.query.UserInfoBeautyQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.service.UserInfoBeautyService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

@RestController("adminBeautyController")
@RequestMapping("/admin")
public class AdminBeautyController extends ABaseController{
    
    @Resource
    private UserInfoBeautyService userInfoBeautyService;

    @SuppressWarnings("rawtypes")
    @RequestMapping("loadBeautyAccountList")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO loadBeautyAccountList(UserInfoBeautyQuery userInfoBeautyQuery) {
        // @SuppressWarnings("rawtypes")
        PaginationResultVO userBeauty =  userInfoBeautyService.findCountByPage(userInfoBeautyQuery);
        return getSuccessResponseVO(userBeauty);
    }

    @SuppressWarnings("rawtypes")
	@RequestMapping("saveBeautAccount")
	@GlobalIntercepter
	public ResponseVO saveBeautAccount(HttpServletRequest request,
	UserInfoBeauty userInfoBeauty) throws Exception {
		userInfoBeautyService.saveBeautAccount(userInfoBeauty);
		return getSuccessResponseVO(null);
	}

    @SuppressWarnings("rawtypes")
	@RequestMapping("delBeautAccount")
	@GlobalIntercepter
	public ResponseVO delBeautAccount(HttpServletRequest request,
	@RequestParam("id") @NotNull Integer id) throws Exception {
		userInfoBeautyService.deleteById(id);
		return getSuccessResponseVO(null);
	}

}
