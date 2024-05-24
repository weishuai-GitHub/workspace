package com.wechat.mappers;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: app发布Mapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/19
 */
public interface AppUpdateMapper<T, P> extends BaseMapper<T, P> {
    /**
     * 根据Id查询app发布
     */
    T selectById( @Param("id") Integer id );

    /**
     * 根据Id更新app发布
     */
    Integer updateById( @Param("bean") T t, @Param("id") Integer id );

    /**
     * 根据Id删除app发布
     */
    Integer deleteById( @Param("id") Integer id );

    /**
     * 根据Version查询app发布
     */
    T selectByVersion( @Param("version") String version );

    /**
     * 根据Version更新app发布
     */
    Integer updateByVersion( @Param("bean") T t, @Param("version") String version );

    /**
     * 根据Version删除app发布
     */
    Integer deleteByVersion( @Param("version") String version );

    T selectLatestVersion(@Param("appVersion") String appVersion,@Param("uid") String uid);

}
