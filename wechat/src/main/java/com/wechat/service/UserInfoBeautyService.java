package com.wechat.service;

import java.util.List;

import com.wechat.entity.po.UserInfoBeauty;
import com.wechat.entity.query.UserInfoBeautyQuery;
import com.wechat.entity.vo.PaginationResultVO;

/**
 * @Description: 靓号信息Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
public interface UserInfoBeautyService {

    /**
     * 根据条件查询列表
     */
    List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(UserInfoBeautyQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<UserInfoBeauty> findCountByPage(UserInfoBeautyQuery query);

    /**
     * 新增
     */
    Integer add(UserInfoBeauty bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserInfoBeauty> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<UserInfoBeauty> beans);

    /**
     * 根据Id查询靓号信息
     */
    UserInfoBeauty getById(Integer id );

    /**
     * 根据Id更新靓号信息
     */
    Integer updateById( UserInfoBeauty t,Integer id );

    /**
     * 根据Id删除靓号信息
     */
    Integer deleteById(Integer id );

    /**
     * 根据UseId查询靓号信息
     */
    UserInfoBeauty getByUseId(String useId );

    /**
     * 根据UseId更新靓号信息
     */
    Integer updateByUseId( UserInfoBeauty t,String useId );

    /**
     * 根据UseId删除靓号信息
     */
    Integer deleteByUseId(String useId );

    /**
     * 根据Email查询靓号信息
     */
    UserInfoBeauty getByEmail(String email );

    /**
     * 根据Email更新靓号信息
     */
    Integer updateByEmail( UserInfoBeauty t,String email );

    /**
     * 根据Email删除靓号信息
     */
    Integer deleteByEmail(String email );

    /**
     * 保存靓号信息
     */
    void saveBeautAccount(UserInfoBeauty userInfoBeauty) throws Exception;
}
