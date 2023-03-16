/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.service;

import cn.mftcc.common.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;

import java.util.List;

/**
 * 用户
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-21 16:30:33
 */
public interface SysUserService {

    IPage<SysUserEntity> findByPage(SysUserEntity sysUserEntity) throws ServiceException;

    void insert(SysUserEntity sysUserEntity) throws ServiceException;

    void update(SysUserEntity sysUserEntity) throws ServiceException;

    void updatePassword(SysUserEntity sysUserEntity) throws ServiceException;

    SysUserEntity findById(int userId) throws ServiceException;

    void deleteById(int userId) throws ServiceException;

    SysUserEntity findByOpNo(String opNo) throws ServiceException;

    List<SysUserEntity> getUserByRoleTypes(String roleTypes) throws ServiceException;

    List<SysUserEntity> findByBrNo(List<String> brNo) throws ServiceException;

    List<SysUserEntity> findByRoleNo(String roleNo) throws ServiceException;

    IPage<SysUserEntity> findByPage4Dialog(JSONObject sysUserEntity);

    IPage<SysUserEntity> findAllByPage(JSONObject sysUserEntity);

    List<SysUserEntity> findAll(JSONObject sysUserEntity) throws ServiceException;

    List<SysUserEntity> getList(SysUserEntity sysUserEntity) throws ServiceException;

    IPage<SysUserEntity> findByPage4admin(SysUserEntity sysUserEntity);

    List<SysUserEntity> findByRoleNo4MQ(String roleNo);

    List<SysUserEntity> findNoDeptNoRoleUserList();

    List<SysUserEntity> findUserListByBrRole(String brNo, String roleNo);


}

