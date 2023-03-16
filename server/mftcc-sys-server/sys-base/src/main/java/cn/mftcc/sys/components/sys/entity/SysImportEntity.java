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
 * 引用对照表
 * 
 * @author guohanchen
 * @email 
 * @date 2020-03-12 11:19:13
 */
@Data
@TableName("sys_import")
public class SysImportEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 索引主键
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 组件ID
	 */
	private String cmptId;
	/**
	 * 
	 */
	private String upCmptId;
	/**
	 * 组件类型
	 */
	private String cmptTyp;

}
