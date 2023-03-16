/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 菜单权限表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 11:35:29
 */
public interface SysAuthService {

    JSONObject getAuthByRole(String roleId);
    void auth(JSONObject jsonObject);


}

