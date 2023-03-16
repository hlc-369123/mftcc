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

import cn.mftcc.sys.components.sys.entity.SysModelAuthEntity;
import cn.mftcc.sys.components.sys.mapper.SysModelAuthMapper;
import cn.mftcc.sys.components.sys.service.SysModelAuthService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能权限配置
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 16:48:50
 */
@Service("sysModelAuthService")
@Transactional(rollbackFor=Exception.class)
public class SysModelAuthServiceImpl implements SysModelAuthService {

    @Autowired
    private SysModelAuthMapper sysModelAuthMapper;
    @Override
    public IPage<SysModelAuthEntity> findByPage(SysModelAuthEntity sysModelAuthEntity) {
        //翻页
        IPage<SysModelAuthEntity> page = new Page<>();
        page.setCurrent(sysModelAuthEntity.getPageNo());
        page.setSize(sysModelAuthEntity.getPageSize());
        QueryWrapper<SysModelAuthEntity> queryWrapper = new QueryWrapper<>();


        return sysModelAuthMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysModelAuthEntity sysModelAuthEntity) {
        sysModelAuthMapper.insert(sysModelAuthEntity);
    }

    @Override
    public void update(SysModelAuthEntity sysModelAuthEntity) {
        sysModelAuthMapper.updateById(sysModelAuthEntity);
    }

    @Override
    public SysModelAuthEntity findById(String id) {
        return sysModelAuthMapper.selectById(id);
    }

    @Override
    public List<String> findRoleIdByModelId(String modelId) {
        return sysModelAuthMapper.findRoleIdByModelId(modelId);
    }

    @Override
    public void deleteById(String id) {
            sysModelAuthMapper.deleteById(id);
    }

}