/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service.impl;

import cn.mftcc.common.utils.DateUtil;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.components.demo.entity.DynamicModelTableEntity;
import cn.mftcc.sys.components.demo.mapper.DynamicModelTableMapper;
import cn.mftcc.sys.components.demo.service.DynamicModelTableService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.mftcc.common.utils.MapperUtil;
import org.springframework.transaction.annotation.Transactional;


import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

/**
 * 融资项目表
 *
 * @author zhanglingnan
 * @email 1670680235@qq.com
 * @date 2020-07-03 16:25:37
 */
@Service("dynamicModelTableService")
@Transactional(rollbackFor = ServiceException.class)
public class DynamicModelTableServiceImpl implements DynamicModelTableService {

    @Autowired
    private DynamicModelTableMapper dynamicModelTableMapper;
    @Autowired
    private MapperUtil mapperUtil;
    @Autowired
    private RequestUtil requestUtil;

    @Override
    public IPage<DynamicModelTableEntity> findByPage(DynamicModelTableEntity leaseFinProjectEntity) throws ServiceException {
        try{
            //翻页
            IPage<DynamicModelTableEntity> page = new Page<>();
            page.setCurrent(leaseFinProjectEntity.getPageNo());
            page.setSize(leaseFinProjectEntity.getPageSize());
            QueryWrapper<DynamicModelTableEntity> queryWrapper = new QueryWrapper<>();
//            mapperUtil.tableQuery(queryWrapper, leaseFinProjectEntity);

            return dynamicModelTableMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,leaseFinProjectEntity.getFinId(),e);
        }
    }

    @Override
    public void insert(DynamicModelTableEntity leaseFinProjectEntity) throws ServiceException {
        try{
            leaseFinProjectEntity.setRegNo(requestUtil.getUserInfo("opNo").toString());
            leaseFinProjectEntity.setRegName(requestUtil.getUserInfo("opName").toString());
            leaseFinProjectEntity.setRegDate(DateUtil.getDate());
            leaseFinProjectEntity.setOrgNo(requestUtil.getUserInfo("brNo").toString());
            leaseFinProjectEntity.setOrgName(requestUtil.getUserInfo("brName").toString());
            leaseFinProjectEntity.setLstRegNo(requestUtil.getUserInfo("opNo").toString());
            leaseFinProjectEntity.setLstRegName(requestUtil.getUserInfo("opName").toString());
            leaseFinProjectEntity.setLstOrgNo(requestUtil.getUserInfo("brNo").toString());
            leaseFinProjectEntity.setLstOrgName(requestUtil.getUserInfo("brName").toString());
            leaseFinProjectEntity.setLstDate(DateUtil.getDate());
            dynamicModelTableMapper.insert(leaseFinProjectEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,leaseFinProjectEntity.getFinId(),e);
        }
    }

    @Override
    public void update(DynamicModelTableEntity leaseFinProjectEntity) throws ServiceException {
        try{
            leaseFinProjectEntity.setLstRegNo(requestUtil.getUserInfo("opNo").toString());
            leaseFinProjectEntity.setLstRegName(requestUtil.getUserInfo("opName").toString());
            leaseFinProjectEntity.setLstOrgNo(requestUtil.getUserInfo("brNo").toString());
            leaseFinProjectEntity.setLstOrgName(requestUtil.getUserInfo("brName").toString());
            leaseFinProjectEntity.setLstDate(DateUtil.getDate());
            dynamicModelTableMapper.updateById(leaseFinProjectEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,leaseFinProjectEntity.getFinId(),e);
        }
    }

    @Override
    public DynamicModelTableEntity findById(String finId) throws ServiceException {
        try{
            return dynamicModelTableMapper.selectById(finId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,finId,e);
        }
    }

    @Override
    public void deleteById(String finId) throws ServiceException {
        try{
            dynamicModelTableMapper.deleteById(finId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,finId,e);
        }
    }

}
