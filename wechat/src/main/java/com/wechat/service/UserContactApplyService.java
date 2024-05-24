package com.wechat.service;

import java.util.List;

import com.wechat.entity.po.UserContactApply;
import com.wechat.entity.query.UserContactApplyQuery;
import com.wechat.entity.vo.PaginationResultVO;

/**
 * @Description: 联系人申请信息Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public interface UserContactApplyService {

    /**
     * 根据条件查询列表
     */
    List<UserContactApply> findListByParam(UserContactApplyQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(UserContactApplyQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<UserContactApply> findCountByPage(UserContactApplyQuery query);

    /**
     * 新增
     */
    Integer add(UserContactApply bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserContactApply> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<UserContactApply> beans);

    /**
     * 根据ApplyId查询联系人申请信息
     */
    UserContactApply getByApplyId(Integer applyId );

    /**
     * 根据ApplyId更新联系人申请信息
     */
    Integer updateByApplyId( UserContactApply t,Integer applyId );

    /**
     * 根据ApplyId删除联系人申请信息
     */
    Integer deleteByApplyId(Integer applyId );

     /**
     * 根据ApplyUserIdAndContactId查询联系人申请信息
     */
    UserContactApply getByApplyUserIdAndContactId(String applyUserId,String contactId );

    /**
     * 根据ApplyUserIdAndContactId更新联系人申请信息
     */
    Integer updateByApplyUserIdAndContactId( UserContactApply t,String applyUserId,String contactId );

    /**
     * 根据ApplyUserIdAndContactId删除联系人申请信息
     */
    Integer deleteByApplyUserIdAndContactId(String applyUserId,String contactId );
    /**
     * 处理申请
     */
    void dealWithApply(String userId,Integer applyId, Integer status) throws Exception ;

    /*
     * 添加联系人申请信息
     */
    void addContact(String applyUserId, String contactId , String receiveUserId,Integer contactType,String applyInfo) throws Exception;
}
