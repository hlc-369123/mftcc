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

import cn.mftcc.sys.components.sys.entity.SysViewCmptEntity;
import cn.mftcc.sys.components.sys.mapper.SysViewCmptMapper;
import cn.mftcc.sys.components.sys.service.SysViewCmptService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 综合视图组件定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-06 15:41:56
 */
@Service("sysViewCmptService")
@Transactional(rollbackFor=Exception.class)
public class SysViewCmptServiceImpl implements SysViewCmptService {

    @Autowired
    private SysViewCmptMapper sysViewCmptMapper;
    @Override
    public IPage<SysViewCmptEntity> findByPage(SysViewCmptEntity sysViewCmptEntity) {
        //翻页
        IPage<SysViewCmptEntity> page = new Page<>();
        page.setCurrent(sysViewCmptEntity.getPageNo());
        page.setSize(sysViewCmptEntity.getPageSize());
        QueryWrapper<SysViewCmptEntity> queryWrapper = new QueryWrapper<>();


        return sysViewCmptMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysViewCmptEntity sysViewCmptEntity) {
        sysViewCmptMapper.insert(sysViewCmptEntity);
    }

    @Override
    public void update(SysViewCmptEntity sysViewCmptEntity) {
        sysViewCmptMapper.updateById(sysViewCmptEntity);
    }

    @Override
    public SysViewCmptEntity findById(String viewCmptId) {
        return sysViewCmptMapper.selectById(viewCmptId);
    }

    @Override
    public void deleteById(String viewCmptId) {
            sysViewCmptMapper.deleteById(viewCmptId);
    }

    @Override
    public List<SysViewCmptEntity> findAllList() {
        return sysViewCmptMapper.selectList(new QueryWrapper<>());
    }

}