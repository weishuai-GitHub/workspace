package com.wechat.service;

import java.util.List;

import com.wechat.entity.po.ChatSessionUser;
import com.wechat.entity.query.ChatSessionUserQuery;
import com.wechat.entity.vo.PaginationResultVO;

/**
 * @Description: 会话用户Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public interface ChatSessionUserService {

    /**
     * 根据条件查询列表
     */
    List<ChatSessionUser> findListByParam(ChatSessionUserQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(ChatSessionUserQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<ChatSessionUser> findCountByPage(ChatSessionUserQuery query);

    /**
     * 新增
     */
    Integer add(ChatSessionUser bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<ChatSessionUser> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<ChatSessionUser> beans);

    /**
     * 根据UserIdAndContactId查询会话用户
     */
    ChatSessionUser getByUserIdAndContactId(String userId,String contactId );

    /**
     * 根据UserIdAndContactId更新会话用户
     */
    Integer updateByUserIdAndContactId( ChatSessionUser t,String userId,String contactId );

    /**
     * 根据UserIdAndContactId删除会话用户
     */
    Integer deleteByUserIdAndContactId(String userId,String contactId );


    //更新冗余信息
    void updateRedundanceInfo(String contactName,String contactId);

}
