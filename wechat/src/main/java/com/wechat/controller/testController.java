package com.wechat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wechat.entity.dto.MessageSendDto;
import com.wechat.websocket.MessageHandler;

import jakarta.annotation.Resource;

@RestController
public class testController {

    @Resource
    private MessageHandler messageHandler;
    @RequestMapping("/test")
    public String test() {
        @SuppressWarnings("rawtypes")
        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageContent("Hello World! " + System.currentTimeMillis());
        messageHandler.sendMessage(messageSendDto);
        return "Hello World!";
    }
}
