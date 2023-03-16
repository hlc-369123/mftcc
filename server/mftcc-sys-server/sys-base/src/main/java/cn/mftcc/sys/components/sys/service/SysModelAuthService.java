/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysModelAuthEntity;

import java.util.List;

/**
 * 功能权限配置
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 16:48:50
 */
public interface SysModelAuthService {

    IPage<SysModelAuthEntity> findByPage(SysModelAuthEntity sysModelAuthEntity);

    void insert(SysModelAuthEntity sysModelAuthEntity);

    void update(SysModelAuthEntity sysModelAuthEntity);

    SysModelAuthEntity findById(String id);

    List<String> findRoleIdByModelId(String modelId);

    void deleteById(String id);
}

