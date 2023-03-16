/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.mftcc.common.MftccEntity;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * 
 * @author guohanchen
 * @email 
 * @date 2020-11-16 17:02:01
 */
@Data
@TableName("demo_bank_acc_manage")
public class DemoBankAccManageEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 账号
	 */
	@TableId
	private String accountNo;
	/**
	 * 户名
	 */
	private String accountName;
	/**
	 * 开户行
	 */
	private String accountBank;
	/**
	 * 支行名称
	 */
	private String accountSubBank;
	/**
	 * 启用状态
	 */
	private String accountSts;
	/**
	 * 关联合同号
	 */
	private String hetongNo;
	/**
	 * 业务期限
	 */
	private String bizDate;
	/**
	 * 账户用途
	 */
	private String accountUse;
	/**
	 * 业务状态
	 */
	private String bizSts;

}
