/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.logger.traceLog;

import cn.mftcc.common.logger.traceLog.entity.SysTraceLog;
import cn.mftcc.common.logger.traceLog.mapper.SysTraceLogMapper;
import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.common.utils.RequestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName SysTraceLog
 * @Description
 * @Author 郭涵晨
 * @Date 2020/9/15 17:08
 */
@Component
public class SysTraceLogUtil {

    @Autowired
    private RequestUtil RequestUtil;
    @Autowired
    private SysTraceLogMapper sysTraceLogMapper;
    /**
     * 交易流水新增接口
     * @param traceNo 交易流水号
     * @param traceCode 交易码
     * @param subTraceCode 子交易码
     * @param busNo 业务主键
     * @param traceDes 交易描述
     */
    public void addLog(String traceNo, String traceCode, String subTraceCode, String busNo, String traceDes){
        SysTraceLog sysTraceLog = new SysTraceLog();
        sysTraceLog.setTraceNo(traceNo);
        sysTraceLog.setTraceCode(traceCode);
        sysTraceLog.setSubTraceCode(subTraceCode);
        sysTraceLog.setBusNo(busNo);
        sysTraceLog.setTraceDes(traceDes);
        sysTraceLog.setRegNo(RequestUtil.getRequest().getAttribute("opNo").toString());
        sysTraceLogMapper.insert(sysTraceLog);
    }

    /**
     * 交易流水日志查询接口---翻页
     * pageSize = -1时 不翻页
     * @param sysTraceLog
     * @return
     */
    public IPage<SysTraceLog> findByPage(SysTraceLog sysTraceLog){
        IPage<SysTraceLog> page = new Page<>();
        page.setCurrent(sysTraceLog.getPageNo());
        page.setSize(sysTraceLog.getPageSize());
        QueryWrapper<SysTraceLog> queryWrapper = new QueryWrapper<>();
        //指定字段查询
        queryWrapper
                .like(StringUtils.isNotBlank(sysTraceLog.getTraceNo()),"trace_no",sysTraceLog.getTraceNo())
                .like(StringUtils.isNotBlank(sysTraceLog.getTraceCode()),"trace_code",sysTraceLog.getTraceCode())
                .like(StringUtils.isNotBlank(sysTraceLog.getSubTraceCode()),"sub_trace_code",sysTraceLog.getSubTraceCode())
                .like(StringUtils.isNotBlank(sysTraceLog.getBusNo()),"bus_no",sysTraceLog.getBusNo());

        //动态查询-多列
        MapperUtil.dynamicQuery(queryWrapper, sysTraceLog.getDynamicQuery(),"trace_no","trace_code","sub_trace_code","bus_no");
        return sysTraceLogMapper.selectPage(page,queryWrapper);
    }
}
