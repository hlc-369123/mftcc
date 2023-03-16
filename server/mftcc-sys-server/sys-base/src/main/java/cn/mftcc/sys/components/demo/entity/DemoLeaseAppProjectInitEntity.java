/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目登记表
 * 
 * @author guohanchen
 * @email 
 * @date 2021-03-19 10:42:18
 */
@Data
@TableName("demo_lease_app_project_init")
public class DemoLeaseAppProjectInitEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String parentAppId;
	/**
	 * 主键id
	 */
	@TableId(type = IdType.UUID)
	private String appId;
	/**
	 * 项目申请号
	 */
	private String appNo;
	/**
	 * 项目名称
	 */
	private String appName;
	/**
	 * 客户号
	 */
	private String cusNo;
	/**
	 * 客户名称
	 */
	private String cusName;
	/**
	 * 客户类型
	 */
	private String cusType;
	/**
	 * 证件类型
	 */
	private String idType;
	/**
	 * 证件号
	 */
	private String idNo;
	/**
	 * 供货商号
	 */
	private String supplierCusNo;
	/**
	 * 供货商名称
	 */
	private String supplierCusName;
	/**
	 * 租赁物描述
	 */
	private String lamRmk;
	/**
	 * 租赁类型
	 */
	private String leaseType;
	/**
	 * 报价方案
	 */
	private String priceRmk;
	/**
	 * 资金用途
	 */
	private String amtUse;
	/**
	 * 风控举措
	 */
	private String riskContMeas;
	/**
	 * 协办客户经理编号
	 */
	private String coManageNo;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 流程模型
	 */
	private String appInitWfNo;
	/**
	 * 提交状态
	 */
	private String appSts;
	/**
	 * 登记人编号
	 */
	private String regNo;
	/**
	 * 登记人名称
	 */
	private String regName;
	/**
	 * 登记人机构号
	 */
	private String orgNo;
	/**
	 * 登记人机构名
	 */
	private String orgName;
	/**
	 * 更新人编号
	 */
	private String lstRegNo;
	/**
	 * 更新人名称
	 */
	private String lstRegName;
	/**
	 * 更新人机构号
	 */
	private String lstOrgNo;
	/**
	 * 更新人机构名
	 */
	private String lstOrgName;
	/**
	 * 登记日期
	 */
	private String regDate;
	/**
	 * 更新日期
	 */
	private String lstDate;
	/**
	 * 登记时间
	 */
	private Date regTime;
	/**
	 * 更新时间
	 */
	private Date lstTime;
	/**
	 * 法人机构号
	 */
	private String corpId;

}
