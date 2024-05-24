package com.wechat.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.wechat.entity.config.AppConfig;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.enums.AppUpdateFileTypeEnums;
import com.wechat.entity.enums.AppUpdateStatusEnums;
import com.wechat.entity.po.AppUpdate;
import com.wechat.entity.query.AppUpdateQuery;
import com.wechat.entity.query.SimplePage;
import com.wechat.entity.vo.PaginationResultVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.exception.BussinessException;
import com.wechat.service.AppUpdateService;

import com.wechat.mappers.AppUpdateMapper;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * @Description: app发布Service实现类
 *
 * @author: ShuaiWei
 * @date: 2024/05/19
 */
@Service("appUpdateService")
public class AppUpdateServiceImpl implements AppUpdateService {

	@Resource
	private AppUpdateMapper<AppUpdate,AppUpdateQuery> appUpdateMapper;

    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
	@Override
    public List<AppUpdate> findListByParam(AppUpdateQuery query){
        return this.appUpdateMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
	@Override
    public Integer findCountByParam(AppUpdateQuery query){
        return this.appUpdateMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
	@Override
    public PaginationResultVO<AppUpdate> findCountByPage(AppUpdateQuery query){
		Integer count = this.findCountByParam(query);
		int pageSize = query.getPageSize() == null? SimplePage.SIZE15:query.getPageSize();
		SimplePage simplePage = new SimplePage(query.getPageNo(),count,pageSize);
		query.setSimplePage(simplePage);
		List<AppUpdate> list = this.findListByParam(query);
		PaginationResultVO<AppUpdate> result = new PaginationResultVO<>(count,simplePage.getPageSize(),simplePage.getPageNo(),simplePage.getPageTotal(),list);
        return result;
    }

    /**
     * 新增
     */
	@Override
    public Integer add(AppUpdate bean){
        return this.appUpdateMapper.insert(bean);
    }

    /**
     * 批量新增
     */
	@Override
    public Integer addBatch(List<AppUpdate> beans){
		return this.appUpdateMapper.insertBatch(beans);
    }

    /**
     * 批量新增或更新
     */
	@Override
    public Integer addOrUpdateBatch(List<AppUpdate> beans){
		return this.appUpdateMapper.insertOrUpdateBatch(beans);
    }

    /**
     * 根据Id查询app发布
     */
	@Override
    public AppUpdate getById(Integer id ){
        return this.appUpdateMapper.selectById(id);
    }

    /**
     * 根据Id更新app发布
     */
	@Override
    public Integer updateById( AppUpdate t,Integer id ){
        return this.appUpdateMapper.updateById(t,id);
    }

    /**
     * 根据Id删除app发布
     * @throws BussinessException 
     */
	@Override
    public Integer deleteById(Integer id ) throws Exception{
        AppUpdate appUpdateDb = this.getById(id);
        if(appUpdateDb.getStatus() != AppUpdateStatusEnums.INIT.getStatus()){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        return this.appUpdateMapper.deleteById(id);
    }

    /**
     * 根据Version查询app发布
     */
	@Override
    public AppUpdate getByVersion(String version ){
        return this.appUpdateMapper.selectByVersion(version);
    }

    /**
     * 根据Version更新app发布
     */
	@Override
    public Integer updateByVersion( AppUpdate t,String version ){
        return this.appUpdateMapper.updateByVersion(t,version);
    }

    /**
     * 根据Version删除app发布
     */
	@Override
    public Integer deleteByVersion(String version ){
        return this.appUpdateMapper.deleteByVersion(version);
    }
    /**
     * saveUpdate
     */
    @Override
    public void saveUpdate(AppUpdate appUpdate) throws Exception{
        AppUpdateFileTypeEnums fileTypeEnums = AppUpdateFileTypeEnums.getByType(appUpdate.getFileType());
        if(fileTypeEnums == null){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }

        if(appUpdate.getId()!=null){
            AppUpdate appUpdateDb = this.getById(appUpdate.getId());
            if(appUpdateDb.getStatus() != AppUpdateStatusEnums.INIT.getStatus()){
                throw new BussinessException(ResponseCodeEnum.CODE_600);
            }
        }
        AppUpdateQuery query = new AppUpdateQuery();
        query.setOrderBy("id desc");
        // query.setSimplePage(new SimplePage(0,1));
        List<AppUpdate> list = this.appUpdateMapper.selectList(query);
        if(list != null && list.size() > 0){
            AppUpdate lastAppUpdate = list.get(0);
            Long lastVersion = Long.parseLong(lastAppUpdate.getVersion().replace(".",""));
            Long currentVersion = Long.parseLong(appUpdate.getVersion().replace(".",""));
            if(appUpdate.getId()==null && currentVersion <= lastVersion){
                throw new BussinessException("版本号必须大于"+lastAppUpdate.getVersion());
            }

            if(appUpdate.getId()!=null && currentVersion <= lastVersion &&!appUpdate.getVersion().equals(lastAppUpdate.getVersion())){
                throw new BussinessException("版本号必须大于"+lastAppUpdate.getVersion());
            }

            AppUpdate versionDb = this.getByVersion(appUpdate.getVersion());
            if(versionDb != null && !versionDb.getId().equals(appUpdate.getId())){
                throw new BussinessException("版本号已存在");
            }
        }
        if(appUpdate.getId()==null)
        {
            appUpdate.setCreateTime(new Date());
            appUpdate.setStatus(AppUpdateStatusEnums.INIT.getStatus());
            this.add(appUpdate);
        }
        else
        {
            appUpdate.setStatus(null);
            this.updateById(appUpdate,appUpdate.getId());
        }
        if(appUpdate.getFileName() != null){
            File folder = new File(appConfig.getProjectFolder()+ Constans.FILE_FOLDER_FILE);
            if(!folder.exists()){
                folder.mkdirs();
            }
            // appUpdate.getFileName().transferTo(new File(folder.getAbsolutePath() + "/" + appUpdate.getId() + Constans.APP_EXE_SUFFIX));
        }
    }

    @Override
    public void postUpdate(Integer id, Integer status, String grayscaleUid) throws Exception {
        AppUpdateStatusEnums statusEnums = AppUpdateStatusEnums.getByStatus(status);
        if(statusEnums == null){
            throw new BussinessException(ResponseCodeEnum.CODE_600);
        }
        
        if(AppUpdateStatusEnums.GRAYSACLE == statusEnums && grayscaleUid == null || "".equals(grayscaleUid)){
            throw new BussinessException("灰度发布用户不能为空");
        }

        if(AppUpdateStatusEnums.GRAYSACLE != statusEnums){
            grayscaleUid = "";
        }

        AppUpdate appUpdate  = new AppUpdate();
        appUpdate.setGrayscaleUid(grayscaleUid);
        appUpdate.setStatus(status);
        this.updateById(appUpdate,id);
    }

    @Override
    public AppUpdate getLatestVersion(String appVersion, String uid) {
        return appUpdateMapper.selectLatestVersion(appVersion, uid);
    }

}
