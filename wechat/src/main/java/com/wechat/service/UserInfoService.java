package com.wechat.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wechat.entity.po.UserInfo;
import com.wechat.entity.query.UserInfoQuery;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.UserInfoVO;
import com.wechat.exception.BussinessException;

/**
 * @Description: 用户信息Service
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
public interface UserInfoService {

    /**
     * 根据条件查询列表
     */
    List<UserInfo> findListByParam(UserInfoQuery query);

    /**
     * 根据条件查询数量
     */
    Integer findCountByParam(UserInfoQuery query);

    /**
     * 分页查询
     */
    PaginationResultVO<UserInfo> findCountByPage(UserInfoQuery query);

    /**
     * 新增
     */
    Integer add(UserInfo bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserInfo> beans);

    /**
     * 批量新增或更新
     */
    Integer addOrUpdateBatch(List<UserInfo> beans);

    /**
     * 根据UseId查询用户信息
     */
    UserInfo getByUseId(String useId );

    /**
     * 根据UseId更新用户信息
     */
    Integer updateByUseId( UserInfo t,String useId );

    /**
     * 根据UseId删除用户信息
     */
    Integer deleteByUseId(String useId );

    /**
     * 根据Email查询用户信息
     */
    UserInfo getByEmail(String email );

    /**
     * 根据Email更新用户信息
     */
    Integer updateByEmail( UserInfo t,String email );

    /**
     * 根据Email删除用户信息
     */
    Integer deleteByEmail(String email );
    /**
     * 注册用户信息
     */
    void register( String email, String password, String nickname) throws Exception;

    /**
     * 登录
     * @throws BussinessException 
     */
    UserInfoVO login(String email, String password) throws Exception;

    /**
     * 修改信息
     */
    void updateInfo(UserInfo userInfo,MultipartFile avatarFile,MultipartFile avatarCover) throws Exception;

    /**
     * 更新状态
     */
    void updateUserStatus(String useId, Integer status) throws Exception;

    /**
     * 强制下线
     */
    void forceOffline(String userId) throws Exception;
}
