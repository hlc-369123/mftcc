/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysViewCmptEntity;

import java.util.List;

/**
 * 综合视图组件定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-06 15:41:56
 */
public interface SysViewCmptService {

    IPage<SysViewCmptEntity> findByPage(SysViewCmptEntity sysViewCmptEntity);

    void insert(SysViewCmptEntity sysViewCmptEntity);

    void update(SysViewCmptEntity sysViewCmptEntity);

    SysViewCmptEntity findById(String viewCmptId);

    void deleteById(String viewCmptId);

    List<SysViewCmptEntity> findAllList();
}

