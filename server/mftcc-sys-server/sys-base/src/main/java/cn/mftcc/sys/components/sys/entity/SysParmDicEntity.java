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
import java.math.BigDecimal;

/**
 * 字典项
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-26 10:34:15
 */
@Data
@TableName("sys_parm_dic")
public class SysParmDicEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 字典项主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 字典项键值
	 */
	private String keyName;
	/**
	 * 实际值
	 */
	private String optCode;
	/**
	 * 显示值
	 */
	private String optName;
	/**
	 * 顺序
	 */
	private BigDecimal seqn;
	/**
	 * 是否生效
	 */
	private String sts;

}
