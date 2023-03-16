/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service.impl;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.components.demo.entity.SysAreaEntity;
import cn.mftcc.sys.components.demo.mapper.SysAreaMapper;
import cn.mftcc.sys.components.demo.service.SysAreaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("sysAreaService")
@Transactional(rollbackFor = ServiceException.class)
public class SysAreaServiceImpl implements SysAreaService {

    @Autowired
    private SysAreaMapper sysAreaMapper;
    @Autowired
    private MapperUtil mapperUtil;
    @Autowired
    private RequestUtil requestUtil;


    @Override
    public List<SysAreaEntity> findByUpLev(SysAreaEntity sysAreaEntity) throws ServiceException {
        QueryWrapper<SysAreaEntity> queryWrapper = new QueryWrapper<>();
            //指定字段查询
        queryWrapper.eq(StringUtils.isNotBlank(sysAreaEntity.getUplev()),"uplev",sysAreaEntity.getUplev());
        return sysAreaMapper.selectList(queryWrapper);
    }
}
