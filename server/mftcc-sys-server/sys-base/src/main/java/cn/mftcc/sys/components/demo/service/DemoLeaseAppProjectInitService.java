/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.demo.entity.DemoLeaseAppProjectInitEntity;

import cn.mftcc.common.exception.ServiceException;

/**
 * 项目登记表
 *
 * @author guohanchen
 * @email 
 * @date 2021-03-19 10:42:18
 */
public interface DemoLeaseAppProjectInitService {

    IPage<DemoLeaseAppProjectInitEntity> findByPage(DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity) throws ServiceException;

    void insert(DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity) throws ServiceException;

    void update(DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity) throws ServiceException;

    DemoLeaseAppProjectInitEntity findById(String appId) throws ServiceException;

    void deleteById(String appId) throws ServiceException;

    void saveEditTable(JSONObject jsonObject);

    void sendMessage(JSONObject jsonObject);

}

