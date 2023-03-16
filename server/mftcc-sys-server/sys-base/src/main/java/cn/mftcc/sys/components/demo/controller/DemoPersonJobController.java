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
import cn.mftcc.sys.components.demo.entity.DemoPersonJobEntity;
import cn.mftcc.sys.components.demo.service.DemoPersonJobService;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 17:16:55
 */
@RestController
@RequestMapping("demo/demoPersonJob")
public class DemoPersonJobController {

    @Autowired
    private DemoPersonJobService demoPersonJobService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody DemoPersonJobEntity demoPersonJobEntity) {
        IPage<DemoPersonJobEntity> list = this.demoPersonJobService.findByPage(demoPersonJobEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody DemoPersonJobEntity demoPersonJobEntity){
        this.demoPersonJobService.insert(demoPersonJobEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @PostMapping("/update")
    public R update(@RequestBody DemoPersonJobEntity demoPersonJobEntity){
        this.demoPersonJobService.update(demoPersonJobEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{workId}")
    public R findById(@PathVariable("workId") String workId){
        DemoPersonJobEntity demoPersonJobEntity = this.demoPersonJobService.findById(workId);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", demoPersonJobEntity);
    }

    @GetMapping("/deleteById/{workId}")
    public R deleteById(@PathVariable("workId") String workId){
        this.demoPersonJobService.deleteById(workId);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }
}