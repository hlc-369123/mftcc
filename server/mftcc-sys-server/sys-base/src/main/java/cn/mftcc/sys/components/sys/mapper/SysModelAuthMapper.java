/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysModelAuthEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 功能权限配置
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-18 16:48:50
 */
@Mapper
public interface SysModelAuthMapper extends BaseMapper<SysModelAuthEntity> {

    void insertBatch(List<SysModelAuthEntity> list);

    List<String> findRoleIdByModelId(String modelId);
}
