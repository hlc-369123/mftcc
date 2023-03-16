/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.utils.AuthorityUtil;
import cn.mftcc.common.utils.RequestUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mftcc.sys.components.sys.entity.SysDataAuthEntity;
import cn.mftcc.sys.components.sys.mapper.SysDataAuthMapper;
import cn.mftcc.sys.components.sys.service.SysDataAuthService;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据权限配置表
 *
 * @author guohanchen
 * @email
 * @date 2020-03-20 11:15:06
 */
@Service("sysDataAuthService")
@Transactional(rollbackFor=Exception.class)
public class SysDataAuthServiceImpl implements SysDataAuthService {

    @Autowired
    private SysDataAuthMapper sysDataAuthMapper;
    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private AuthorityUtil authorityUtil;

    @Override
    public IPage<SysDataAuthEntity> findByPage(SysDataAuthEntity sysDataAuthEntity) {
        //翻页
        IPage<SysDataAuthEntity> page = new Page<>();
        page.setCurrent(sysDataAuthEntity.getPageNo());
        page.setSize(sysDataAuthEntity.getPageSize());
        QueryWrapper<SysDataAuthEntity> queryWrapper = new QueryWrapper<>();


        return sysDataAuthMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysDataAuthEntity sysDataAuthEntity) {
        Map<String,Object> map = new HashMap<>();
        String roleId = sysDataAuthEntity.getRoleId();
        map.put("role_id",roleId);
        map.put("model_id",sysDataAuthEntity.getModelId());
        int size = sysDataAuthMapper.selectByMap(map).size();
        if(size>0){
            QueryWrapper<SysDataAuthEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("role_id",roleId)
                    .eq("model_id",sysDataAuthEntity.getModelId());
            sysDataAuthMapper.update(sysDataAuthEntity,queryWrapper);
        }else{
            sysDataAuthMapper.insert(sysDataAuthEntity);
        }
        JSONArray dataAuth = this.getDataAuthByRoleId(roleId);
        try {
            authorityUtil.setAuth(roleId,"dataAuth",dataAuth);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(SysDataAuthEntity sysDataAuthEntity) {
        sysDataAuthMapper.updateById(sysDataAuthEntity);
    }

    @Override
    public SysDataAuthEntity findById(String id) {
        return sysDataAuthMapper.selectById(id);
    }

    @Override
    public void deleteById(String id) {
            sysDataAuthMapper.deleteById(id);
    }

    @Override
    public SysDataAuthEntity getDataAuthByModelId(String modelId, String roleId) {
        QueryWrapper<SysDataAuthEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("role_id",roleId)
                .eq("model_id",modelId);
        return sysDataAuthMapper.selectOne(queryWrapper);
    }

    @Override
    public JSONObject getDataAuthByRoleId(String corpId, String roleNo) {
        String[] roleNoArr = roleNo.split("\\|");
        int maxAuthType = 0;
        JSONObject daJsonObject = new JSONObject();
        for(String rno : roleNoArr){
            String roleId = corpId + rno;
            JSONArray jsonArray = sysDataAuthMapper.getAuthByRoleId(roleId);
            for(int i=0;i<jsonArray.size();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String mapperPath = object.getString("mapperPath");
                if(daJsonObject.containsKey(mapperPath)){
                    int authType = object.getInteger("authType");
                    if(authType>maxAuthType){
                        maxAuthType = authType;
                        daJsonObject.put(mapperPath,object);
                    }
                }else{
                    daJsonObject.put(mapperPath,object);
                }
            }
        }

        return daJsonObject;
    }

    @Override
    public JSONArray getDataAuthByRoleId(String roleId) {
        JSONArray jsonArray = sysDataAuthMapper.getAuthByRoleId(roleId);
        return jsonArray;
    }

    @Override
    public List<String> findRoleIdByModelId(String modelId) {
        return sysDataAuthMapper.findRoleIdByModelId(modelId);
    }

}