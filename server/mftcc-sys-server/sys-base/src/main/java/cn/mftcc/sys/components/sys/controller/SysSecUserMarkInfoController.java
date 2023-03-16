/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

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
import cn.mftcc.sys.components.sys.entity.SysSecUserMarkInfoEntity;
import cn.mftcc.sys.components.sys.service.SysSecUserMarkInfoService;

/**
 * 用户记录信息
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-04 15:12:09
 */
@RestController
@RequestMapping("sys/sysSecUserMarkInfo")
public class SysSecUserMarkInfoController {

    @Autowired
    private SysSecUserMarkInfoService sysSecUserMarkInfoService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) {
        IPage<SysSecUserMarkInfoEntity> list = this.sysSecUserMarkInfoService.findByPage(sysSecUserMarkInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity){
        this.sysSecUserMarkInfoService.insert(sysSecUserMarkInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @PostMapping("/update")
    public R update(@RequestBody SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity){
        this.sysSecUserMarkInfoService.update(sysSecUserMarkInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{userId}")
    public R findById(@PathVariable("userId") String userId){
        SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity = this.sysSecUserMarkInfoService.findById(userId);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysSecUserMarkInfoEntity);
    }

    @DeleteMapping("/deleteById/{userId}")
    public R deleteById(@PathVariable("userId") String userId){
        this.sysSecUserMarkInfoService.deleteById(userId);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @PostMapping("/unlock")
    public R unlock(@RequestBody SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity){
        this.sysSecUserMarkInfoService.unlock(sysSecUserMarkInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }
}