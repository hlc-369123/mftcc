/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.mftcc.sys.components.sys.entity.SysModelAuthEntity;
import cn.mftcc.sys.components.sys.service.SysModelAuthService;

/**
 * 功能权限配置
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 16:48:50
 */
@RestController
@RequestMapping("sys/sysModelAuth")
public class SysModelAuthController {

    @Autowired
    private SysModelAuthService sysModelAuthService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysModelAuthEntity sysModelAuthEntity) {
        IPage<SysModelAuthEntity> list = this.sysModelAuthService.findByPage(sysModelAuthEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysModelAuthEntity sysModelAuthEntity){
        try {
            this.sysModelAuthService.insert(sysModelAuthEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody SysModelAuthEntity sysModelAuthEntity){
        try {
            this.sysModelAuthService.update(sysModelAuthEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{id}")
    public R findById(@PathVariable("id") String id){
        try {
            SysModelAuthEntity sysModelAuthEntity = this.sysModelAuthService.findById(id);
            return R.ok().put("data", sysModelAuthEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") String id){
        try {
            this.sysModelAuthService.deleteById(id);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}