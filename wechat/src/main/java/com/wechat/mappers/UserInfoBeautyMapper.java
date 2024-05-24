package com.wechat.mappers;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: 靓号信息Mapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
public interface UserInfoBeautyMapper<T, P> extends BaseMapper<T, P> {
    /**
     * 根据Id查询靓号信息
     */
    T selectById( @Param("id") Integer id );

    /**
     * 根据Id更新靓号信息
     */
    Integer updateById( @Param("bean") T t, @Param("id") Integer id );

    /**
     * 根据Id删除靓号信息
     */
    Integer deleteById( @Param("id") Integer id );

    /**
     * 根据UseId查询靓号信息
     */
    T selectByUseId( @Param("useId") String useId );

    /**
     * 根据UseId更新靓号信息
     */
    Integer updateByUseId( @Param("bean") T t, @Param("useId") String useId );

    /**
     * 根据UseId删除靓号信息
     */
    Integer deleteByUseId( @Param("useId") String useId );

    /**
     * 根据Email查询靓号信息
     */
    T selectByEmail( @Param("email") String email );

    /**
     * 根据Email更新靓号信息
     */
    Integer updateByEmail( @Param("bean") T t, @Param("email") String email );

    /**
     * 根据Email删除靓号信息
     */
    Integer deleteByEmail( @Param("email") String email );

}
