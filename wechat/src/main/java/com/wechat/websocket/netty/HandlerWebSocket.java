
package com.wechat.websocket.netty;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.redis.RedisCompent;
import com.wechat.websocket.ChannelContextUtils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
// import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;

@Component("handlerWebSocket")
@Sharable
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(HandlerWebSocket.class);

    @Resource
    private RedisCompent redisCompent;

    @Resource
    private ChannelContextUtils channelContextUtils;

    /**
     * 通道就绪调用,一般用户来做初始化
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有新的连接加入....");
    }

    /**
     * 通道断开调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("有连接断开....");
        channelContextUtils.removeContext(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Channel channel = ctx.channel();
        Attribute<Object> attributeKey = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = (String) attributeKey.get();
        // logger.info("收到消息userId={}的消息{}",userId, textWebSocketFrame.text());
        redisCompent.saveUserHeartBeat(userId);
        // channelContextUtils.send2Group(textWebSocketFrame.text());
    }   

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            fullHttpRequestHandler(ctx, (FullHttpRequest) msg);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String url = handshakeComplete.requestUri();
            QueryStringDecoder decoder = new QueryStringDecoder(url);
            Map<String, List<String>> parameters = decoder.parameters();
            if (parameters.isEmpty() || !parameters.containsKey("token")) {
                ctx.channel().close();
                return;
            }
            List<String> tokens = parameters.get("token");
            String token = tokens.get(0);
            TokenUserInfo tokenUserInfo = redisCompent.getTokenUserInfo(token);
            if (tokenUserInfo == null) {
                logger.error("tokenUserInfo is null");
                ctx.channel().close();
                return;

            }
            // redisCompent.saveChannel(tokenUserInfo.getUserId(), ctx.channel());
            channelContextUtils.addContext(tokenUserInfo.getUserId(), ctx.channel());
            // logger.info("握手成功,{}", url);
        }
    }

    /**
     * 处理连接请求，客户端WebSocket发送握手包时会执行这一次请求
     * 
     * @param ctx
     * @param request
     */
    private void fullHttpRequestHandler(ChannelHandlerContext ctx, FullHttpRequest request) {
        String url = request.uri();
        QueryStringDecoder decoder = new QueryStringDecoder(url);
        Map<String, List<String>> parameters = decoder.parameters();
        if (parameters.isEmpty() || !parameters.containsKey("token")) {
            ctx.channel().close();
            return;
        }
        List<String> tokens = parameters.get("token");
        String token = tokens.get(0);
        TokenUserInfo tokenUserInfo = redisCompent.getTokenUserInfo(token);
        if (tokenUserInfo == null) {
            logger.error("tokenUserInfo is null");
            ctx.channel().close();
            return;

        }
        // redisCompent.saveChannel(tokenUserInfo.getUserId(), ctx.channel());
        channelContextUtils.addContext(tokenUserInfo.getUserId(), ctx.channel());
        // logger.info("握手成功,{}", url);
    }
}
