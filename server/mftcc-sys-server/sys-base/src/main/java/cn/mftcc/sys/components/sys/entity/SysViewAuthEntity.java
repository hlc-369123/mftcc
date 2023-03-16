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

/**
 * 视角权限配置
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-18 15:00:10
 */
@Data
@TableName("sys_view_auth")
public class SysViewAuthEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 索引主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 角色编号
	 */
	private String roleId;
	/**
	 * 视角编号
	 */
	private String viewId;
	/**
	 * 视角组件编号
	 */
	private String viewCmptId;

}
