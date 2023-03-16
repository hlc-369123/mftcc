/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.sys.components.sys.entity.SysParmTransEntity;
import cn.mftcc.sys.components.sys.mapper.SysParmTransMapper;
import cn.mftcc.sys.components.sys.service.SysParmTransService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;


import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.constant.SysConstant;

import java.util.List;

/**
 * 字典项转换
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-07-22 16:29:26
 */
@Service("sysParmTransService")
@Transactional(rollbackFor = ServiceException.class)
public class SysParmTransServiceImpl implements SysParmTransService {

    @Autowired
    private SysParmTransMapper sysParmTransMapper;
    @Override
    public IPage<SysParmTransEntity> findByPage(SysParmTransEntity sysParmTransEntity) throws ServiceException {
        try{
            //翻页
            IPage<SysParmTransEntity> page = new Page<>();
            page.setCurrent(sysParmTransEntity.getPageNo());
            page.setSize(sysParmTransEntity.getPageSize());
            QueryWrapper<SysParmTransEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
            queryWrapper
                .eq(StringUtils.isNotBlank(sysParmTransEntity.getParmKey()),"parm_key",sysParmTransEntity.getParmKey())
                .eq(StringUtils.isNotBlank(sysParmTransEntity.getParmCode()),"parm_code",sysParmTransEntity.getParmCode())
                .eq(StringUtils.isNotBlank(sysParmTransEntity.getTransSysId()),"trans_sys_id",sysParmTransEntity.getTransSysId());


            return sysParmTransMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,sysParmTransEntity.getId(),e);
        }
    }

    @Override
    public void insert(SysParmTransEntity sysParmTransEntity) throws ServiceException {
        try{
            sysParmTransMapper.insert(sysParmTransEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysParmTransEntity.getId(),e);
        }
    }

    @Override
    public void update(SysParmTransEntity sysParmTransEntity) throws ServiceException {
        try{
            sysParmTransMapper.updateById(sysParmTransEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,sysParmTransEntity.getId(),e);
        }
    }

    @Override
    public SysParmTransEntity findById(String id) throws ServiceException {
        try{
            return sysParmTransMapper.selectById(id);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,id,e);
        }
    }

    @Override
    public void deleteById(String id) throws ServiceException {
        try{
            sysParmTransMapper.deleteById(id);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,id,e);
        }
    }

    @Override
    public List<SysParmTransEntity> findAll(){
        QueryWrapper<SysParmTransEntity> queryWrapper = new QueryWrapper<>();
        return sysParmTransMapper.selectList(queryWrapper);
    }
}
