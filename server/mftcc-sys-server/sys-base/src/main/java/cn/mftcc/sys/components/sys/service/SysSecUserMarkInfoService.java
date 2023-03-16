/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysSecUserMarkInfoEntity;

import cn.mftcc.common.exception.ServiceException;

/**
 * 用户记录信息
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-04 15:12:09
 */
public interface SysSecUserMarkInfoService {

    IPage<SysSecUserMarkInfoEntity> findByPage(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException;

    void insert(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException;

    void update(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException;

    SysSecUserMarkInfoEntity findById(String userId) throws ServiceException;

    void deleteById(String userId) throws ServiceException;

    void insertOrUpdate(String opNo,String message) throws ServiceException;

    void unlock(SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) throws ServiceException;
}

