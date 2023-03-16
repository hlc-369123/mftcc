/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-21 16:30:33
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {

    List<SysUserEntity> getUserByRoleTypes(List<String> roleTypes);

    IPage<SysUserEntity> findByPage4Dialog(IPage<SysUserEntity> page, @Param(Constants.WRAPPER)QueryWrapper<SysUserEntity> queryWrapper);

    List<SysUserEntity> findUserListByBrRole(@Param(Constants.WRAPPER)QueryWrapper<SysUserEntity> queryWrapper);
}
