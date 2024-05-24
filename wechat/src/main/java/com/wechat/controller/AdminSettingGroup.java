
package com.wechat.controller;

import java.io.File;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.config.AppConfig;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.SysSettingDto;
import com.wechat.entity.query.GroupInfoQuery;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.redis.RedisCompent;

import jakarta.annotation.Resource;

@RestController("adminSettingGroup")
@RequestMapping("/admin")
public class AdminSettingGroup  extends ABaseController{
    @Resource
    private RedisCompent redisCompent;

    @Resource
    private AppConfig appConfig;

    @SuppressWarnings("rawtypes")
    @RequestMapping("getSysSetting")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO getSysSetting(GroupInfoQuery groupInfoQuery) {
        // @SuppressWarnings("rawtypes")
        SysSettingDto sysSettingDto = redisCompent.getSysSetting();
        return getSuccessResponseVO(sysSettingDto);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("saveSysSetting")
    @GlobalIntercepter(checkAdmin = true)
    public ResponseVO saveSysSetting(
        SysSettingDto sysSettingDto,
        @RequestParam("robotFile") MultipartFile robotFile,
	    @RequestParam("robotCover") MultipartFile robotCover) throws Exception {
        // @SuppressWarnings("rawtypes")
        if(robotFile != null){
            String baseFolder = appConfig.getProjectFolder();
            File TargerFolder = new File(baseFolder + Constans.FILE_FOLDER_AVATAR_NAME);
            if(!TargerFolder.exists()){
                TargerFolder.mkdirs();
            }
            String filePath = TargerFolder.getPath() + "/" + Constans.ROBOT_UID + Constans.IMAGE_SUFFIX;
            robotFile.transferTo(new File(filePath));
            robotCover.transferTo(new File(TargerFolder.getPath() + "/" + Constans.ROBOT_UID + Constans.COVER_IMAGE_SUFFIX));
        }
        redisCompent.saveSysSetting(sysSettingDto);
        return getSuccessResponseVO(null);
    }
}
