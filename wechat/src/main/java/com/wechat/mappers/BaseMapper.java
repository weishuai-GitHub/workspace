package com.wechat.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<T,P> {

    /**
     * insert:插入数据. <br/>
     */
    Integer insert(@Param("bean") T t);

    /**
     * insertOrUpdate:更新数据或插入. <br/>
     */
    Integer insertOrUpdate(@Param("bean") T t);

    /**
     * insertBatch:批量插入. <br/>
     */
    Integer insertBatch(@Param("list") List<T> list);

    /**
     * insertOrUpdateBatch:批量更新数据或插入. <br/>
     */

    Integer insertOrUpdateBatch(@Param("list") List<T> list);
    
    /**
     * update:根据参数更新数据. <br/>
     */
    Integer updateByParam(@Param("bean") T t, @Param("query") P p);
    /**
     * selectList:根据参数查询集合. <br/>
     */

    List<T> selectList(@Param("query") P p);

    /**
     * selectCount:根据集合查询数量. <br/>
     */
    Integer selectCount(@Param("query") P p);

}
