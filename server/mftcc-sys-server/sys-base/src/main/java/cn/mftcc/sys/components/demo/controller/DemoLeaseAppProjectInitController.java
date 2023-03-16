/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.demo.entity.DemoLeaseAppProjectInitEntity;
import cn.mftcc.sys.components.demo.service.DemoLeaseAppProjectInitService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目登记表
 *
 * @author guohanchen
 * @email 
 * @date 2021-03-19 10:42:18
 */
@RestController
@RequestMapping("demo/demoLeaseAppProjectInit")
public class DemoLeaseAppProjectInitController {

    @Autowired
    private DemoLeaseAppProjectInitService demoLeaseAppProjectInitService;

    @RequestMapping("/findByPage")
    public R findByPage(@RequestBody DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity) {
        IPage<DemoLeaseAppProjectInitEntity> list = this.demoLeaseAppProjectInitService.findByPage(demoLeaseAppProjectInitEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @RequestMapping("/insert")
    public R insert(@RequestBody DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity){
        this.demoLeaseAppProjectInitService.insert(demoLeaseAppProjectInitEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @RequestMapping("/update")
    public R update(@RequestBody DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity){
        this.demoLeaseAppProjectInitService.update(demoLeaseAppProjectInitEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @RequestMapping("/findById/{appId}")
    public R findById(@PathVariable("appId") String appId){
        DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity = this.demoLeaseAppProjectInitService.findById(appId);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", demoLeaseAppProjectInitEntity);
    }

    @RequestMapping("/deleteById/{appId}")
    public R deleteById(@PathVariable("appId") String appId){
        this.demoLeaseAppProjectInitService.deleteById(appId);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @RequestMapping("/saveEditTable")
    public R saveEditTable(@RequestBody JSONObject jsonObject) {
        this.demoLeaseAppProjectInitService.saveEditTable(jsonObject);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @RequestMapping("/sendMessage")
    public R sendMessage(@RequestBody JSONObject jsonObject) {
        this.demoLeaseAppProjectInitService.sendMessage(jsonObject);
        return R.ok(SysConstant.MSG_CONFIG_SUCCESS);
    }

}