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
 * 功能权限配置
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-18 16:48:50
 */
@Data
@TableName("sys_model_auth")
public class SysModelAuthEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 角色编号
	 */
	private String roleId;
	/**
	 * 功能编号
	 */
	private String modelId;

}
