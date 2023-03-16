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

import cn.mftcc.sys.components.demo.entity.DemoCusInfoEntity;
import cn.mftcc.sys.components.demo.mapper.DemoCusInfoMapper;
import cn.mftcc.sys.components.demo.service.DemoCusInfoService;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

/**
 *
 *
 * @author guohanchen
 * @email
 * @date 2020-11-16 14:00:20
 */
@Service("demoCusInfoService")
@Transactional(rollbackFor = ServiceException.class)
public class DemoCusInfoServiceImpl implements DemoCusInfoService {

    @Autowired
    private DemoCusInfoMapper demoCusInfoMapper;
    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public IPage<DemoCusInfoEntity> findByPage(DemoCusInfoEntity demoCusInfoEntity) throws ServiceException {
        try{
            //翻页
            IPage<DemoCusInfoEntity> page = new Page<>();
            page.setCurrent(demoCusInfoEntity.getPageNo());
            page.setSize(demoCusInfoEntity.getPageSize());
            QueryWrapper<DemoCusInfoEntity> queryWrapper = new QueryWrapper<>();
            mapperUtil.tableQuery(queryWrapper,demoCusInfoEntity);
            return demoCusInfoMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,demoCusInfoEntity.getCusNo(),e);
        }
    }

    @Override
    public void insert(DemoCusInfoEntity demoCusInfoEntity) throws ServiceException {
        try{
            demoCusInfoMapper.insert(demoCusInfoEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,demoCusInfoEntity.getCusNo(),e);
        }
    }

    @Override
    public void update(DemoCusInfoEntity demoCusInfoEntity) throws ServiceException {
        try{
            demoCusInfoMapper.updateById(demoCusInfoEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,demoCusInfoEntity.getCusNo(),e);
        }
    }

    @Override
    public DemoCusInfoEntity findById(String cusno) throws ServiceException {
        try{
            return demoCusInfoMapper.selectById(cusno);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,cusno,e);
        }
    }

    @Override
    public void deleteById(String cusno) throws ServiceException {
        try{
            demoCusInfoMapper.deleteById(cusno);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,cusno,e);
        }
    }

}
