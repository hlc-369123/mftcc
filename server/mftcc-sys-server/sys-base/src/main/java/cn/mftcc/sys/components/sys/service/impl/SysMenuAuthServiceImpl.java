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

import cn.mftcc.sys.components.sys.entity.SysMenuAuthEntity;
import cn.mftcc.sys.components.sys.mapper.SysMenuAuthMapper;
import cn.mftcc.sys.components.sys.service.SysMenuAuthService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 菜单权限表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 11:35:29
 */
@Service("sysMenuAuthService")
@Transactional(rollbackFor=Exception.class)
public class SysMenuAuthServiceImpl implements SysMenuAuthService {

    @Autowired
    private SysMenuAuthMapper sysMenuAuthMapper;
    @Override
    public IPage<SysMenuAuthEntity> findByPage(SysMenuAuthEntity sysMenuAuthEntity) {
        //翻页
        IPage<SysMenuAuthEntity> page = new Page<>();
        page.setCurrent(sysMenuAuthEntity.getPageNo());
        page.setSize(sysMenuAuthEntity.getPageSize());
        QueryWrapper<SysMenuAuthEntity> queryWrapper = new QueryWrapper<>();


        return sysMenuAuthMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysMenuAuthEntity sysMenuAuthEntity) {
        sysMenuAuthMapper.insert(sysMenuAuthEntity);
    }

    @Override
    public void update(SysMenuAuthEntity sysMenuAuthEntity) {
        sysMenuAuthMapper.updateById(sysMenuAuthEntity);
    }

    @Override
    public SysMenuAuthEntity findById(String id) {
        return sysMenuAuthMapper.selectById(id);
    }

    @Override
    public void deleteById(String id) {
            sysMenuAuthMapper.deleteById(id);
    }

}