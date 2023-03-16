/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统登录日志
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-21 10:17:23
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLogEntity> {
	
}
