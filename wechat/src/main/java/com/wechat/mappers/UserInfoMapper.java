package com.wechat.mappers;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: 用户信息Mapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
public interface UserInfoMapper<T, P> extends BaseMapper<T, P> {
    /**
     * 根据UseId查询用户信息
     */
    T selectByUseId( @Param("useId") String useId );

    /**
     * 根据UseId更新用户信息
     */
    Integer updateByUseId( @Param("bean") T t, @Param("useId") String useId );

    /**
     * 根据UseId删除用户信息
     */
    Integer deleteByUseId( @Param("useId") String useId );

    /**
     * 根据Email查询用户信息
     */
    T selectByEmail( @Param("email") String email );

    /**
     * 根据Email更新用户信息
     */
    Integer updateByEmail( @Param("bean") T t, @Param("email") String email );

    /**
     * 根据Email删除用户信息
     */
    Integer deleteByEmail( @Param("email") String email );

}
