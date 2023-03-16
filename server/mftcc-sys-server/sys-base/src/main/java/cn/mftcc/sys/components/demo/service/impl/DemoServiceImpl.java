/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service.impl;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.sys.components.demo.mapper.DemoMapper;
import cn.mftcc.sys.components.demo.service.DemoService;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.mapper.SysUserMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 14:00:20
 */
@Service("demoService")
@Transactional(rollbackFor = ServiceException.class)
public class DemoServiceImpl implements DemoService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private DemoMapper demoMapper;


    @Override
    public void saveEditTable(JSONObject jsonObject) {
        JSONArray insertData = jsonObject.getJSONArray("insertData");
        JSONArray updateData = jsonObject.getJSONArray("updateData");
        String editType = jsonObject.getString("editType");
        for(int i=0;i<insertData.size();i++){
            SysUserEntity sysUserEntity = insertData.getJSONObject(i).toJavaObject(SysUserEntity.class);
            sysUserMapper.insert(sysUserEntity);
        }
        for(int i=0;i<updateData.size();i++){
            SysUserEntity sysUserEntity = updateData.getJSONObject(i).toJavaObject(SysUserEntity.class);
            sysUserMapper.updateById(sysUserEntity);
        }
    }

    @Override
    public List<Map<String, Object>> SysAreaList(JSONObject jsonObject) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(jsonObject.getString("uplev")),"uplev",jsonObject.getString("uplev"));
        List<Map<String, Object>> maps = demoMapper.selectAreaList(queryWrapper);
        return maps;
    }
}