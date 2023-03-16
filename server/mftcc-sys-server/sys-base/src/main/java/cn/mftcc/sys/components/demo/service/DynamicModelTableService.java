/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service;

import cn.mftcc.sys.components.demo.entity.DynamicModelTableEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.mftcc.common.exception.ServiceException;

/**
 * 融资项目表
 *
 * @author zhanglingnan
 * @email 1670680235@qq.com
 * @date 2020-07-03 16:25:37
 */
public interface DynamicModelTableService {

    IPage<DynamicModelTableEntity> findByPage(DynamicModelTableEntity leaseFinProjectEntity) throws ServiceException;

    void insert(DynamicModelTableEntity leaseFinProjectEntity) throws ServiceException;

    void update(DynamicModelTableEntity leaseFinProjectEntity) throws ServiceException;

    DynamicModelTableEntity findById(String finId) throws ServiceException;

    void deleteById(String finId) throws ServiceException;
}

