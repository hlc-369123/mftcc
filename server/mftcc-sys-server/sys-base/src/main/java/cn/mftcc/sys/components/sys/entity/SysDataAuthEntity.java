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
 * 数据权限配置表
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-20 11:15:06
 */
@Data
@TableName("sys_data_auth")
public class SysDataAuthEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 索引主键
	 */
	@TableId(type= IdType.UUID)
	private String id;
	/**
	 * 角色编号
	 */
	private String roleId;
	/**
	 * 功能编号
	 */
	private String modelId;
	/**
	 * 数据权限类型
	 */
	private String authType;
	/**
	 *拓展字段
	 */
	private String authField;

}
