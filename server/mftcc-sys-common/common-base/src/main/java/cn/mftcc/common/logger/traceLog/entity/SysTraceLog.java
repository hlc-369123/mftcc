/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.logger.traceLog.entity;

import cn.mftcc.common.MftccEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @ClassName SysTraceLog
 * @Description
 * @Author 郭涵晨
 * @Date 2020/9/15 17:21
 */
@Data
@TableName("sys_trace_log")
public class SysTraceLog extends MftccEntity {

    @TableId(type = IdType.UUID)
    private String uuid;
    private String traceNo;
    private String traceCode;
    private String subTraceCode;
    private String traceDes;
    private String busNo;
    private String regNo;
    private Timestamp regDate;

}
