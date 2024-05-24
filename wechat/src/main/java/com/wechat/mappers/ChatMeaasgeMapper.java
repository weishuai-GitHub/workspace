package com.wechat.mappers;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: 聊天消息表Mapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public interface ChatMeaasgeMapper<T, P> extends BaseMapper<T, P> {
    /**
     * 根据MessageId查询聊天消息表
     */
    T selectByMessageId( @Param("messageId") Long messageId );

    /**
     * 根据MessageId更新聊天消息表
     */
    Integer updateByMessageId( @Param("bean") T t, @Param("messageId") Long messageId );

    /**
     * 根据MessageId删除聊天消息表
     */
    Integer deleteByMessageId( @Param("messageId") Long messageId );

}
