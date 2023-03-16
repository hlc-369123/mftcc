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
 * 菜单权限表
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-18 11:35:29
 */
@Data
@TableName("sys_menu_auth")
public class SysMenuAuthEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序列主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 角色编号
	 */
	private String roleId;
	/**
	 * 菜单编号
	 */
	private String menuId;

}
