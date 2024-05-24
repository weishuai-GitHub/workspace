package com.wechat.service.impl;

import java.util.List;

import com.wechat.entity.po.ChatSessionUser;
import com.wechat.entity.query.ChatSessionUserQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.service.ChatSessionUserService;
import com.wechat.mappers.ChatSessionUserMapper;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @Description: 会话用户Service实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
@Service("chatSessionUserService")
public class ChatSessionUserServiceImpl implements ChatSessionUserService {

	@Resource
	private ChatSessionUserMapper<ChatSessionUser,ChatSessionUserQuery> chatSessionUserMapper;

    /**
     * 根据条件查询列表
     */
	@Override
    public List<ChatSessionUser> findListByParam(ChatSessionUserQuery query){
        return this.chatSessionUserMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(ChatSessionUserQuery query){
        return this.chatSessionUserMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<ChatSessionUser> findCountByPage(ChatSessionUserQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<ChatSessionUser> list = this.findListByParam(query);
		PaginationResultVO<ChatSessionUser> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(ChatSessionUser bean){
        return this.chatSessionUserMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<ChatSessionUser> beans){
		return this.chatSessionUserMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<ChatSessionUser> beans){
		return this.chatSessionUserMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据UserIdAndContactId查询会话用户
     */
	@Override
    public ChatSessionUser getByUserIdAndContactId(String userId,String contactId ){
        return this.chatSessionUserMapper.selectByUserIdAndContactId(userId,contactId);
    }

    /**
     * 根据UserIdAndContactId更新会话用户
     */
	@Override
    public Integer updateByUserIdAndContactId( ChatSessionUser t,String userId,String contactId ){
        return this.chatSessionUserMapper.updateByUserIdAndContactId(t,userId,contactId);
    }

    /**
     * 根据UserIdAndContactId删除会话用户
     */
	@Override
    public Integer deleteByUserIdAndContactId(String userId,String contactId ){
        return this.chatSessionUserMapper.deleteByUserIdAndContactId(userId,contactId);
    }

    //更新冗余信息
    public void updateRedundanceInfo(String contactName,String contactId){

    }

}
