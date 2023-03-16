/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysImportEntity;

/**
 * 引用对照表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-12 11:19:13
 */
public interface SysImportService {

    IPage<SysImportEntity> findByPage(SysImportEntity sysImportEntity);

    void insert(SysImportEntity sysImportEntity);

    void update(SysImportEntity sysImportEntity);

    SysImportEntity findById(String id);

    void deleteById(String id);

    void deleteByIdUpId(String nodeId, String upCmptId);
}

