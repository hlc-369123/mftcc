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
 * 功能定义
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-12 10:12:38
 */
@Data
@TableName("sys_model")
public class SysModelEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 功能ID
	 */
	@TableId(type = IdType.UUID)
	private String modelId;
	/**
	 * 按钮标识
	 */
	private String btnKey;
	/**
	 * mapper路径
	 */
	private String mapperPath;
	/**
	 * 功能名称
	 */
	private String modelName;
	/**
	 * 功能描述
	 */
	private String modelRmk;
	/**
	 * 功能类型
	 */
	private String modelTyp;
	/**
	 * 显隐表达式
	 */
	private String expr;
	/**
	 * 数据权限定义
	 */
	private String dataAuth;
	/**
	 * 请求权限定义
	 */
	private String urlAuth;
	/**
	 * 法人机构号
	 */
	private String corpId;

}
