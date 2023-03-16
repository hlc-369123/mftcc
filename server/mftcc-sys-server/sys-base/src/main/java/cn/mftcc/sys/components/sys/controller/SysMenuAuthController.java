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

import cn.mftcc.sys.components.sys.entity.SysMenuAuthEntity;
import cn.mftcc.sys.components.sys.service.SysMenuAuthService;

/**
 * 菜单权限表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-18 11:35:29
 */
@RestController
@RequestMapping("sys/sysMenuAuth")
public class SysMenuAuthController {

    @Autowired
    private SysMenuAuthService sysMenuAuthService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysMenuAuthEntity sysMenuAuthEntity) {
        IPage<SysMenuAuthEntity> list = this.sysMenuAuthService.findByPage(sysMenuAuthEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysMenuAuthEntity sysMenuAuthEntity){
        try {
            this.sysMenuAuthService.insert(sysMenuAuthEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody SysMenuAuthEntity sysMenuAuthEntity){
        try {
            this.sysMenuAuthService.update(sysMenuAuthEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{id}")
    public R findById(@PathVariable("id") String id){
        try {
            SysMenuAuthEntity sysMenuAuthEntity = this.sysMenuAuthService.findById(id);
            return R.ok().put("data", sysMenuAuthEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") String id){
        try {
            this.sysMenuAuthService.deleteById(id);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}