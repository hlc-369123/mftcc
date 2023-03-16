/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 字典项类别
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-25 18:18:49
 */
@Data
@TableName("sys_parm_mold")
public class SysParmMoldEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.UUID)
	private String moldId;
	/**
	 * 父级id
	 */
	private String pid;
	/**
	 * 类型名称
	 */
	private String moldName;
	/**
	 * 类型描述
	 */
	private String moldCnt;

	@TableField(exist=false)
	private List<SysParmMoldEntity> children;

}
