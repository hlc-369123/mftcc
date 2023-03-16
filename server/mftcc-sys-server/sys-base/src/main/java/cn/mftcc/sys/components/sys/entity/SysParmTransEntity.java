/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.mftcc.common.MftccEntity;

import java.io.Serializable;
import lombok.Data;

/**
 * 字典项转换
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-07-22 16:29:26
 */
@Data
@TableName("sys_parm_trans")
public class SysParmTransEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 键值
	 */
	private String parmKey;
	/**
	 * 描述
	 */
	private String parmKeyName;
	/**
	 * 源码值
	 */
	private String parmCode;
	/**
	 * 外部系统ID
	 */
	private String transSysId;
	/**
	 * 转换后码值
	 */
	private String transCode;

}
