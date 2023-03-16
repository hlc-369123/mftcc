/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.mftcc.common.MftccEntity;

import java.io.Serializable;
import lombok.Data;

/**
 * 登陆校验规则表
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-03 13:42:15
 */
@Data
@TableName("sys_sec_audit_config")
public class SysSecAuditConfigEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	private String itemNo;
	/**
	 * 规则种类
	 */
	private String codeType;
	/**
	 * 规则描述
	 */
	private String itemName;
	/**
	 * 规则值
	 */
	private String itemValues;
	/**
	 * 启用标志
	 */
	private String isUse;
	/**
	 * 是否可编辑
	 */
	private String isEdit;

}
