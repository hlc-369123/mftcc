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

import cn.mftcc.sys.components.sys.entity.SysViewEntity;
import cn.mftcc.sys.components.sys.mapper.SysViewMapper;
import cn.mftcc.sys.components.sys.service.SysViewService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 综合视图定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-16 10:44:59
 */
@Service("sysViewService")
@Transactional(rollbackFor=Exception.class)
public class SysViewServiceImpl implements SysViewService {

    @Autowired
    private SysViewMapper sysViewMapper;
    @Override
    public IPage<SysViewEntity> findByPage(SysViewEntity sysViewEntity) {
        //翻页
        IPage<SysViewEntity> page = new Page<>();
        page.setCurrent(sysViewEntity.getPageNo());
        page.setSize(sysViewEntity.getPageSize());
        QueryWrapper<SysViewEntity> queryWrapper = new QueryWrapper<>();


        return sysViewMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysViewEntity sysViewEntity) {
        sysViewMapper.insert(sysViewEntity);
    }

    @Override
    public void update(SysViewEntity sysViewEntity) {
        sysViewMapper.updateById(sysViewEntity);
    }

    @Override
    public SysViewEntity findById(String viewId) {
        return sysViewMapper.selectById(viewId);
    }

    @Override
    public void deleteById(String viewId) {
            sysViewMapper.deleteById(viewId);
    }

    @Override
    public List<SysViewEntity> findAllList() {
        return sysViewMapper.selectList(new QueryWrapper<>());
    }

}