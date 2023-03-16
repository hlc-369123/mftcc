/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.initStyle.service;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.sys.components.initStyle.entity.SysInitStyleEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 *
 *
 * @author guohanchen
 * @email
 * @date 2021-05-27 08:34:16
 */
public interface SysInitStyleService {

    IPage<SysInitStyleEntity> findByPage(SysInitStyleEntity sysInitStyleEntity) throws ServiceException;

    void insert(SysInitStyleEntity sysInitStyleEntity) throws ServiceException;

    void update(SysInitStyleEntity sysInitStyleEntity) throws ServiceException;

    SysInitStyleEntity findById(Integer id) throws ServiceException;

    void deleteById(Integer id) throws ServiceException;

    List<SysInitStyleEntity> findListByCompanyId(String companyId);
}

