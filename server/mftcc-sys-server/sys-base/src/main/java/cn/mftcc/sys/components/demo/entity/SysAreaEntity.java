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

@Data
@TableName("sys_area")
public class SysAreaEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private String areaNo;
	private String areaName;
	private String lev;
	private String uplev;
	private String areaSts;
	private String ifLeaf;

}
