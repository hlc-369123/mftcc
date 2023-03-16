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
 * 信息码配置表
 * 
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-27 14:10:21
 */
@Data
@TableName("sys_msg_config")
public class SysMsgConfigEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 信息码
	 */
	@TableId
	private String msgCode;
	/**
	 * 信息模板
	 */
	private String msgCnt;
	/**
	 * 信息类型
	 */
	private String msgType;
	/**
	 * 信息级别
	 */
	private String msgLvl;

	/**
	 * 类型主键
	 */
	private String moldId;

}
