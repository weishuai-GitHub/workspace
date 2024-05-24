package com.wechat.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wechat.entity.dto.MessageSendDto;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.po.ChatMeaasge;
import com.wechat.entity.query.ChatMeaasgeQuery;
import com.wechat.entity.vo.PaginationResultVO;


/**
 * @Description: 聊天消息表Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/20
 */
public interface ChatMeaasgeService {

    /**
     * 根据条件查询列表
     */
    List<ChatMeaasge> findListByParam(ChatMeaasgeQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(ChatMeaasgeQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<ChatMeaasge> findCountByPage(ChatMeaasgeQuery query);

    /**
     * 新增
     */
    Integer add(ChatMeaasge bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<ChatMeaasge> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<ChatMeaasge> beans);

    /**
     * 根据MessageId查询聊天消息表
     */
    ChatMeaasge getByMessageId(Long messageId );

    /**
     * 根据MessageId更新聊天消息表
     */
    Integer updateByMessageId( ChatMeaasge t,Long messageId );

    /**
     * 根据MessageId删除聊天消息表
     */
    Integer deleteByMessageId(Long messageId );

    @SuppressWarnings("rawtypes")
    MessageSendDto saveMessage(ChatMeaasge chatMeaasge,TokenUserInfo tokenUserInfo) throws Exception;

    void saveMessageFile(String userId, Long messageId,  MultipartFile file,
            MultipartFile cover) throws Exception;

    File downloadFile(TokenUserInfo tokenUserInfo, long long1, Boolean showCover) throws Exception;

}
