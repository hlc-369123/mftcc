/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.sys.components.sys.entity.SysMenuEntity;
import cn.mftcc.sys.components.sys.service.SysMenuService;
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

/**
 * 系统菜单表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-05 12:11:55
 */
@RestController
@RequestMapping("sys/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysMenuEntity sysMenuEntity) {
        IPage<SysMenuEntity> list = this.sysMenuService.findByPage(sysMenuEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysMenuEntity sysMenuEntity){
        try {
            this.sysMenuService.insert(sysMenuEntity);
            return R.ok().put("menuId",sysMenuEntity.getMenuId());
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysMenuEntity sysMenuEntity){
        try {
            this.sysMenuService.update(sysMenuEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{menuId}")
    public R findById(@PathVariable("menuId") String menuId){
        try {
            SysMenuEntity sysMenuEntity = this.sysMenuService.findById(menuId);
            return R.ok().put("data", sysMenuEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{menuId}")
    public R deleteById(@PathVariable("menuId") String menuId){
        try {
            this.sysMenuService.deleteById(menuId);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}