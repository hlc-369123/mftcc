/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author guohanchen
 * @email 
 * @date 2020-11-16 14:00:20
 */
@Data
@TableName("demo_cus_info")
public class DemoCusInfoEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String cusNo;
	/**
	 * 
	 */
	private String cusName;
	/**
	 * 
	 */
	private String idType;
	/**
	 * 
	 */
	private String idNum;
	/**
	 * 
	 */
	private String sex;
	/**
	 * 
	 */
	private Date brithday;
	/**
	 * 
	 */
	private Integer age;
	/**
	 * 
	 */
	private String degree;
	/**
	 * 
	 */
	private String education;
	/**
	 * 
	 */
	private String nation;
	/**
	 * 
	 */
	private String cusPhone;
	/**
	 * 
	 */
	private String email;
	/**
	 * 
	 */
	private String weixin;
	/**
	 * 
	 */
	private String emergencyContact;
	/**
	 * 
	 */
	private String emergencyContactPhone;
	/**
	 * 
	 */
	private String commAddress;
	/**
	 * 
	 */
	private String withMyselfState;
	/**
	 * 
	 */
	private String homeAddress;
	/**
	 * 
	 */
	private String homePostcode;
	/**
	 * 
	 */
	private String workAddress;
	/**
	 * 
	 */
	private String workPostcode;
	/**
	 * 
	 */
	private String cusFrom;
	/**
	 * 
	 */
	private String cusManager;
	/**
	 * 
	 */
	private  String regDate;
	/**
	 * 
	 */
	private String regTime;
	private String homePhone;
	private String marital;
	private String homeAddressDetailed;
	private String spouseName;
	private String spouseIdType;
	private String spouseIdNum;
	private String spousePhone;
}
