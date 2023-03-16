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
 * 系统登录日志
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-21 10:17:23
 */
@Data
@TableName("sys_login_log")
public class SysLoginLogEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * token
	 */
	@TableId
	private String token;
	/**
	 * 机构号
	 */
	private String brNo;
	/**
	 * 操作员编号
	 */
	private String opNo;
	/**
	 * 操作员名称
	 */
	private String opName;
	/**
	 * 登录日期
	 */
	private String loginDate;
	/**
	 * 登录时间
	 */
	private String loginTime;
	/**
	 * 登录ip
	 */
	private String loginIp;
	/**
	 * 登出时间
	 */
	private String logoutTime;
	/**
	 * 操作系统
	 */
	private String osName;
	/**
	 * 系统版本
	 */
	private String osVersion;
	/**
	 * 浏览器
	 */
	private String ieName;
	/**
	 * 浏览器版本
	 */
	private String ieVersion;

}
