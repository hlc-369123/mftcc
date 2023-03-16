/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysUserSearchFilterEntity;

import cn.mftcc.common.exception.ServiceException;

import java.util.List;

/**
 * 筛选条件存储表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-23 09:42:01
 */
public interface SysUserSearchFilterService {

    IPage<SysUserSearchFilterEntity> findByPage(SysUserSearchFilterEntity sysUserSearchFilterEntity) throws ServiceException;

    SysUserSearchFilterEntity insert(SysUserSearchFilterEntity sysUserSearchFilterEntity) throws ServiceException;

    void update(SysUserSearchFilterEntity sysUserSearchFilterEntity) throws ServiceException;

    SysUserSearchFilterEntity findById(String filterNo) throws ServiceException;

    List<SysUserSearchFilterEntity> findByVue(String vue) throws ServiceException;

    void deleteById(String filterNo) throws ServiceException;
}

