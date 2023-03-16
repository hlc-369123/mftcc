/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysUserSearchFilterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 筛选条件存储表
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-23 09:42:01
 */
@Mapper
public interface SysUserSearchFilterMapper extends BaseMapper<SysUserSearchFilterEntity> {
	
}
