/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.initStyle.service.impl;

import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.common.utils.UUIDUtil;
import cn.mftcc.sys.components.initStyle.entity.SysInitStyleEntity;
import cn.mftcc.sys.components.initStyle.mapper.SysInitStyleMapper;
import cn.mftcc.sys.components.initStyle.service.SysInitStyleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 * @author guohanchen
 * @email
 * @date 2021-05-27 08:34:16
 */
@Service("sysInitStyleService")
@Transactional(rollbackFor = ServiceException.class)
public class SysInitStyleServiceImpl implements SysInitStyleService {

    @Autowired
    private SysInitStyleMapper sysInitStyleMapper;
    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public IPage<SysInitStyleEntity> findByPage(SysInitStyleEntity sysInitStyleEntity) throws ServiceException {
        try{
            //翻页
            IPage<SysInitStyleEntity> page = new Page<>();
            page.setCurrent(sysInitStyleEntity.getPageNo());
            page.setSize(sysInitStyleEntity.getPageSize());
            QueryWrapper<SysInitStyleEntity> queryWrapper = new QueryWrapper<>();
            mapperUtil.tableQuery(queryWrapper,sysInitStyleEntity);
            return sysInitStyleMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysInitStyleEntity.getId(),e);
        }
    }

    @Override
    public void insert(SysInitStyleEntity sysInitStyleEntity) throws ServiceException {
        try{
            if (sysInitStyleEntity.getId() == null) {
                sysInitStyleEntity.setId(UUIDUtil.getUUID());
            }
            sysInitStyleMapper.deleteByCompanyId(sysInitStyleEntity.getCompanyId());
            sysInitStyleMapper.insert(sysInitStyleEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysInitStyleEntity.getId(),e);
        }
    }

    @Override
    public void update(SysInitStyleEntity sysInitStyleEntity) throws ServiceException {
        try{
            sysInitStyleMapper.updateById(sysInitStyleEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysInitStyleEntity.getId(),e);
        }
    }

    @Override
    public SysInitStyleEntity findById(Integer id) throws ServiceException {
        try{
            return sysInitStyleMapper.selectById(id);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,id,e);
        }
    }

    @Override
    public void deleteById(Integer id) throws ServiceException {
        try{
            sysInitStyleMapper.deleteById(id);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,id,e);
        }
    }

    @Override
    public List<SysInitStyleEntity> findListByCompanyId(String companyId) {
        return sysInitStyleMapper.findListByCompanyId(companyId);
    }

}
