/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.demo.entity.SysAreaEntity;
import cn.mftcc.sys.components.demo.service.SysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("demo/sysArea")
public class SysAreaController {

    @Autowired
    private SysAreaService sysAreaService;

    @PostMapping("/findByUpLev")
    public R findByUpLev(@RequestBody SysAreaEntity sysAreaEntity) {
        List<SysAreaEntity> list = this.sysAreaService.findByUpLev(sysAreaEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }


}
