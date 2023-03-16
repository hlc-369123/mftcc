/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import java.io.Serializable;

import lombok.Data;

/**
 * 角色
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-21 16:55:37
 */
@Data
@TableName("sys_role")
public class SysRoleEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@TableId
	private String roleId;//角色id
	private String corpId;//法人机构号
	private String roleNo;//角色号
	private String roleName;//角色名
	private String roleType;//角色类型
	private int roleLvl;//角色级别

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public int getRoleLvl() {
		return roleLvl;
	}

	public void setRoleLvl(int roleLvl) {
		this.roleLvl = roleLvl;
	}
}
