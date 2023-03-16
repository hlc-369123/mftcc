/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统菜单表
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-05 12:11:55
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {
    List<SysMenuEntity> findListByRoleIds(List<String> roleIds);
}
