package com.wechat.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.enums.MessageTypeEnums;
import com.wechat.entity.po.GroupInfo;
import com.wechat.entity.query.GroupInfoQuery;
import com.wechat.entity.vo.PaginationResultVO;

/**
 * @Description: 群组信息Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public interface GroupInfoService {

    /**
     * 根据条件查询列表
     */
    List<GroupInfo> findListByParam(GroupInfoQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(GroupInfoQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<GroupInfo> findCountByPage(GroupInfoQuery query);

    /**
     * 新增
     */
    Integer add(GroupInfo bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<GroupInfo> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<GroupInfo> beans);

    /**
     * 根据GroupId查询群组信息
     */
    GroupInfo getByGroupId(String groupId );

    /**
     * 根据GroupId更新群组信息
     */
    Integer updateByGroupId( GroupInfo t,String groupId );

    /**
     * 根据GroupId删除群组信息
     */
    Integer deleteByGroupId(String groupId );


    /**
     * 保存群组信息
     */
    void saveGroup(GroupInfo groupInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws Exception;

    /**
     * 解散群组
     */
    void dissolutionGroup(String groupOwerId, String groupId) throws Exception;

    void addOrRemoveGroupUser(TokenUserInfo tokenUserInfo,String groupId,String selectContacts,
            Integer opType) throws Exception;

    void leafGroup(String contactId, String groupId,MessageTypeEnums messageTypeEnums) throws Exception;
}
