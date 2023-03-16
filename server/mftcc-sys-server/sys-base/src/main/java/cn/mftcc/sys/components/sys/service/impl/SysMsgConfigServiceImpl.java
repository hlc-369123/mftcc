/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.utils.MapperUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import cn.mftcc.sys.components.sys.entity.SysMsgConfigEntity;
import cn.mftcc.sys.components.sys.mapper.SysMsgConfigMapper;
import cn.mftcc.sys.components.sys.service.SysMsgConfigService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service("sysMsgConfigService")
@Transactional(rollbackFor=Exception.class)
public class SysMsgConfigServiceImpl implements SysMsgConfigService {

    @Autowired
    private SysMsgConfigMapper sysMsgConfigMapper;
    @Override
    public IPage<SysMsgConfigEntity> findByPage(SysMsgConfigEntity sysMsgConfigEntity) {
        //翻页
        IPage<SysMsgConfigEntity> page = new Page<>();
        page.setCurrent(sysMsgConfigEntity.getPageNo());
        page.setSize(sysMsgConfigEntity.getPageSize());
        QueryWrapper<SysMsgConfigEntity> queryWrapper = new QueryWrapper<>();
        String [] moldIds = {};
        if(StringUtils.isNotBlank(sysMsgConfigEntity.getMoldId())){
            moldIds = sysMsgConfigEntity.getMoldId().split("\\|");
        }
        //指定字段查询
        queryWrapper
                .in(moldIds.length > 0,"mold_id", Arrays.asList(moldIds))
                .like(StringUtils.isNotBlank(sysMsgConfigEntity.getMsgCnt()),"msg_cnt",sysMsgConfigEntity.getMsgCnt())
                .like(StringUtils.isNotBlank(sysMsgConfigEntity.getMsgCode()),"msg_code",sysMsgConfigEntity.getMsgCode())
                .like(StringUtils.isNotBlank(sysMsgConfigEntity.getMsgLvl()),"msg_lvl",sysMsgConfigEntity.getMsgLvl())
                .like(StringUtils.isNotBlank(sysMsgConfigEntity.getMsgType()),"msg_type",sysMsgConfigEntity.getMsgType());

        //动态查询-多列
        MapperUtil.dynamicQuery(queryWrapper, sysMsgConfigEntity.getDynamicQuery(),"msg_cnt","msg_code","msg_lvl","msg_type");
        return sysMsgConfigMapper.selectPage(page,queryWrapper);
    }

    @Override
    public List<SysMsgConfigEntity> findAll(){
        QueryWrapper<SysMsgConfigEntity> queryWrapper = new QueryWrapper<>();
        return sysMsgConfigMapper.selectList(queryWrapper);
    }

    @Override
    public void insert(SysMsgConfigEntity sysMsgConfigEntity) {
        sysMsgConfigMapper.insert(sysMsgConfigEntity);
    }

    @Override
    public void update(SysMsgConfigEntity sysMsgConfigEntity) {
        sysMsgConfigMapper.updateById(sysMsgConfigEntity);
    }

    @Override
    public SysMsgConfigEntity findById(String msgCode) {
        return sysMsgConfigMapper.selectById(msgCode);
    }

    @Override
    public void deleteById(String msgCode) {
            sysMsgConfigMapper.deleteById(msgCode);
    }

}