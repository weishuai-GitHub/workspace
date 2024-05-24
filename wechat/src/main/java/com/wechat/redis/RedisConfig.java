
package com.wechat.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
/**
 * @Description: 靓号信息Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
@Configuration
public class RedisConfig<V>{
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    @Bean(name = "redissonClient",destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        try {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
            RedissonClient redissonClient =  Redisson.create();
            return redissonClient;
        } catch (Exception e) {
            logger.error("初始化RedissonClient失败,请redis检查配置", e);
        }
        return null;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 设置key序列化器
        template.setKeySerializer(RedisSerializer.string());
        // 设置value序列化器
        template.setValueSerializer(RedisSerializer.json());
        // 设置hash key序列化器
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置hash value序列化器
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;
    }
}
