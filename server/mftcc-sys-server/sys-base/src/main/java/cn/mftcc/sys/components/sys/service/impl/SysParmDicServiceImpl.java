/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service.impl;

import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.sys.components.sys.entity.SysParmDicEntity;
import cn.mftcc.sys.components.sys.mapper.SysParmDicMapper;
import cn.mftcc.sys.components.sys.service.SysParmDicService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("sysParmDicService")
@Transactional(rollbackFor=Exception.class)
public class SysParmDicServiceImpl implements SysParmDicService {

    @Autowired
    private SysParmDicMapper sysParmDicMapper;
    @Override
    public IPage<SysParmDicEntity> findByPage(SysParmDicEntity sysParmDicEntity) {
        //翻页
        IPage<SysParmDicEntity> page = new Page<>();
        page.setCurrent(sysParmDicEntity.getPageNo());
        page.setSize(sysParmDicEntity.getPageSize());
        QueryWrapper<SysParmDicEntity> queryWrapper = new QueryWrapper<>();
        //指定字段查询
        queryWrapper
            .eq(StringUtils.isNotBlank(sysParmDicEntity.getKeyName()),"key_name",sysParmDicEntity.getKeyName())
            .eq(StringUtils.isNotBlank(sysParmDicEntity.getOptCode()),"opt_code",sysParmDicEntity.getOptCode())
            .eq(StringUtils.isNotBlank(sysParmDicEntity.getOptName()),"opt_name",sysParmDicEntity.getOptName())
            .eq(StringUtils.isNotBlank(sysParmDicEntity.getSts()),"sts",sysParmDicEntity.getSts());

        //动态查询-多列
        MapperUtil.dynamicQuery(queryWrapper, sysParmDicEntity.getDynamicQuery(),"key_name","opt_code","opt_name","seqn","sts");
        return sysParmDicMapper.selectPage(page,queryWrapper);
    }

    @Override
    public List<SysParmDicEntity> findByKeyName(List<String> keyNameList){
        QueryWrapper<SysParmDicEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("key_name",keyNameList);
        return sysParmDicMapper.selectList(queryWrapper);
    }

    @Override
    public void insert(SysParmDicEntity sysParmDicEntity) {
        sysParmDicMapper.insert(sysParmDicEntity);
    }

    @Override
    public void update(SysParmDicEntity sysParmDicEntity) {
        sysParmDicMapper.updateById(sysParmDicEntity);
    }

    @Override
    public SysParmDicEntity findById(String keyName) {
        return sysParmDicMapper.selectById(keyName);
    }

    @Override
    public void deleteById(String keyName) {
        sysParmDicMapper.deleteById(keyName);
    }

    @Override
    public SysParmDicEntity findByKeyNameAndOptCode(SysParmDicEntity sysParmDicEntity) {
        QueryWrapper<SysParmDicEntity> queryWrapper = new QueryWrapper<>();
        //指定字段查询
        queryWrapper.eq("KEY_NAME",sysParmDicEntity.getKeyName())
                .eq("OPT_CODE",sysParmDicEntity.getOptCode());
        return sysParmDicMapper.selectOne(queryWrapper);
    }
}