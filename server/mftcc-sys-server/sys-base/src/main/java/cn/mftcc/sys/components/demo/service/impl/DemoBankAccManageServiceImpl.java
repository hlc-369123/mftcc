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

import cn.mftcc.sys.components.demo.entity.DemoBankAccManageEntity;
import cn.mftcc.sys.components.demo.mapper.DemoBankAccManageMapper;
import cn.mftcc.sys.components.demo.service.DemoBankAccManageService;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

/**
 *
 *
 * @author guohanchen
 * @email
 * @date 2020-11-16 17:02:01
 */
@Service("demoBankAccManageService")
@Transactional(rollbackFor = ServiceException.class)
public class DemoBankAccManageServiceImpl implements DemoBankAccManageService {

    @Autowired
    private DemoBankAccManageMapper demoBankAccManageMapper;
    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public IPage<DemoBankAccManageEntity> findByPage(DemoBankAccManageEntity demoBankAccManageEntity) throws ServiceException {
        try{
            //翻页
            IPage<DemoBankAccManageEntity> page = new Page<>();
            page.setCurrent(demoBankAccManageEntity.getPageNo());
            page.setSize(demoBankAccManageEntity.getPageSize());
            QueryWrapper<DemoBankAccManageEntity> queryWrapper = new QueryWrapper<>();
            mapperUtil.tableQuery(queryWrapper,demoBankAccManageEntity);
            return demoBankAccManageMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,demoBankAccManageEntity.getAccountNo(),e);
        }
    }

    @Override
    public void insert(DemoBankAccManageEntity demoBankAccManageEntity) throws ServiceException {
        try{
            demoBankAccManageMapper.insert(demoBankAccManageEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,demoBankAccManageEntity.getAccountNo(),e);
        }
    }

    @Override
    public void update(DemoBankAccManageEntity demoBankAccManageEntity) throws ServiceException {
        try{
            demoBankAccManageMapper.updateById(demoBankAccManageEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,demoBankAccManageEntity.getAccountNo(),e);
        }
    }

    @Override
    public DemoBankAccManageEntity findById(String accountNo) throws ServiceException {
        try{
            return demoBankAccManageMapper.selectById(accountNo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,accountNo,e);
        }
    }

    @Override
    public void deleteById(String accountNo) throws ServiceException {
        try{
            demoBankAccManageMapper.deleteById(accountNo);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,accountNo,e);
        }
    }

}
