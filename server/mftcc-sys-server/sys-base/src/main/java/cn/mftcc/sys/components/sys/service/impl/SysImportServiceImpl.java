/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.mftcc.sys.components.sys.entity.SysImportEntity;
import cn.mftcc.sys.components.sys.mapper.SysImportMapper;
import cn.mftcc.sys.components.sys.service.SysImportService;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 引用对照表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-12 11:19:13
 */
@Service("sysImportService")
@Transactional(rollbackFor=Exception.class)
public class SysImportServiceImpl implements SysImportService {

    @Autowired
    private SysImportMapper sysImportMapper;
    @Override
    public IPage<SysImportEntity> findByPage(SysImportEntity sysImportEntity) {
        //翻页
        IPage<SysImportEntity> page = new Page<>();
        page.setCurrent(sysImportEntity.getPageNo());
        page.setSize(sysImportEntity.getPageSize());
        QueryWrapper<SysImportEntity> queryWrapper = new QueryWrapper<>();


        return sysImportMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysImportEntity sysImportEntity) {
        sysImportMapper.insert(sysImportEntity);
    }

    @Override
    public void update(SysImportEntity sysImportEntity) {
        sysImportMapper.updateById(sysImportEntity);
    }

    @Override
    public SysImportEntity findById(String id) {
        return sysImportMapper.selectById(id);
    }

    @Override
    public void deleteById(String id) {
        sysImportMapper.deleteById(id);
    }

    @Override
    public void deleteByIdUpId(String nodeId, String upCmptId) {
        Map<String,Object> map = new HashMap<>();
        map.put("cmpt_id",nodeId);
        map.put("up_cmpt_id",upCmptId);
        sysImportMapper.deleteByMap(map);
    }

}