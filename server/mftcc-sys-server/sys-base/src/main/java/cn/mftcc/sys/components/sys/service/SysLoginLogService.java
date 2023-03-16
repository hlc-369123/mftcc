/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysLoginLogEntity;

import cn.mftcc.common.exception.ServiceException;

/**
 * 系统登录日志
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-21 10:17:23
 */
public interface SysLoginLogService {

    IPage<SysLoginLogEntity> findByPage(SysLoginLogEntity sysLoginLogEntity) throws ServiceException;

    void insert(SysLoginLogEntity sysLoginLogEntity) throws ServiceException;

    void update(SysLoginLogEntity sysLoginLogEntity) throws ServiceException;

    SysLoginLogEntity findById(String token) throws ServiceException;

    void deleteById(String token) throws ServiceException;
}

