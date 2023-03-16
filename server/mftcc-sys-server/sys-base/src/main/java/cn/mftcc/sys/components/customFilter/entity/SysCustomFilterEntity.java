/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.customFilter.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 自定义筛选
 * 
 * @author guohanchen
 * @email 
 * @date 2021-05-19 14:34:17
 */
@Data
@TableName("sys_custom_filter")
public class SysCustomFilterEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 路由地址
	 */
	private String router;
	/**
	 * 用户编号
	 */
	private String opNo;
	/**
	 * 自定义筛选内容
	 */
	private String customFilter;

}
