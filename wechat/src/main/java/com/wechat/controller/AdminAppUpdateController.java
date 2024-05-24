package com.wechat.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.po.AppUpdate;
import com.wechat.entity.query.AppUpdateQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.service.AppUpdateService;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;

@RestController("adminAppUpdateController")
@RequestMapping("/admin")
public class AdminAppUpdateController extends ABaseController{

    @Resource
    private AppUpdateService appUpdateService;

    @SuppressWarnings("rawtypes")
    @RequestMapping("loadUpdateList")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO loadUpdateList(AppUpdateQuery appUpdateQuery) {
        // @SuppressWarnings("rawtypes")
        appUpdateQuery.setOrderBy("create_time desc");
        PaginationResultVO gResultVO =  appUpdateService.findCountByPage(appUpdateQuery);
        return getSuccessResponseVO(gResultVO);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("saveUpdate")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO saveUpdate(AppUpdate appUpdate) throws Exception {
        appUpdateService.saveUpdate(appUpdate);
        return getSuccessResponseVO(null);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("delUpdate")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO delUpdate(@RequestParam("id") @NotNull Integer id) throws Exception {
        // @SuppressWarnings("rawtypes")
        appUpdateService.deleteById(id);
        return getSuccessResponseVO(null);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("postUpdate")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO postUpdate(@RequestParam("id") @NotNull Integer id,
    @RequestParam("status") @NotNull Integer status,
    @RequestParam("grayscaleUid") String grayscaleUid) throws Exception {
        // @SuppressWarnings("rawtypes")
        appUpdateService.postUpdate(id,status,grayscaleUid);
        return getSuccessResponseVO(null);
    }
}
