package com.wechat.mappers;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: 群组信息Mapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public interface GroupInfoMapper<T, P> extends BaseMapper<T, P> {
    /**
     * 根据GroupId查询群组信息
     */
    T selectByGroupId( @Param("groupId") String groupId );

    /**
     * 根据GroupId更新群组信息
     */
    Integer updateByGroupId( @Param("bean") T t, @Param("groupId") String groupId );

    /**
     * 根据GroupId删除群组信息
     */
    Integer deleteByGroupId( @Param("groupId") String groupId );

}
