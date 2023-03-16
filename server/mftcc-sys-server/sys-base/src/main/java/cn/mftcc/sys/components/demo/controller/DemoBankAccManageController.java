/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.demo.entity.DemoBankAccManageEntity;
import cn.mftcc.sys.components.demo.service.DemoBankAccManageService;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 17:02:01
 */
@RestController
@RequestMapping("demo/demoBankAccManage")
public class DemoBankAccManageController {

    @Autowired
    private DemoBankAccManageService demoBankAccManageService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody DemoBankAccManageEntity demoBankAccManageEntity) {
        IPage<DemoBankAccManageEntity> list = this.demoBankAccManageService.findByPage(demoBankAccManageEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody DemoBankAccManageEntity demoBankAccManageEntity){
        this.demoBankAccManageService.insert(demoBankAccManageEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @PostMapping("/update")
    public R update(@RequestBody DemoBankAccManageEntity demoBankAccManageEntity){
        this.demoBankAccManageService.update(demoBankAccManageEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{accountNo}")
    public R findById(@PathVariable("accountNo") String accountNo){
        DemoBankAccManageEntity demoBankAccManageEntity = this.demoBankAccManageService.findById(accountNo);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", demoBankAccManageEntity);
    }

    @GetMapping("/deleteById/{accountNo}")
    public R deleteById(@PathVariable("accountNo") String accountNo){
        this.demoBankAccManageService.deleteById(accountNo);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }
}