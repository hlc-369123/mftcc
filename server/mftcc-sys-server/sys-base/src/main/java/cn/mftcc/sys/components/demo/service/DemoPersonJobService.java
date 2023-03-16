/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.demo.entity.DemoPersonJobEntity;

import cn.mftcc.common.exception.ServiceException;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 17:16:55
 */
public interface DemoPersonJobService {

    IPage<DemoPersonJobEntity> findByPage(DemoPersonJobEntity demoPersonJobEntity) throws ServiceException;

    void insert(DemoPersonJobEntity demoPersonJobEntity) throws ServiceException;

    void update(DemoPersonJobEntity demoPersonJobEntity) throws ServiceException;

    DemoPersonJobEntity findById(String workId) throws ServiceException;

    void deleteById(String workId) throws ServiceException;
}

