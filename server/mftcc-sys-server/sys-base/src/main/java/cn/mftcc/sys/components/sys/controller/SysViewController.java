/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.mftcc.sys.components.sys.entity.SysViewEntity;
import cn.mftcc.sys.components.sys.service.SysViewService;

/**
 * 综合视图定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-06 15:35:22
 */
@RestController
@RequestMapping("sys/sysView")
public class SysViewController {

    @Autowired
    private SysViewService sysViewService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysViewEntity sysViewEntity) {
        IPage<SysViewEntity> list = this.sysViewService.findByPage(sysViewEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysViewEntity sysViewEntity){
        try {
            this.sysViewService.insert(sysViewEntity);
            return R.ok().put("viewId",sysViewEntity.getViewId());
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysViewEntity sysViewEntity){
        try {
            this.sysViewService.update(sysViewEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{viewId}")
    public R findById(@PathVariable("viewId") String viewId){
        try {
            SysViewEntity sysViewEntity = this.sysViewService.findById(viewId);
            return R.ok().put("data", sysViewEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @GetMapping("/deleteById/{viewId}")
    public R deleteById(@PathVariable("viewId") String viewId){
        try {
            this.sysViewService.deleteById(viewId);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}