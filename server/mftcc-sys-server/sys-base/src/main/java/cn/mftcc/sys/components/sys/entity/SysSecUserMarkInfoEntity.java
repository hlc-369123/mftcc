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
 * 用户记录信息
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-04 15:12:09
 */
@Data
@TableName("sys_sec_user_mark_info")
public class SysSecUserMarkInfoEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户代码
	 */
	@TableId
	private String opNo;
	/**
	 * 密码修改时间
	 */
	private String passwordUpdateTime;
	/**
	 * 登陆次数
	 */
	private Integer visitTimes;
	/**
	 * 登陆错误次数
	 */
	private Integer loginErrorTimes;
	/**
	 * 上次登陆时间
	 */
	private String lastSignInTime;
	/**
	 * 上次退出时间
	 */
	private String lastSignOutTime;
	/**
	 * 本次登陆时间
	 */
	private String currentSignInTime;
	/**
	 * 密码状态代码
	 */
	private String passwordState;
	/**
	 * 密码状态内容
	 */
	private String passwordMessege;

}
