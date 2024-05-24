package com.wechat.service.impl;

import java.util.List;

import com.wechat.entity.enums.BeautyAccountEnums;
import com.wechat.entity.po.UserInfo;
import com.wechat.entity.po.UserInfoBeauty;
import com.wechat.entity.query.UserInfoBeautyQuery;
import com.wechat.entity.query.UserInfoQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.exception.BussinessException;
import com.wechat.service.UserInfoBeautyService;
import com.wechat.mappers.UserInfoBeautyMapper;
import com.wechat.mappers.UserInfoMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 靓号信息Service实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
@Service("userInfoBeautyService")
public class UserInfoBeautyServiceImpl implements UserInfoBeautyService {

	@Resource
	private UserInfoBeautyMapper<UserInfoBeauty,UserInfoBeautyQuery> userInfoBeautyMapper;

    @Resource
	private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;
    /**
     * 根据条件查询列表
     */
	@Override
    public List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery query){
        return this.userInfoBeautyMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(UserInfoBeautyQuery query){
        return this.userInfoBeautyMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<UserInfoBeauty> findCountByPage(UserInfoBeautyQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<UserInfoBeauty> list = this.findListByParam(query);
		PaginationResultVO<UserInfoBeauty> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(UserInfoBeauty bean){
        return this.userInfoBeautyMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<UserInfoBeauty> beans){
		return this.userInfoBeautyMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<UserInfoBeauty> beans){
		return this.userInfoBeautyMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据Id查询靓号信息
     */
	@Override
    public UserInfoBeauty getById(Integer id ){
        return this.userInfoBeautyMapper.selectById(id);
    }

    /**
     * 根据Id更新靓号信息
     */
	@Override
    public Integer updateById( UserInfoBeauty t,Integer id ){
        return this.userInfoBeautyMapper.updateById(t,id);
    }

    /**
     * 根据Id删除靓号信息
     */
	@Override
    public Integer deleteById(Integer id ){
        return this.userInfoBeautyMapper.deleteById(id);
    }

    /**
     * 根据UseId查询靓号信息
     */
	@Override
    public UserInfoBeauty getByUseId(String useId ){
        return this.userInfoBeautyMapper.selectByUseId(useId);
    }

    /**
     * 根据UseId更新靓号信息
     */
	@Override
    public Integer updateByUseId( UserInfoBeauty t,String useId ){
        return this.userInfoBeautyMapper.updateByUseId(t,useId);
    }

    /**
     * 根据UseId删除靓号信息
     */
	@Override
    public Integer deleteByUseId(String useId ){
        return this.userInfoBeautyMapper.deleteByUseId(useId);
    }

    /**
     * 根据Email查询靓号信息
     */
	@Override
    public UserInfoBeauty getByEmail(String email ){
        return this.userInfoBeautyMapper.selectByEmail(email);
    }

    /**
     * 根据Email更新靓号信息
     */
	@Override
    public Integer updateByEmail( UserInfoBeauty t,String email ){
        return this.userInfoBeautyMapper.updateByEmail(t,email);
    }

    /**
     * 根据Email删除靓号信息
     */
	@Override
    public Integer deleteByEmail(String email ){
        return this.userInfoBeautyMapper.deleteByEmail(email);
    }

    /**
     * 保存靓号信息
     * @throws BussinessException 
     */
    @SuppressWarnings("null")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBeautAccount(UserInfoBeauty userInfoBeauty) throws Exception {
        if(userInfoBeauty.getId() != null){
            UserInfoBeauty beautyAccount = this.getById(userInfoBeauty.getId());
            if(BeautyAccountEnums.USED.getCode().equals(beautyAccount.getStaus())){
                throw new  BussinessException(ResponseCodeEnum.CODE_600);
            }
        }
        UserInfoBeauty beautyAccount = this.getByEmail(userInfoBeauty.getEmail());
        if(beautyAccount!=null && userInfoBeauty.getId()==null ){
            throw new  BussinessException("靓号邮箱已存在");
        }
        if(userInfoBeauty.getId()!=null&&beautyAccount!=null&& !userInfoBeauty.getId().equals(beautyAccount.getId())){
            throw new  BussinessException("靓号邮箱已存在");
        }

        //判断靓号是否存在
        beautyAccount = this.getByUseId(userInfoBeauty.getUseId());
        if(beautyAccount!=null && userInfoBeauty.getId()==null ){
            throw new  BussinessException("靓号邮箱已存在");
        }
        if(userInfoBeauty.getId()!=null&&beautyAccount!=null&& !userInfoBeauty.getId().equals(beautyAccount.getId())){
            throw new  BussinessException("靓号邮箱已存在");
        }
        // 判断邮箱是否注册
        UserInfo userInfo = userInfoMapper.selectByEmail(userInfoBeauty.getEmail());
        if(userInfo != null){
            throw new  BussinessException("靓号邮箱已注册");
        }

        // 判断靓号是否注册
        userInfo = userInfoMapper.selectByUseId(userInfoBeauty.getUseId());
        if(userInfo != null){
            throw new  BussinessException("靓号已注册");
        }

        if(userInfoBeauty.getId() != null){
            this.updateById(beautyAccount, beautyAccount.getId());
        }
        else{
            userInfoBeauty.setStaus(BeautyAccountEnums.NO_USE.getCode());
            this.add(userInfoBeauty);
        }
    }

}
