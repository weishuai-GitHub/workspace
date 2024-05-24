package com.wechat.mappers;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: 会话IDMapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public interface ChatSessionMapper<T, P> extends BaseMapper<T, P> {
    /**
     * 根据SessionId查询会话ID
     */
    T selectBySessionId( @Param("sessionId") String sessionId );

    /**
     * 根据SessionId更新会话ID
     */
    Integer updateBySessionId( @Param("bean") T t, @Param("sessionId") String sessionId );

    /**
     * 根据SessionId删除会话ID
     */
    Integer deleteBySessionId( @Param("sessionId") String sessionId );

}
