/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.mapper;

import cn.mftcc.sys.components.sys.entity.SysSecAuditConfigEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登陆校验规则表
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-03 13:42:15
 */
@Mapper
public interface SysSecAuditConfigMapper extends BaseMapper<SysSecAuditConfigEntity> {
	
}
