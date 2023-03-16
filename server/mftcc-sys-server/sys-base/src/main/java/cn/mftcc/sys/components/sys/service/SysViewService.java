/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysViewEntity;

import java.util.List;

/**
 * 综合视图定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-06 15:35:22
 */
public interface SysViewService {

    IPage<SysViewEntity> findByPage(SysViewEntity sysViewEntity);

    void insert(SysViewEntity sysViewEntity);

    void update(SysViewEntity sysViewEntity);

    SysViewEntity findById(String viewId);

    void deleteById(String viewId);

    List<SysViewEntity> findAllList();

}

