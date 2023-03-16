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
 * 系统菜单表
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-05 12:11:55
 */
@Data
@TableName("sys_menu")
public class SysMenuEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单编号
	 */
	@TableId(type = IdType.UUID)
	private String menuId;
	/**
	 * 菜单名称
	 */
	private String menuName;
	/**
	 * 上级菜单编号
	 */
	private String upMenuId;
	/**
	 * 菜单图标
	 */
	private String menuIcon;
	/**
	 * 菜单URL
	 */
	private String menuUrl;
	/**
	 * 序号
	 */
	private Integer sn;
	/**
	 * 状态
	 */
	private String sts;
	/**
	 * 法人机构号
	 */
	private String corpId;
	/**
	 * 是否为新页签
	 */
	private String newTab;

}
