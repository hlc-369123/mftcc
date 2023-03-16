/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.sys.components.sys.entity.SysImportEntity;
import cn.mftcc.sys.components.sys.entity.SysModelEntity;
import cn.mftcc.sys.components.sys.service.SysImportService;
import cn.mftcc.sys.components.sys.service.SysModelService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import java.util.List;

/**
 * 功能定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-12 10:12:38
 */
@RestController
@RequestMapping("sys/sysModel")
public class SysModelController {

    @Autowired
    private SysModelService sysModelService;
    @Autowired
    private SysImportService sysImportService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysModelEntity sysModelEntity) {
        IPage<SysModelEntity> list = this.sysModelService.findByPage(sysModelEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody JSONObject jsonObject){
        try {
            SysModelEntity sysModelEntity = jsonObject.toJavaObject(SysModelEntity.class);
            this.sysModelService.insert(sysModelEntity);
            SysImportEntity sysImportEntity = jsonObject.toJavaObject(SysImportEntity.class);
            sysImportEntity.setCmptId(sysModelEntity.getModelId());
            sysImportEntity.setCmptTyp(sysModelEntity.getModelTyp());
            this.sysImportService.insert(sysImportEntity);
            return R.ok().put("modelId",sysModelEntity.getModelId());
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysModelEntity sysModelEntity){
        try {
            this.sysModelService.update(sysModelEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{modelId}")
    public R findById(@PathVariable("modelId") String modelId){
        try {
            SysModelEntity sysModelEntity = this.sysModelService.findById(modelId);
            return R.ok().put("data", sysModelEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{modelId}")
    public R deleteById(@PathVariable("modelId") String modelId){
        try {
            this.sysModelService.deleteById(modelId);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    @GetMapping("/findAllList")
    public R findAllList() {
        List<SysModelEntity> list = this.sysModelService.findAllList();
        return R.ok().put("list", list);
    }

    @PostMapping("/findUrlAuthByModelId")
    public R findUrlAuthByModelId(@RequestBody SysModelEntity sysModelEntity) {
        JSONArray list = this.sysModelService.findUrlAuthByModelId(sysModelEntity.getModelId());
        return R.ok().put("list", list);
    }

    @PostMapping("/deleteUrlAuthByModelId")
    public R deleteUrlAuthByModelId(@RequestBody JSONObject parm){
        this.sysModelService.deleteUrlAuthByModelId(parm);
        return R.ok();
    }
}