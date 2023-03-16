/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.customFilter.service;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.sys.components.customFilter.entity.SysCustomFilterEntity;

/**
 * 自定义筛选
 *
 * @author guohanchen
 * @email 
 * @date 2021-05-19 14:34:17
 */
public interface SysCustomFilterService {

    void save(SysCustomFilterEntity sysCustomFilterEntity) throws ServiceException;

    SysCustomFilterEntity findByRouterOpNo(SysCustomFilterEntity sysCustomFilterEntity) throws ServiceException;
}

