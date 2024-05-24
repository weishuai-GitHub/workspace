package com.wechat.service;

import java.util.List;

import com.wechat.entity.po.AppUpdate;
import com.wechat.entity.query.AppUpdateQuery;
import com.wechat.entity.vo.PaginationResultVO;

import jakarta.validation.constraints.NotNull;

/**
 * @Description: app发布Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/19
 */
public interface AppUpdateService {

    /**
     * 根据条件查询列表
     */
    List<AppUpdate> findListByParam(AppUpdateQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(AppUpdateQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<AppUpdate> findCountByPage(AppUpdateQuery query);

    /**
     * 新增
     */
    Integer add(AppUpdate bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<AppUpdate> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<AppUpdate> beans);

    /**
     * 根据Id查询app发布
     */
    AppUpdate getById(Integer id );

    /**
     * 根据Id更新app发布
     */
    Integer updateById( AppUpdate t,Integer id );

    /**
     * 根据Id删除app发布
     * @throws Exception 
     */
    Integer deleteById(Integer id ) throws Exception;

    /**
     * 根据Version查询app发布
     */
    AppUpdate getByVersion(String version );

    /**
     * 根据Version更新app发布
     */
    Integer updateByVersion( AppUpdate t,String version );

    /**
     * 根据Version删除app发布
     */
    Integer deleteByVersion(String version );

    /**
     * saveUpdate
     */
    void saveUpdate(AppUpdate appUpdate) throws Exception;


    /**
     * postUpdate
     * @throws Exception 
     */
    void postUpdate(@NotNull Integer id, @NotNull Integer status, String grayscaleUid) throws Exception;

    AppUpdate getLatestVersion(String appVersion, String uid);

}
