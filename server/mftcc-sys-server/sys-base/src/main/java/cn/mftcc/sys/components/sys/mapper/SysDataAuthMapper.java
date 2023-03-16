/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysDataAuthEntity;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据权限配置表
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-20 11:15:06
 */
@Mapper
public interface SysDataAuthMapper extends BaseMapper<SysDataAuthEntity> {

    JSONArray getAuthByRoleId(String roleId);

    List<String> findRoleIdByModelId(String modelId);

}
