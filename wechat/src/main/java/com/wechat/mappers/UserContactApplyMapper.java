package com.wechat.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;


/**
 * @Description: 联系人申请信息Mapper
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public interface UserContactApplyMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 联合查询联系人申请信息
     * @param applyId
     * @return
     */
    List<T> selectJoinQuery( @Param("query") P p );
    /**
     * 根据ApplyId查询联系人申请信息
     */
    T selectByApplyId( @Param("applyId") Integer applyId );

    /**
     * 根据ApplyId更新联系人申请信息
     */
    Integer updateByApplyId( @Param("bean") T t, @Param("applyId") Integer applyId );

    /**
     * 根据ApplyId删除联系人申请信息
     */
    Integer deleteByApplyId( @Param("applyId") Integer applyId );

    /**
     * 根据ApplyUserIdAndContactId查询联系人申请信息
     */
    T selectByApplyUserIdAndContactId( @Param("applyUserId") String applyUserId, @Param("contactId") String contactId );

    /**
     * 根据ApplyUserIdAndContactId更新联系人申请信息
     */
    Integer updateByApplyUserIdAndContactId( @Param("bean") T t, @Param("applyUserId") String applyUserId, @Param("contactId") String contactId );

    /**
     * 根据ApplyUserIdAndContactId删除联系人申请信息
     */
    Integer deleteByApplyUserIdAndContactId( @Param("applyUserId") String applyUserId, @Param("contactId") String contactId );
}
