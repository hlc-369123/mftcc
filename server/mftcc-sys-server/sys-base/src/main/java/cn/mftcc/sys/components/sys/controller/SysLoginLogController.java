/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.sys.entity.SysLoginLogEntity;
import cn.mftcc.sys.components.sys.service.SysLoginLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统登录日志
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-21 10:17:23
 */
@RestController
@RequestMapping("sys/sysLoginLog")
public class SysLoginLogController {

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysLoginLogEntity sysLoginLogEntity) {
        IPage<SysLoginLogEntity> list = this.sysLoginLogService.findByPage(sysLoginLogEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysLoginLogEntity sysLoginLogEntity){
        this.sysLoginLogService.insert(sysLoginLogEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @PutMapping("/update")
    public R update(@RequestBody SysLoginLogEntity sysLoginLogEntity){
        this.sysLoginLogService.update(sysLoginLogEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{token}")
    public R findById(@PathVariable("token") String token){
        SysLoginLogEntity sysLoginLogEntity = this.sysLoginLogService.findById(token);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysLoginLogEntity);
    }

    @DeleteMapping("/deleteById/{token}")
    public R deleteById(@PathVariable("token") String token){
        this.sysLoginLogService.deleteById(token);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }
}