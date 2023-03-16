/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.sys.components.sys.entity.SysParmKeyEntity;
import cn.mftcc.sys.components.sys.mapper.SysParmKeyMapper;
import cn.mftcc.sys.components.sys.service.SysParmKeyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service("sysParmKeyService")
@Transactional(rollbackFor=Exception.class)
public class SysParmKeyServiceImpl implements SysParmKeyService {

    @Autowired
    private SysParmKeyMapper sysParmKeyMapper;
    @Override
    public IPage<SysParmKeyEntity> findByPage(SysParmKeyEntity sysParmKeyEntity) {
        //翻页
        IPage<SysParmKeyEntity> page = new Page<>();
        page.setCurrent(sysParmKeyEntity.getPageNo());
        page.setSize(sysParmKeyEntity.getPageSize());
        QueryWrapper<SysParmKeyEntity> queryWrapper = new QueryWrapper<>();
        String [] moldIds = {};
        if(StringUtils.isNotBlank(sysParmKeyEntity.getMoldId())){
            moldIds = sysParmKeyEntity.getMoldId().split("\\|");
        }
        //指定字段查询
        queryWrapper
            .like(StringUtils.isNotBlank(sysParmKeyEntity.getEdit()),"edit",sysParmKeyEntity.getEdit())
            .like(StringUtils.isNotBlank(sysParmKeyEntity.getKeyCnt()),"key_cnt",sysParmKeyEntity.getKeyCnt())
            .like(StringUtils.isNotBlank(sysParmKeyEntity.getKeyName()),"key_name",sysParmKeyEntity.getKeyName())
            .in(moldIds.length > 0,"mold_id", Arrays.asList(moldIds))
            .like(StringUtils.isNotBlank(sysParmKeyEntity.getSts()),"sts",sysParmKeyEntity.getSts());

        //动态查询-多列
        MapperUtil.dynamicQuery(queryWrapper, sysParmKeyEntity.getDynamicQuery(),"edit","key_cnt","key_name","mold_id","sts");
        return sysParmKeyMapper.selectPage(page,queryWrapper);
    }

    @Override
    public void insert(SysParmKeyEntity sysParmKeyEntity) {
        sysParmKeyMapper.insert(sysParmKeyEntity);
    }

    @Override
    public void update(SysParmKeyEntity sysParmKeyEntity) {
        sysParmKeyMapper.updateById(sysParmKeyEntity);
    }

    @Override
    public SysParmKeyEntity findById(String keyName) {
        return sysParmKeyMapper.selectById(keyName);
    }

    @Override
    public void deleteById(String keyName) {
        sysParmKeyMapper.deleteById(keyName);
    }

    @Override
    public List<SysParmKeyEntity> findByMoldIds(List<String> moldIds){
        QueryWrapper<SysParmKeyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(moldIds.size()>0,"mold_id",moldIds);;
        return sysParmKeyMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysParmKeyEntity> findAll(){
        QueryWrapper<SysParmKeyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sts","1");;
        return sysParmKeyMapper.selectList(queryWrapper);
    }

}