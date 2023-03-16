/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.sys.components.sys.entity.SysMenuEntity;
import cn.mftcc.sys.components.sys.mapper.SysMenuMapper;
import cn.mftcc.sys.components.sys.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统菜单表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-05 12:11:55
 */
@Service("sysMenuService")
@Transactional(rollbackFor=Exception.class)
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Override
    public IPage<SysMenuEntity> findByPage(SysMenuEntity sysMenuEntity) {
        //翻页
        IPage<SysMenuEntity> page = new Page<>();
        page.setCurrent(sysMenuEntity.getPageNo());
        page.setSize(sysMenuEntity.getPageSize());
        QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<>();

        return sysMenuMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysMenuEntity sysMenuEntity) {
        sysMenuMapper.insert(sysMenuEntity);
    }

    @Override
    public void update(SysMenuEntity sysMenuEntity) {
        sysMenuMapper.updateById(sysMenuEntity);
    }

    @Override
    public SysMenuEntity findById(String menuId) {
        return sysMenuMapper.selectById(menuId);
    }

    @Override
    public void deleteById(String menuId) {
        sysMenuMapper.deleteById(menuId);
    }

    @Override
    public List<SysMenuEntity> findAllList() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("up_menu_id","sn","menu_id");
        return sysMenuMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysMenuEntity> findListByRoleIds(List<String> roleIds) {
        return sysMenuMapper.findListByRoleIds(roleIds);
    }
}