/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.sys.common.runner.CacheUtil;
import cn.mftcc.sys.components.sys.entity.SysParmTransEntity;
import cn.mftcc.sys.components.sys.service.SysParmTransService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;

/**
 * 字典项转换
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-07-22 16:29:26
 */
@RestController
@RequestMapping("sys/sysParmTrans")
public class SysParmTransController {

    @Autowired
    private SysParmTransService sysParmTransService;

    @Autowired
    private CacheUtil cacheUtil;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysParmTransEntity sysParmTransEntity) {
        IPage<SysParmTransEntity> list = this.sysParmTransService.findByPage(sysParmTransEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysParmTransEntity sysParmTransEntity){
        this.sysParmTransService.insert(sysParmTransEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @PostMapping("/update")
    public R update(@RequestBody SysParmTransEntity sysParmTransEntity){
        this.sysParmTransService.update(sysParmTransEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{id}")
    public R findById(@PathVariable("id") String id){
        SysParmTransEntity sysParmTransEntity = this.sysParmTransService.findById(id);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysParmTransEntity);
    }

    @DeleteMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") String id){
        this.sysParmTransService.deleteById(id);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @PostMapping("/initParmTransCaChe")
    public R initParmTransCaChe() {
        try {
            cacheUtil.initParmTransCaChe();
            return R.ok();
        }catch (Exception e){
            return R.error();
        }
    }
}
