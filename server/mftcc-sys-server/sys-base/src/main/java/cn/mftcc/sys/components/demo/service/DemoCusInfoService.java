/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.demo.entity.DemoCusInfoEntity;

import cn.mftcc.common.exception.ServiceException;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 14:00:20
 */
public interface DemoCusInfoService {

    IPage<DemoCusInfoEntity> findByPage(DemoCusInfoEntity demoCusInfoEntity) throws ServiceException;

    void insert(DemoCusInfoEntity demoCusInfoEntity) throws ServiceException;

    void update(DemoCusInfoEntity demoCusInfoEntity) throws ServiceException;

    DemoCusInfoEntity findById(String cusno) throws ServiceException;

    void deleteById(String cusno) throws ServiceException;
}

