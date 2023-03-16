/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysViewAuthEntity;

/**
 * 视角权限配置
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 15:00:10
 */
public interface SysViewAuthService {

    IPage<SysViewAuthEntity> findByPage(SysViewAuthEntity sysViewAuthEntity);

    void insert(SysViewAuthEntity sysViewAuthEntity);

    void update(SysViewAuthEntity sysViewAuthEntity);

    SysViewAuthEntity findById(String id);

    void deleteById(String id);
}

