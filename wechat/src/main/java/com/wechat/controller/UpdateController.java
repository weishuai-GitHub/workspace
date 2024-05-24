package com.wechat.controller;

import java.util.Arrays;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.config.AppConfig;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.enums.AppUpdateFileTypeEnums;
import com.wechat.entity.po.AppUpdate;
import com.wechat.entity.vo.AppUpdateVO;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.service.AppUpdateService;
import com.wechat.utils.ToolUtils;

import jakarta.annotation.Resource;

@RestController("updateController")
@RequestMapping("/update")
public class UpdateController extends ABaseController{

    @Resource
    private AppUpdateService appUpdateService;

    @Resource
    private AppConfig appConfig;

    @SuppressWarnings({ "rawtypes" })
    @RequestMapping("checkVersion")
    @GlobalIntercepter
    public ResponseVO checkVersion(@RequestParam("appVersion") String appVersion,
    @RequestParam("uid") String uid) {
        // @SuppressWarnings("rawtypes")
        if(appVersion == null || "".equals(appVersion)){
            return getSuccessResponseVO(null);
        }
        AppUpdate appUpdate  = appUpdateService.getLatestVersion(appVersion, uid);
        if(appUpdate == null) return getSuccessResponseVO(null);
        AppUpdateVO  appUpdateVO = ToolUtils.copy(appUpdate, AppUpdateVO.class);
        if(AppUpdateFileTypeEnums.LOCAL_FILE.getType().equals(appUpdate.getFileType())){
        }else appUpdateVO.setSize(0L);
        appUpdateVO.setUpdateList(Arrays.asList(appUpdate.getUpdateDescArray()));
        String fileName = Constans.APP_NAME + appUpdate.getVersion() + Constans.APP_EXE_SUFFIX;
        appUpdateVO.setFileName(fileName);
        return getSuccessResponseVO(appUpdateVO);
    }

}
