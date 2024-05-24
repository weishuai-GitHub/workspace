package com.wechat.websocket.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class HandlerHeartBeat extends ChannelDuplexHandler{
    private static final Logger logger = LoggerFactory.getLogger(HandlerHeartBeat.class);
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent e = (IdleStateEvent) evt;
            Channel channel = ctx.channel();
            if(e.state()== IdleState.READER_IDLE){
                Attribute<Object> attributeKey = channel.attr(AttributeKey.valueOf(channel.id().toString()));
                String userId = (String) attributeKey.get();
                logger.info("用户{}心跳超时",userId);
                ctx.close();
            }else if(e.state()== IdleState.WRITER_IDLE){
                ctx.writeAndFlush("heart");
            }
        }
    }

}
