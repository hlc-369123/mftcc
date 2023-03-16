/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import cn.mftcc.sys.components.sys.entity.SysMenuEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 系统菜单表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-05 12:11:55
 */
public interface SysMenuService {

    IPage<SysMenuEntity> findByPage(SysMenuEntity sysMenuEntity);

    void insert(SysMenuEntity sysMenuEntity);

    void update(SysMenuEntity sysMenuEntity);

    SysMenuEntity findById(String menuId);

    void deleteById(String menuId);

    List<SysMenuEntity> findAllList();

    List<SysMenuEntity> findListByRoleIds(List<String> roleIds);
}

