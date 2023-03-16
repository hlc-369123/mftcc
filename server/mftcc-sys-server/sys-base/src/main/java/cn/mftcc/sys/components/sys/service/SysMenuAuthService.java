/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysMenuAuthEntity;

/**
 * 菜单权限表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 11:35:29
 */
public interface SysMenuAuthService {

    IPage<SysMenuAuthEntity> findByPage(SysMenuAuthEntity sysMenuAuthEntity);

    void insert(SysMenuAuthEntity sysMenuAuthEntity);

    void update(SysMenuAuthEntity sysMenuAuthEntity);

    SysMenuAuthEntity findById(String id);

    void deleteById(String id);
}

