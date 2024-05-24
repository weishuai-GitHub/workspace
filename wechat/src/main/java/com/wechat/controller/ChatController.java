package com.wechat.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.config.AppConfig;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.MessageSendDto;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.po.ChatMeaasge;
import com.wechat.entity.po.ChatSession;
import com.wechat.entity.po.ChatSessionUser;
import com.wechat.entity.query.ChatSessionQuery;
import com.wechat.entity.query.ChatSessionUserQuery;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.exception.BussinessException;
import com.wechat.mappers.ChatSessionMapper;
import com.wechat.mappers.ChatSessionUserMapper;
import com.wechat.redis.RedisCompent;
import com.wechat.service.ChatMeaasgeService;
import com.wechat.utils.ToolUtils;
import com.wechat.websocket.MessageHandler;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.FileInputStream;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/chat")
public class ChatController extends ABaseController{
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Resource
    private RedisCompent redisCompent;

    @Resource
    private ChatSessionMapper<ChatSession,ChatSessionQuery> chatSessionMapper;

    @Resource
    private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery> chatSessionUserMapper;

    @Resource
    private ChatMeaasgeService chatMeaasgeService;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private AppConfig appConfig;

    @SuppressWarnings("rawtypes")
    @RequestMapping("/sendMessage")
    @GlobalIntercepter
    public ResponseVO sendMessage(
        HttpServletRequest request,
        ChatMeaasge chatMeaasge) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        MessageSendDto messageSendDto = chatMeaasgeService.saveMessage(chatMeaasge, tokenUserInfo);
        return getSuccessResponseVO(messageSendDto);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("uploadFile")
    @GlobalIntercepter
    public ResponseVO uploadFile(
        HttpServletRequest request,
        @RequestParam("messageId") @NotNull Long messageId,
        @RequestParam("file") @NotNull MultipartFile file,
        @RequestParam("cover") @NotNull MultipartFile cover) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        chatMeaasgeService.saveMessageFile(tokenUserInfo.getUserId(), messageId, file, cover);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("downloadFile")
    @GlobalIntercepter
    public void downloadFile(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam("fileId") @NotEmpty String fileId,
        @RequestParam("showCover")  @NotNull Boolean showCover) throws Exception{
        TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
        FileInputStream in = null;
        try(ServletOutputStream out = response.getOutputStream()){
            File file = null;
            if(!ToolUtils.isNumber(fileId)){
                String avatarFolderName = Constans.FILE_FOLDER_AVATAR_NAME;
                String avatarPath = appConfig.getProjectFolder() + avatarFolderName + fileId + Constans.IMAGE_SUFFIX;
                if(showCover){
                    avatarPath =  appConfig.getProjectFolder() + avatarFolderName + fileId + Constans.COVER_IMAGE_SUFFIX;
                }
                file = new File(avatarPath);
                if(!file.exists()){
                    throw new BussinessException(ResponseCodeEnum.CODE_602);
                }
            }else{
                file = chatMeaasgeService.downloadFile(tokenUserInfo,Long.parseLong(fileId),showCover);
            }
            response.setContentType("application/x-download;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;");
            response.setContentLengthLong(file.length());
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            int len;
            while((len = in.read(byteData)) > 0){
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("downloadFile error", e);
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        } finally {
            if(in != null){
                in.close();
            }
        }
    }
    
}
