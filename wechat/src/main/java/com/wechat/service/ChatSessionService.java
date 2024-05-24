package com.wechat.service;

import java.util.List;

import com.wechat.entity.po.ChatSession;
import com.wechat.entity.query.ChatSessionQuery;
import com.wechat.entity.vo.PaginationResultVO;

/**
 * @Description: 会话IDService
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public interface ChatSessionService {

    /**
     * 根据条件查询列表
     */
    List<ChatSession> findListByParam(ChatSessionQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(ChatSessionQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<ChatSession> findCountByPage(ChatSessionQuery query);

    /**
     * 新增
     */
    Integer add(ChatSession bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<ChatSession> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<ChatSession> beans);

    /**
     * 根据SessionId查询会话ID
     */
    ChatSession getBySessionId(String sessionId );

    /**
     * 根据SessionId更新会话ID
     */
    Integer updateBySessionId( ChatSession t,String sessionId );

    /**
     * 根据SessionId删除会话ID
     */
    Integer deleteBySessionId(String sessionId );

}
