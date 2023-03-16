/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.mftcc.common.MftccEntity;

import java.math.BigDecimal;
import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

/**
 * 融资项目表
 *
 * @author zhanglingnan
 * @email 1670680235@qq.com
 * @date 2020-07-03 16:25:37
 */
@Data
@TableName("demo_dynamic_model_table")
public class DynamicModelTableEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 融资项目主键ID
	 */
	@TableId(type = IdType.UUID)
	private String finId;
	/**
	 * 融资项目编号
	 */
	private String finNo;
	/**
	 * 融资授信编号
	 */
	private String finAuthNo;
	/**
	 * 融资机构类型
	 */
	private String finOrgType;
	/**
	 * 融资机构名称
	 */
	private String finOrgName;
	/**
	 * 业务种类
	 */
	private String finType;
	/**
	 * 放款日期
	 */
	private String loanDate;
	/**
	 * 到期日期
	 */
	private String endDate;
	/**
	 * 贷款期限月
	 */
	private Integer termMon;
	/**
	 * 基准利率
	 */
	private BigDecimal baseRate;
	/**
	 * 执行利率
	 */
	private BigDecimal executeRate;
	/**
	 * 放款本金
	 */
	private BigDecimal loanAmt;
	/**
	 * 已还本金
	 */
	private BigDecimal repayAmt;
	/**
	 * 未偿还本金
	 */
	private BigDecimal loanBal;
	/**
	 * XIRR
	 */
	private BigDecimal xirr;
	/**
	 * 融资成本
	 */
	private BigDecimal finCost;
	/**
	 * 贷款性质
	 */
	private String loanNature;
	/**
	 * 融资项目名称
	 */
	private String finProjectName;
	/**
	 * 融资主体
	 */
	private String finMain;
	/**
	 * 是否无追索保理
	 */
	private String ifNoRecFac;
	/**
	 * 本金还款方式
	 */
	private String prcpRepayType;
	/**
	 * 利息还款方式
	 */
	private String normRepayType;
	/**
	 * 计息方式
	 */
	private String icType;
	/**
	 * 其他方式描述
	 */
	private String otherTypeDesc;
	/**
	 * 保证金比例
	 */
	private BigDecimal secFlt;
	/**
	 * 保证金金额
	 */
	private BigDecimal secAmt;
	/**
	 * 手续费/顾问费支付方式
	 */
	private String feePayType;
	/**
	 * 手续费/顾问费比例(年)
	 */
	private BigDecimal feeFlt;
	/**
	 * 手续费/顾问费金额
	 */
	private BigDecimal feeAmt;
	/**
	 * 会计类别
	 */
	private String accCateType;
	/**
	 * 放贷账户银行
	 */
	private String accBankName;
	/**
	 * 放贷账户名称
	 */
	private String accName;
	/**
	 * 放贷账户
	 */
	private String accNo;
	/**
	 * 调息约定
	 */
	private String adjustType;
	/**
	 * 较基准利率
	 */
	private String comBaseRate;
	/**
	 * 上下浮比例
	 */
	private BigDecimal floatFlt;
	/**
	 * 上下浮额度
	 */
	private BigDecimal floatAmt;
	/**
	 * 固定利率
	 */
	private BigDecimal fixedRate;
	/**
	 * 项目状态
	 */
	private String finProSts;
	/**
	 * 其他利率
	 */
	private BigDecimal otherRate;
	/**
	 * 登记人编号
	 */
	private String regNo;
	/**
	 * 登记人名称
	 */
	private String regName;
	/**
	 * 最后更新操作员编号
	 */
	private String lstRegNo;
	/**
	 * 最后更新操作员名称
	 */
	private String lstRegName;
	/**
	 * 登记机构编号
	 */
	private String orgNo;
	/**
	 * 登记机构名称
	 */
	private String orgName;
	/**
	 * 最后更新机构编号
	 */
	private String lstOrgNo;
	/**
	 * 最后更新机构名称
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
	private Timestamp regTime;
	/**
	 * 更新时间
	 */
	private Timestamp lstTime;
	/**
	 * 法人机构号
	 */
	private String corpId;


}
