/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.customFilter.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.customFilter.entity.SysCustomFilterEntity;
import cn.mftcc.sys.components.customFilter.service.SysCustomFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义筛选
 *
 * @author guohanchen
 * @email 
 * @date 2021-05-19 14:34:17
 */
@RestController
@RequestMapping("customFilter/sysCustomFilter")
public class SysCustomFilterController {

    @Autowired
    private SysCustomFilterService sysCustomFilterService;

    @RequestMapping("/save")
    public R save(@RequestBody SysCustomFilterEntity sysCustomFilterEntity){
        this.sysCustomFilterService.save(sysCustomFilterEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @RequestMapping("/findByRouterOpNo")
    public R findByRouterOpNo(@RequestBody SysCustomFilterEntity sysCustomFilterEntity){
        SysCustomFilterEntity sysCustomFilter = this.sysCustomFilterService.findByRouterOpNo(sysCustomFilterEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysCustomFilter);
    }

}