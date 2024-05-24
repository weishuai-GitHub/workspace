package com.wechat.service;

import java.util.List;

import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.dto.UserContactSearchResultDto;
import com.wechat.entity.enums.UserContactStatusEnums;
import com.wechat.entity.po.UserContact;
import com.wechat.entity.query.UserContactQuery;
import com.wechat.entity.vo.PaginationResultVO;

/**
 * @Description: 联系人信息Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
public interface UserContactService {

    /**
     * 根据条件查询列表
     */
    List<UserContact> findListByParam(UserContactQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(UserContactQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<UserContact> findCountByPage(UserContactQuery query);

    /**
     * 新增
     */
    Integer add(UserContact bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserContact> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<UserContact> beans);

    /**
     * 根据UserIdAndContactId查询联系人信息
     */
    UserContact getByUserIdAndContactId(String userId,String contactId );

    /**
     * 根据UserIdAndContactId更新联系人信息
     */
    Integer updateByUserIdAndContactId( UserContact t,String userId,String contactId );

    /**
     * 根据UserIdAndContactId删除联系人信息
     */
    Integer deleteByUserIdAndContactId(String userId,String contactId );

    /**
     * 根据UserId查询联系人信息
     */

    UserContactSearchResultDto searchContact(String userId, String contactId);

    /**
     * 申请添加联系人
     */
    Integer applyAdd(TokenUserInfo tokenUserInfo, String contactId, String applyInfo) throws Exception;

    /**
     * 删除联系人
     */
    Integer deleteContact(String userId, String contactId,UserContactStatusEnums statusEnums);

    void addContact4Robot(String userId);
}
