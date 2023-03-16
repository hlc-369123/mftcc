/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.mftcc.common.utils.MapperUtil;
import org.springframework.transaction.annotation.Transactional;

import cn.mftcc.sys.components.demo.entity.DemoPersonJobEntity;
import cn.mftcc.sys.components.demo.mapper.DemoPersonJobMapper;
import cn.mftcc.sys.components.demo.service.DemoPersonJobService;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

/**
 *
 *
 * @author guohanchen
 * @email
 * @date 2020-11-16 17:16:55
 */
@Service("demoPersonJobService")
@Transactional(rollbackFor = ServiceException.class)
public class DemoPersonJobServiceImpl implements DemoPersonJobService {

    @Autowired
    private DemoPersonJobMapper demoPersonJobMapper;
    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public IPage<DemoPersonJobEntity> findByPage(DemoPersonJobEntity demoPersonJobEntity) throws ServiceException {
        try{
            //翻页
            IPage<DemoPersonJobEntity> page = new Page<>();
            page.setCurrent(demoPersonJobEntity.getPageNo());
            page.setSize(demoPersonJobEntity.getPageSize());
            QueryWrapper<DemoPersonJobEntity> queryWrapper = new QueryWrapper<>();
            mapperUtil.tableQuery(queryWrapper,demoPersonJobEntity);
            return demoPersonJobMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,demoPersonJobEntity.getWorkId(),e);
        }
    }

    @Override
    public void insert(DemoPersonJobEntity demoPersonJobEntity) throws ServiceException {
        try{
            demoPersonJobMapper.insert(demoPersonJobEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,demoPersonJobEntity.getWorkId(),e);
        }
    }

    @Override
    public void update(DemoPersonJobEntity demoPersonJobEntity) throws ServiceException {
        try{
            demoPersonJobMapper.updateById(demoPersonJobEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,demoPersonJobEntity.getWorkId(),e);
        }
    }

    @Override
    public DemoPersonJobEntity findById(String workId) throws ServiceException {
        try{
            return demoPersonJobMapper.selectById(workId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,workId,e);
        }
    }

    @Override
    public void deleteById(String workId) throws ServiceException {
        try{
            demoPersonJobMapper.deleteById(workId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,workId,e);
        }
    }

}
