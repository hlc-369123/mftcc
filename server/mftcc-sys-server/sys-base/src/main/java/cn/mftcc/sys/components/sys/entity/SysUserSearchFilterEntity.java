/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.mftcc.common.MftccEntity;

import java.io.Serializable;
import lombok.Data;

/**
 * 筛选条件存储表
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-23 09:42:01
 */
@Data
@TableName("sys_user_search_filter")
public class SysUserSearchFilterEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 筛选编号
	 */
	@TableId(type = IdType.UUID)
	private String filterNo;
	/**
	 * 用户号
	 */
	private String opNo;
	/**
	 * 请求对应组件
	 */
	private String vue;
	/**
	 * 筛选名称
	 */
	private String filterName;
	/**
	 * 筛选数据结构
	 */
	private String filterContent;

}
