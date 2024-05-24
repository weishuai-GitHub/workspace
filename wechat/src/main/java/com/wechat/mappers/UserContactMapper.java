package com.wechat.mappers;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: 联系人信息Mapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public interface UserContactMapper<T, P> extends BaseMapper<T, P> {
    /**
     * 根据UserIdAndContactId查询联系人信息
     */
    T selectByUserIdAndContactId( @Param("userId") String userId, @Param("contactId") String contactId );

    /**
     * 根据UserIdAndContactId更新联系人信息
     */
    Integer updateByUserIdAndContactId( @Param("bean") T t, @Param("userId") String userId, @Param("contactId") String contactId );

    /**
     * 根据UserIdAndContactId删除联系人信息
     */
    Integer deleteByUserIdAndContactId( @Param("userId") String userId, @Param("contactId") String contactId );

}
