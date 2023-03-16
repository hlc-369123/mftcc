/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysModelEntity;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 功能定义
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-12 10:12:38
 */
@Mapper
public interface SysModelMapper extends BaseMapper<SysModelEntity> {

	JSONArray findAllJionImport();

	List<String> findListByRoleIds(List<String> roleIds);

	List<SysModelEntity> findListByRoleId(String roleId);

}
