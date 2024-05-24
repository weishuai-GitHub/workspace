package com.wechat.websocket.netty;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wechat.entity.config.AppConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
// import io.netty.handler.codec.http.HttpObjectAggregator;
// import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;

@Component("nettyWebSocketStarter")
public class NettyWebSocketStarter implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketStarter.class);
    private static EventLoopGroup bossGroup = new NioEventLoopGroup();

    private static EventLoopGroup workGroup = new NioEventLoopGroup();

    @Resource
    private HandlerWebSocket handlerWebSocket;

    @Resource
    private AppConfig appConfig;


    @PreDestroy
    public void close(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @Override
    public void run()
    {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ChannelInitializer<Channel>(){
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //设置几个重要的处理器
                        //对http协议的支持，使用http编解码器
                        HttpServerCodec httpServerCodec = new HttpServerCodec();
                        pipeline.addLast(httpServerCodec);
                        //聚合解码httpRequest/Response/lastHttpContent到FullHttpRequest
                        //保证接收的http请求的完整性
                        pipeline.addLast(new HttpObjectAggregator(64*1024));
                        

                        //将http协议升级为ws协议,对websocket支持
                        pipeline.addLast(new WebSocketServerCompressionHandler());
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws",null,true,65536,true,true,1000L));
                        pipeline.addLast(handlerWebSocket);
                        // 心跳 
                        //readerIdleTimeSeconds 读超时，即测试一定时间内未接收到被测试端消息
                        //writerIdleTimeSeconds 写超时，即测试端一定时间内未发送消息
                        //allIdleTimeSeconds 所有类型的超时时间
                        pipeline.addLast(new IdleStateHandler(60, 0, 0,TimeUnit.SECONDS));
                        pipeline.addLast(new HandlerHeartBeat());
                    }
                });
            
            ChannelFuture channelFuture = serverBootstrap.bind(appConfig.getWsPort()).sync();
            logger.info("netty启动成功,端口:{}",appConfig.getWsPort());
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("启动netty失败",e);
        }finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
