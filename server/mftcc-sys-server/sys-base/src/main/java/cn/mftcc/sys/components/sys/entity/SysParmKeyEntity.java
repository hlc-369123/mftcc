/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典项键值
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-26 10:20:27
 */
@Data
@TableName("sys_parm_key")
public class SysParmKeyEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 键值
	 */
	@TableId
	private String keyName;
	/**
	 * 描述
	 */
	private String keyCnt;
	/**
	 * 是否可编辑
	 */
	private String edit;
	/**
	 * 是否生效
	 */
	private String sts;
	/**
	 * 服务id
	 */
	private String moldId;

}
