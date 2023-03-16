/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.initStyle.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author guohanchen
 * @email
 * @date 2021-05-27 08:34:16
 */
@Data
@TableName("sys_init_style")
public class SysInitStyleEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private String id;
	/**
	 * 公司编号
	 */
	private String companyId;
	/**
	 * 系统名称
	 */
	private String title;
	/**
	 * 系统名称颜色
	 */
	private String titleColor;
	/**
	 * 系统logo位置
	 */
	private String logoSite;
	/**
	 * 输入区域背景色
	 */
	private String inputAreaColor;
	/**
	 * 输入框背景颜色
	 */
	private String inputColor;
	/**
	 * 输入框文字颜色
	 */
	private String inputTextColor;
	/**
	 * 输入框图标颜色
	 */
	private String inputIconColor;
	/**
	 * 按钮背景色
	 */
	private String buttonColor;
	/**
	 * 按钮文字颜色
	 */
	private String buttonTextColor;
	/**
	 * 登录页背景图
	 */
	private String loginBackImg;
	/**
	 * 登录页背景图
	 */
	private String loginLogo;
	/**
	 * 登录logo
	 */
	private String sysLogo;
	/**
	 * 宣传语
	 */
	private String slogan;
	/**
	 * 加载动画
	 */
	private String loadingImg;
	/**
	 * 小号加载动画
	 */
	private String smallLoadingImg;
	/**
	 * 综合页面logo
	 */
	private String allLogo;
	/**
	 * 备用字段1
	 */
	private String spare1;
	/**
	 * 备用字段2
	 */
	private String spare2;
	/**
	 * 备用3
	 */
	private String spare3;
	/**
	 * 备用4
	 */
	private String spare4;
	/**
	 * 备用5
	 */
	private String spare5;

}
