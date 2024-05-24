package com.wechat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.wechat.redis.RedisUtils;
import com.wechat.websocket.netty.NettyWebSocketStarter;

import io.lettuce.core.RedisConnectionException;
import jakarta.annotation.Resource;

@Component("initRun")
public class InitRun implements ApplicationRunner{
    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private NettyWebSocketStarter nettyWebSocketStarter;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            logger.info("初始化开始");
            dataSource.getConnection();
            logger.info("初始化数据源:{}",dataSource);
            Integer name = (Integer) redisUtils.get("name");
            logger.info("redis启动成功,name =" + name);
            new Thread(nettyWebSocketStarter).start();  // Start the Netty server
            logger.info("初始化netty:{}",nettyWebSocketStarter);
            logger.info("初始化结束");
        }catch (SQLException e) {
            logger.error("数据库配置错误", e);
        }catch(RedisConnectionException e){
            logger.error("redis配置错误，请检查redis配置");
        }catch(Exception e){
            logger.error("启动失败",e);
        }
    }
}
