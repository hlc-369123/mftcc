/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import cn.mftcc.sys.components.demo.entity.DemoCusInfoEntity;
import cn.mftcc.sys.components.demo.service.DemoCusInfoService;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 14:00:20
 */
@RestController
@RequestMapping("demo/demoCusInfo")
public class DemoCusInfoController {

    @Autowired
    private DemoCusInfoService demoCusInfoService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody DemoCusInfoEntity demoCusInfoEntity) {
        IPage<DemoCusInfoEntity> list = this.demoCusInfoService.findByPage(demoCusInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody DemoCusInfoEntity demoCusInfoEntity){
        this.demoCusInfoService.insert(demoCusInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @PostMapping("/update")
    public R update(@RequestBody DemoCusInfoEntity demoCusInfoEntity){
        this.demoCusInfoService.update(demoCusInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{cusno}")
    public R findById(@PathVariable("cusno") String cusno){
        DemoCusInfoEntity demoCusInfoEntity = this.demoCusInfoService.findById(cusno);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", demoCusInfoEntity);
    }

    @GetMapping("/deleteById/{cusno}")
    public R deleteById(@PathVariable("cusno") String cusno){
        this.demoCusInfoService.deleteById(cusno);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @PostMapping("/findListDemo")
    public R findListDemo(){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<5;i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","业务"+i);
            jsonObject.put("value",i);
            jsonArray.add(jsonObject);
        }
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", jsonArray);
    }
}