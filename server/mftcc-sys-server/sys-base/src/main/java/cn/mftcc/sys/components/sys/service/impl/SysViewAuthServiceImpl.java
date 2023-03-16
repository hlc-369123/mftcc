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

import cn.mftcc.sys.components.sys.entity.SysViewAuthEntity;
import cn.mftcc.sys.components.sys.mapper.SysViewAuthMapper;
import cn.mftcc.sys.components.sys.service.SysViewAuthService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 视角权限配置
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 15:00:10
 */
@Service("sysViewAuthService")
@Transactional(rollbackFor=Exception.class)
public class SysViewAuthServiceImpl implements SysViewAuthService {

    @Autowired
    private SysViewAuthMapper sysViewAuthMapper;
    @Override
    public IPage<SysViewAuthEntity> findByPage(SysViewAuthEntity sysViewAuthEntity) {
        //翻页
        IPage<SysViewAuthEntity> page = new Page<>();
        page.setCurrent(sysViewAuthEntity.getPageNo());
        page.setSize(sysViewAuthEntity.getPageSize());
        QueryWrapper<SysViewAuthEntity> queryWrapper = new QueryWrapper<>();


        return sysViewAuthMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysViewAuthEntity sysViewAuthEntity) {
        sysViewAuthMapper.insert(sysViewAuthEntity);
    }

    @Override
    public void update(SysViewAuthEntity sysViewAuthEntity) {
        sysViewAuthMapper.updateById(sysViewAuthEntity);
    }

    @Override
    public SysViewAuthEntity findById(String id) {
        return sysViewAuthMapper.selectById(id);
    }

    @Override
    public void deleteById(String id) {
            sysViewAuthMapper.deleteById(id);
    }

}