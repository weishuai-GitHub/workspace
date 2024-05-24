package com.wechat.service.impl;

import java.util.List;

import com.wechat.entity.po.ChatSession;
import com.wechat.entity.query.ChatSessionQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.service.ChatSessionService;
import com.wechat.mappers.ChatSessionMapper;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @Description: 会话IDService实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
@Service("chatSessionService")
public class ChatSessionServiceImpl implements ChatSessionService {

	@Resource
	private ChatSessionMapper<ChatSession,ChatSessionQuery> chatSessionMapper;

    /**
     * 根据条件查询列表
     */
	@Override
    public List<ChatSession> findListByParam(ChatSessionQuery query){
        return this.chatSessionMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(ChatSessionQuery query){
        return this.chatSessionMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<ChatSession> findCountByPage(ChatSessionQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<ChatSession> list = this.findListByParam(query);
		PaginationResultVO<ChatSession> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(ChatSession bean){
        return this.chatSessionMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<ChatSession> beans){
		return this.chatSessionMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<ChatSession> beans){
		return this.chatSessionMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据SessionId查询会话ID
     */
	@Override
    public ChatSession getBySessionId(String sessionId ){
        return this.chatSessionMapper.selectBySessionId(sessionId);
    }

    /**
     * 根据SessionId更新会话ID
     */
	@Override
    public Integer updateBySessionId( ChatSession t,String sessionId ){
        return this.chatSessionMapper.updateBySessionId(t,sessionId);
    }

    /**
     * 根据SessionId删除会话ID
     */
	@Override
    public Integer deleteBySessionId(String sessionId ){
        return this.chatSessionMapper.deleteBySessionId(sessionId);
    }

}
