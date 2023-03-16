/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.utils.AuthorityUtil;
import cn.mftcc.sys.components.sys.entity.SysMenuAuthEntity;
import cn.mftcc.sys.components.sys.entity.SysModelAuthEntity;
import cn.mftcc.sys.components.sys.entity.SysViewAuthEntity;
import cn.mftcc.sys.components.sys.mapper.SysMenuAuthMapper;
import cn.mftcc.sys.components.sys.mapper.SysModelAuthMapper;
import cn.mftcc.sys.components.sys.mapper.SysViewAuthMapper;
import cn.mftcc.sys.components.sys.service.SysAuthService;
import cn.mftcc.sys.components.sys.service.SysDataAuthService;
import cn.mftcc.sys.components.sys.service.SysModelService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单权限表
 *
 * @author guohanchen
 * @email
 * @date 2020-03-18 11:35:29
 */
@Transactional(rollbackFor=Exception.class)
@Service("sysAuthService")
public class SysAuthServiceImpl implements SysAuthService {

    @Autowired
    private SysMenuAuthMapper sysMenuAuthMapper;
    @Autowired
    private SysViewAuthMapper sysViewAuthMapper;
    @Autowired
    private SysModelAuthMapper sysModelAuthMapper;
    @Autowired
    private SysModelService sysModelService;
    @Autowired
    private AuthorityUtil authorityUtil;
    @Autowired
    private SysDataAuthService sysDataAuthService;

    @Override
    public JSONObject getAuthByRole(String roleId) {
        Map<String,Object> map = new HashMap<>();
        map.put("role_id",roleId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("menuList",sysMenuAuthMapper.selectByMap(map));
        jsonObject.put("viewList",sysViewAuthMapper.selectByMap(map));
        jsonObject.put("modelList",sysModelAuthMapper.selectByMap(map));
        return jsonObject;
    }

    @Override
    public void auth(JSONObject jsonObject) {
        String roleId = jsonObject.getString("roleId");
        Map<String,Object> map = new HashMap<>();
        map.put("role_id",roleId);
        List<SysMenuAuthEntity> menuList = jsonObject.getJSONArray("menu").toJavaList(SysMenuAuthEntity.class);
        sysMenuAuthMapper.deleteByMap(map);
        if(menuList.size()>0){
            sysMenuAuthMapper.insertBatch(menuList);
        }
        List<SysViewAuthEntity> viewList = jsonObject.getJSONArray("view").toJavaList(SysViewAuthEntity.class);
        sysViewAuthMapper.deleteByMap(map);
        if(viewList.size()>0){
            sysViewAuthMapper.insertBatch(viewList);
        }
        List<SysModelAuthEntity> modelList = jsonObject.getJSONArray("model").toJavaList(SysModelAuthEntity.class);
        sysModelAuthMapper.deleteByMap(map);
        if(modelList.size()>0){
            sysModelAuthMapper.insertBatch(modelList);
        }

        JSONArray urlAuth = sysModelService.getUrlAuthByRoleId(roleId);
        JSONArray dataAuth = sysDataAuthService.getDataAuthByRoleId(roleId);
        try {
            authorityUtil.setAuth(roleId,"urlAuth",urlAuth);
            authorityUtil.setAuth(roleId,"dataAuth",dataAuth);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}