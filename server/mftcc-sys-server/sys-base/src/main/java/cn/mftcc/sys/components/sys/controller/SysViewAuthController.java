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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.mftcc.sys.components.sys.entity.SysViewAuthEntity;
import cn.mftcc.sys.components.sys.service.SysViewAuthService;

/**
 * 视角权限配置
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 15:00:10
 */
@RestController
@RequestMapping("sys/sysViewAuth")
public class SysViewAuthController {

    @Autowired
    private SysViewAuthService sysViewAuthService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysViewAuthEntity sysViewAuthEntity) {
        IPage<SysViewAuthEntity> list = this.sysViewAuthService.findByPage(sysViewAuthEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysViewAuthEntity sysViewAuthEntity){
        try {
            this.sysViewAuthService.insert(sysViewAuthEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody SysViewAuthEntity sysViewAuthEntity){
        try {
            this.sysViewAuthService.update(sysViewAuthEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{id}")
    public R findById(@PathVariable("id") String id){
        try {
            SysViewAuthEntity sysViewAuthEntity = this.sysViewAuthService.findById(id);
            return R.ok().put("data", sysViewAuthEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @GetMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") String id){
        try {
            this.sysViewAuthService.deleteById(id);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}