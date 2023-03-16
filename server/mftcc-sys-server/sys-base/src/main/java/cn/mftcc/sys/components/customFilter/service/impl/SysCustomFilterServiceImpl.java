/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.customFilter.service.impl;

import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.sys.components.customFilter.entity.SysCustomFilterEntity;
import cn.mftcc.sys.components.customFilter.mapper.SysCustomFilterMapper;
import cn.mftcc.sys.components.customFilter.service.SysCustomFilterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义筛选
 *
 * @author guohanchen
 * @email 
 * @date 2021-05-19 14:34:17
 */
@Service("sysCustomFilterService")
@Transactional(rollbackFor = ServiceException.class)
public class SysCustomFilterServiceImpl implements SysCustomFilterService {

    @Autowired
    private SysCustomFilterMapper sysCustomFilterMapper;

    @Override
    public void save(SysCustomFilterEntity sysCustomFilterEntity) throws ServiceException {
        try{
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("router",sysCustomFilterEntity.getRouter());
            columnMap.put("op_no",sysCustomFilterEntity.getOpNo());
            sysCustomFilterMapper.deleteByMap(columnMap);
            sysCustomFilterMapper.insert(sysCustomFilterEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,sysCustomFilterEntity.getRouter(),e);
        }
    }

    @Override
    public SysCustomFilterEntity findByRouterOpNo(SysCustomFilterEntity sysCustomFilterEntity) throws ServiceException {
        try{
            QueryWrapper<SysCustomFilterEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("router",sysCustomFilterEntity.getRouter());
            queryWrapper.eq("op_no",sysCustomFilterEntity.getOpNo());
            return sysCustomFilterMapper.selectOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,"",e);
        }
    }

}