/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.mftcc.sys.components.sys.entity.SysImportEntity;
import cn.mftcc.sys.components.sys.service.SysImportService;

/**
 * 引用对照表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-12 11:19:13
 */
@RestController
@RequestMapping("sys/sysImport")
public class SysImportController {

    @Autowired
    private SysImportService sysImportService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysImportEntity sysImportEntity) {
        IPage<SysImportEntity> list = this.sysImportService.findByPage(sysImportEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysImportEntity sysImportEntity){
        try {
            this.sysImportService.insert(sysImportEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PostMapping("/insertModel")
    public R insertModel(@RequestBody JSONObject jsonObject){
        try {
            JSONArray importArr = jsonObject.getJSONArray("importArr");
            for(int i = 0;i<importArr.size();i++){
                SysImportEntity sysImportEntity = importArr.getJSONObject(i).toJavaObject(SysImportEntity.class);
                this.sysImportService.insert(sysImportEntity);
            }
            return R.ok();
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody SysImportEntity sysImportEntity){
        try {
            this.sysImportService.update(sysImportEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{id}")
    public R findById(@PathVariable("id") String id){
        try {
            SysImportEntity sysImportEntity = this.sysImportService.findById(id);
            return R.ok().put("data", sysImportEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") String id){
        try {
            this.sysImportService.deleteById(id);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}