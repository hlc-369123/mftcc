/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.sys.entity.SysRoleEntity;
import cn.mftcc.sys.components.sys.service.SysRoleService;
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
 * 角色
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-21 16:55:37
 */
@RestController
@RequestMapping("sys/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysRoleEntity sysRoleEntity) {
        IPage<SysRoleEntity> list = this.sysRoleService.findByPage(sysRoleEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/findByPage4admin")
    public R findByPage4admin(@RequestBody SysRoleEntity sysRoleEntity) {
        IPage<SysRoleEntity> list = this.sysRoleService.findByPage4admin(sysRoleEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/findAll")
    public R findAll() {
        List<SysRoleEntity> list = this.sysRoleService.findAll();
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysRoleEntity sysRoleEntity){
        this.sysRoleService.insert(sysRoleEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @PutMapping("/update")
    public R update(@RequestBody SysRoleEntity sysRoleEntity){
        this.sysRoleService.update(sysRoleEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{roleId}")
    public R findById(@PathVariable("roleId") String roleId){
        SysRoleEntity sysRoleEntity = this.sysRoleService.findById(roleId);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysRoleEntity);
    }

    @DeleteMapping("/deleteById/{roleId}")
    public R deleteById(@PathVariable("roleId") String roleId){
        this.sysRoleService.deleteById(roleId);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @PostMapping("/findListByCorpId")
    public R findListByCorpId(){
        List<SysRoleEntity> list = this.sysRoleService.findListByCorpId();
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }
}