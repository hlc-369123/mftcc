/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.demo.entity.DemoBankAccManageEntity;

import cn.mftcc.common.exception.ServiceException;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 17:02:01
 */
public interface DemoBankAccManageService {

    IPage<DemoBankAccManageEntity> findByPage(DemoBankAccManageEntity demoBankAccManageEntity) throws ServiceException;

    void insert(DemoBankAccManageEntity demoBankAccManageEntity) throws ServiceException;

    void update(DemoBankAccManageEntity demoBankAccManageEntity) throws ServiceException;

    DemoBankAccManageEntity findById(String accountNo) throws ServiceException;

    void deleteById(String accountNo) throws ServiceException;
}

