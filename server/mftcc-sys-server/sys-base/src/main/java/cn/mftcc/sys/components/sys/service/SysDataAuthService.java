/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysDataAuthEntity;

import java.util.List;

/**
 * 数据权限配置表
 *
 * @author guohanchen
 * @email
 * @date 2020-03-20 11:15:06
 */
public interface SysDataAuthService {

    IPage<SysDataAuthEntity> findByPage(SysDataAuthEntity sysDataAuthEntity);

    void insert(SysDataAuthEntity sysDataAuthEntity);

    void update(SysDataAuthEntity sysDataAuthEntity);

    SysDataAuthEntity findById(String id);

    void deleteById(String id);

    SysDataAuthEntity getDataAuthByModelId(String modelId, String roleId);

    JSONObject getDataAuthByRoleId(String corpId, String roleNo);

    JSONArray getDataAuthByRoleId(String roleId);

    List<String> findRoleIdByModelId(String modelId);
}


