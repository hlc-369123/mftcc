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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.mftcc.sys.components.sys.entity.SysDataAuthEntity;
import cn.mftcc.sys.components.sys.service.SysDataAuthService;

/**
 * 数据权限配置表
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-20 11:15:06
 */
@RestController
@RequestMapping("sys/sysDataAuth")
public class SysDataAuthController {

    @Autowired
    private SysDataAuthService sysDataAuthService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysDataAuthEntity sysDataAuthEntity) {
        IPage<SysDataAuthEntity> list = this.sysDataAuthService.findByPage(sysDataAuthEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysDataAuthEntity sysDataAuthEntity){
        try {
            this.sysDataAuthService.insert(sysDataAuthEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("操作失败");
        }
    }

    @PostMapping("/update")
    public R update(@RequestBody SysDataAuthEntity sysDataAuthEntity){
        try {
            this.sysDataAuthService.update(sysDataAuthEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{id}")
    public R findById(@PathVariable("id") String id){
        try {
            SysDataAuthEntity sysDataAuthEntity = this.sysDataAuthService.findById(id);
            return R.ok().put("data", sysDataAuthEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") String id){
        try {
            this.sysDataAuthService.deleteById(id);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    @GetMapping("/getDataAuthByModelId/{modelId}/{roleId}")
    public R getDataAuthByModelId(@PathVariable("modelId") String modelId,@PathVariable("roleId") String roleId){
        try {
            SysDataAuthEntity sysDataAuthEntity = sysDataAuthService.getDataAuthByModelId(modelId,roleId);
//            R r = R.ok();
//            if(sysDataAuthEntity != null){
//                r.put("authType",sysDataAuthEntity.getAuthType());
//            }
            return R.ok().put("authData",sysDataAuthEntity);
        } catch (Exception e) {
            return R.error("获取权限失败");
        }
    }
}