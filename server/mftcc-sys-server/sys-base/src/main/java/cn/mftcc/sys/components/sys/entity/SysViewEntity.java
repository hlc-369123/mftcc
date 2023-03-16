/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 综合视图定义
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-06 15:35:22
 */
@Data
@TableName("sys_view")
public class SysViewEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 视角编号
	 */
	@TableId(type = IdType.UUID)
	private String viewId;
	/**
	 * 上级组件编号
	 */
	private String upCmptId;
	/**
	 * 上级组件类型
	 */
	private String upCmptTyp;
	/**
	 * 视角名称
	 */
	private String viewName;
	/**
	 * 序号
	 */
	private Integer sn;
	/**
	 * 状态
	 */
	private String sts;
	/**
	 * 法人机构号
	 */
	private String corpId;

}
