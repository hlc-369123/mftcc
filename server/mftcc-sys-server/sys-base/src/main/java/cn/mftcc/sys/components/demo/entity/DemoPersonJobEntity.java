/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.entity;

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
 * @date 2020-11-16 17:16:55
 */
@Data
@TableName("demo_person_job")
public class DemoPersonJobEntity extends MftccEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	private String workId;
	/**
	 * 所在单位
	 */
	private String workUnit;
	/**
	 * 工作时间
	 */
	private String workDateStart;
	private String workDateEnd;
	/**
	 * 担任职务
	 */
	private String duty;
	/**
	 * 单位电话
	 */
	private String telephone;

}
