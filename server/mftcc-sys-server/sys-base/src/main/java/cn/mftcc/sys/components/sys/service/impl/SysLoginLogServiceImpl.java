/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import cn.mftcc.sys.components.sys.entity.SysLoginLogEntity;
import cn.mftcc.sys.components.sys.mapper.SysLoginLogMapper;
import cn.mftcc.sys.components.sys.service.SysLoginLogService;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

/**
 * 系统登录日志
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-21 10:17:23
 */
@Service("sysLoginLogService")
@Transactional(rollbackFor = ServiceException.class)
public class SysLoginLogServiceImpl implements SysLoginLogService {

    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;
    @Override
    public IPage<SysLoginLogEntity> findByPage(SysLoginLogEntity sysLoginLogEntity) throws ServiceException {
        try{
            //翻页
            IPage<SysLoginLogEntity> page = new Page<>();
            page.setCurrent(sysLoginLogEntity.getPageNo());
            page.setSize(sysLoginLogEntity.getPageSize());
            QueryWrapper<SysLoginLogEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
            queryWrapper
                .eq(StringUtils.isNotBlank(sysLoginLogEntity.getToken()),"token",sysLoginLogEntity.getToken())
                .eq(StringUtils.isNotBlank(sysLoginLogEntity.getLoginIp()),"login_ip",sysLoginLogEntity.getLoginIp())
                .eq(StringUtils.isNotBlank(sysLoginLogEntity.getOpNo()),"op_no",sysLoginLogEntity.getOpNo())
                .eq(StringUtils.isNotBlank(sysLoginLogEntity.getOpName()),"op_name",sysLoginLogEntity.getOpName());


            return sysLoginLogMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysLoginLogEntity.getToken(),e);
        }
    }

    @Override
    public void insert(SysLoginLogEntity sysLoginLogEntity) throws ServiceException {
        try{
            sysLoginLogMapper.insert(sysLoginLogEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysLoginLogEntity.getToken(),e);
        }
    }

    @Override
    public void update(SysLoginLogEntity sysLoginLogEntity) throws ServiceException {
        try{
            sysLoginLogMapper.updateById(sysLoginLogEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysLoginLogEntity.getToken(),e);
        }
    }

    @Override
    public SysLoginLogEntity findById(String token) throws ServiceException {
        try{
            return sysLoginLogMapper.selectById(token);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,token,e);
        }
    }

    @Override
    public void deleteById(String token) throws ServiceException {
        try{
            sysLoginLogMapper.deleteById(token);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,token,e);
        }
    }

}