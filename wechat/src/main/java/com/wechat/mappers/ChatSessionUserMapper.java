package com.wechat.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: 会话用户Mapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public interface ChatSessionUserMapper<T, P> extends BaseMapper<T, P> {
    /**
     * 根据UserIdAndContactId查询会话用户
     */
    T selectByUserIdAndContactId( @Param("userId") String userId, @Param("contactId") String contactId );

    /**
     * 根据UserIdAndContactId更新会话用户
     */
    Integer updateByUserIdAndContactId( @Param("bean") T t, @Param("userId") String userId, @Param("contactId") String contactId );

    /**
     * 根据UserIdAndContactId删除会话用户
     */
    Integer deleteByUserIdAndContactId( @Param("userId") String userId, @Param("contactId") String contactId );

    List<T> findListParam(@Param("query") P p);
}
