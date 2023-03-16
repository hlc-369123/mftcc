/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import cn.mftcc.sys.components.sys.entity.SysParmTransEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.mftcc.common.exception.ServiceException;

import java.util.List;

/**
 * 字典项转换
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-07-22 16:29:26
 */
public interface SysParmTransService {

    IPage<SysParmTransEntity> findByPage(SysParmTransEntity sysParmTransEntity) throws ServiceException;

    void insert(SysParmTransEntity sysParmTransEntity) throws ServiceException;

    void update(SysParmTransEntity sysParmTransEntity) throws ServiceException;

    SysParmTransEntity findById(String id) throws ServiceException;

    void deleteById(String id) throws ServiceException;

    List<SysParmTransEntity> findAll() throws ServiceException;;
}

