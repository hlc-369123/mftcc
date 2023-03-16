/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysModelEntity;

import java.util.List;

/**
 * 功能定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-12 10:12:38
 */
public interface SysModelService {

    IPage<SysModelEntity> findByPage(SysModelEntity sysModelEntity);

    void insert(SysModelEntity sysModelEntity);

    void update(SysModelEntity sysModelEntity);

    SysModelEntity findById(String modelId);

    void deleteById(String modelId);

    JSONArray findListJionImport();

    List<SysModelEntity> findAllList();

    List<String> findListByRoleIds(List<String> roleIds);

    JSONArray findUrlAuthByModelId(String modelId);

    JSONArray getUrlAuthByRoleId(String roleId);

    void deleteUrlAuthByModelId(JSONObject parm);
}

