/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.sys.components.sys.entity.SysRoleEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 角色
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-21 16:55:37
 */
public interface SysRoleService {

    IPage<SysRoleEntity> findByPage(SysRoleEntity sysRoleEntity) throws ServiceException;

    List<SysRoleEntity> findAll() throws ServiceException;

    void insert(SysRoleEntity sysRoleEntity) throws ServiceException;

    void update(SysRoleEntity sysRoleEntity) throws ServiceException;

    SysRoleEntity findById(String roleId) throws ServiceException;

    SysRoleEntity findByRoleNo(String roleNo) throws ServiceException;

    void deleteById(String roleNo) throws ServiceException;

    List<SysRoleEntity> findListByCorpId() throws ServiceException;

    List<SysRoleEntity> findListByRoleIds(List<String> roleIds) throws ServiceException;

    List<SysRoleEntity> findListByRoleNos(List<String> roleNos) throws ServiceException;

    List<SysRoleEntity> findListByCorpId(String corpId) throws ServiceException;

    IPage<SysRoleEntity> findByPage4admin(SysRoleEntity sysRoleEntity);

    List<SysRoleEntity> selectList(SysRoleEntity sysRoleEntity) throws ServiceException;
}

