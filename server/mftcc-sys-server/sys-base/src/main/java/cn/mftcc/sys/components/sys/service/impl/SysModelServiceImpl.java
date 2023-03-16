/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.utils.AuthorityUtil;
import cn.mftcc.sys.components.sys.service.SysDataAuthService;
import cn.mftcc.sys.components.sys.service.SysModelAuthService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mftcc.sys.components.sys.entity.SysModelEntity;
import cn.mftcc.sys.components.sys.mapper.SysModelMapper;
import cn.mftcc.sys.components.sys.service.SysModelService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-12 10:12:38
 */
@Service("sysModelService")
@Transactional(rollbackFor=Exception.class)
public class SysModelServiceImpl implements SysModelService {

    @Autowired
    private SysModelMapper sysModelMapper;
    @Autowired
    private AuthorityUtil authorityUtil;
    @Autowired
    private SysModelAuthService sysModelAuthService;
    @Autowired
    private SysModelService sysModelService;
    @Autowired
    private SysDataAuthService sysDataAuthService;
    @Override
    public IPage<SysModelEntity> findByPage(SysModelEntity sysModelEntity) {
        //翻页
        IPage<SysModelEntity> page = new Page<>();
        page.setCurrent(sysModelEntity.getPageNo());
        page.setSize(sysModelEntity.getPageSize());
        QueryWrapper<SysModelEntity> queryWrapper = new QueryWrapper<>();


        return sysModelMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysModelEntity sysModelEntity) {
        sysModelMapper.insert(sysModelEntity);
    }

    @Override
    public void update(SysModelEntity sysModelEntity) {
        sysModelMapper.updateById(sysModelEntity);

        List<String> sysModelAuthList = sysModelAuthService.findRoleIdByModelId(sysModelEntity.getModelId());
        for(String roleId : sysModelAuthList){
            JSONArray urlAuth = sysModelService.getUrlAuthByRoleId(roleId);
            try {
                authorityUtil.setAuth(roleId,"urlAuth",urlAuth);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        List<String> sysDataAuthList = sysDataAuthService.findRoleIdByModelId(sysModelEntity.getModelId());

        for(String roleId : sysDataAuthList){
            JSONArray dataAuth = sysDataAuthService.getDataAuthByRoleId(roleId);
            try {
                authorityUtil.setAuth(roleId,"dataAuth",dataAuth);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public SysModelEntity findById(String modelId) {
        return sysModelMapper.selectById(modelId);
    }

    @Override
    public void deleteById(String modelId) {
            sysModelMapper.deleteById(modelId);
    }

    @Override
    public JSONArray findListJionImport() {
        return sysModelMapper.findAllJionImport();
    }

    @Override
    public List<SysModelEntity> findAllList() {
        return sysModelMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public List<String> findListByRoleIds(List<String> roleIds){
        return sysModelMapper.findListByRoleIds(roleIds);
    }

    @Override
    public JSONArray findUrlAuthByModelId(String modelId) {
        QueryWrapper<SysModelEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("model_id",modelId);
        SysModelEntity sysModelEntity = sysModelMapper.selectOne(queryWrapper);
        if(sysModelEntity!=null&&StringUtils.isNotBlank(sysModelEntity.getUrlAuth())){
            return JSONArray.parseArray(sysModelEntity.getUrlAuth());
        }else{
            return new JSONArray();
        }
    }

    @Override
    public JSONArray getUrlAuthByRoleId(String roleId){
        JSONArray jsonArray = new JSONArray();
        List<SysModelEntity> sysModelList = sysModelMapper.findListByRoleId(roleId);
        for(SysModelEntity sysModelEntity : sysModelList){
            if(StringUtils.isNotBlank(sysModelEntity.getUrlAuth())){
                JSONArray urlAuth = JSONArray.parseArray(sysModelEntity.getUrlAuth());
                for(int i = 0; i < urlAuth.size(); i++){
                    jsonArray.add(urlAuth.getJSONObject(i));
                }
            }
        }
        return jsonArray;
    }

    @Override
    public void deleteUrlAuthByModelId(JSONObject parm){
        String modelId = parm.getString("modelId");
        JSONArray list = this.findUrlAuthByModelId(modelId);
        JSONArray urlAuthArray = new JSONArray();
        for(int i = 0;i< list.size();i++){
            JSONObject urlAuth = list.getJSONObject(i);
            if(!parm.get("url").equals(urlAuth.getString("url"))){
                urlAuthArray.add(urlAuth);
            }
        }
        SysModelEntity sysModelEntity = this.findById(modelId);
        sysModelEntity.setUrlAuth(urlAuthArray.toJSONString());
        this.update(sysModelEntity);
    }

}