package com.wechat.websocket;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wechat.entity.dto.MessageSendDto;
import com.wechat.utils.JsonUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
/**
 * @Description: 消息处理器
 * 
 */
@Component("messageHandler")
public class MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    private static final String MESSAGE_TOPIC = "message.topic";
    
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ChannelContextUtils channelContextUtils;

    @PostConstruct
    public void lisMessage() {
        RTopic rTopic = redissonClient.getTopic(MESSAGE_TOPIC);
        rTopic.addListener(MessageSendDto.class, (channel, message) -> {
            logger.info("接收到消息：{}",JsonUtils.toJson(message) );
            // sendMessage(message);
            channelContextUtils.sendMessage(message);
        });
    }

    /**
     * @Description: 发送消息
     * @param messageSendDto
     */
    public void sendMessage(@SuppressWarnings("rawtypes") MessageSendDto messageSendDto) {
        RTopic rTopic = redissonClient.getTopic(MESSAGE_TOPIC);
        rTopic.publish(messageSendDto);
    }
}
