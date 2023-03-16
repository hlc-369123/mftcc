/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.demo.service.DemoService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 14:00:20
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @PostMapping("/saveEditTable")
    public R saveEditTable(@RequestBody JSONObject jsonObject) {
        this.demoService.saveEditTable(jsonObject);
        return R.ok(SysConstant.MSG_CONFIG_SUCCESS);
    }

    @PostMapping("/testFlowable")
    public R testFlowable(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject.toJSONString());
        return R.ok(SysConstant.MSG_CONFIG_SUCCESS);
    }
    @PostMapping("/SysAreaList")
    public R SysAreaList(@RequestBody JSONObject jsonObject) {
        List<Map<String, Object>> maps = this.demoService.SysAreaList(jsonObject);
        return R.ok(SysConstant.MSG_CONFIG_SUCCESS).put("list", maps);
    }

    @PostMapping("/getTableData")
    public R getTableData(@RequestBody JSONObject jsonObject) {
        List<Map<String, Object>> maps = this.demoService.SysAreaList(jsonObject);
        return R.ok(SysConstant.MSG_CONFIG_SUCCESS).put("list", maps);
    }

}