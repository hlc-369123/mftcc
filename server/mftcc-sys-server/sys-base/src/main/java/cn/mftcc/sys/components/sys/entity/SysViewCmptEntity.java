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
 * 综合视图组件定义
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-06 15:41:56
 */
@Data
@TableName("sys_view_cmpt")
public class SysViewCmptEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 视角组件编号
	 */
	@TableId(type = IdType.UUID)
	private String viewCmptId;
	/**
	 * 视角编号
	 */
	private String viewId;
	/**
	 * 视角组件名称
	 */
	private String viewCmptName;
	/**
	 * 上级视角组件编号
	 */
	private String upViewCmptId;
	/**
	 * 视角组件类型
	 */
	private String viewCmptTyp;
	/**
	 * 菜单地址
	 */
	private String menuUrl;
	/**
	 * 按钮函数
	 */
	private String btnMthd;
	/**
	 * 是否刷新页面
	 */
	private String refreshFlg;
	/**
	 * 是否默认加载
	 */
	private String loadFlg;
	/**
	 * 显隐表达式
	 */
	private String expr;
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
