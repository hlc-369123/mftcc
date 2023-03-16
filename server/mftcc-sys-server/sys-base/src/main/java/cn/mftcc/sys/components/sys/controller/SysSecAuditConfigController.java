/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.sys.entity.SysSecAuditConfigEntity;
import cn.mftcc.sys.components.sys.service.SysSecAuditConfigService;
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
import java.util.Map;

/**
 * 登陆校验规则表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-06-03 13:42:15
 */
@RestController
@RequestMapping("sys/sysSecAuditConfig")
public class SysSecAuditConfigController {

    @Autowired
    private SysSecAuditConfigService sysSecAuditConfigService;

    @PostMapping("/findList")
    public R findList(@RequestBody SysSecAuditConfigEntity sysSecAuditConfigEntity) {
        List<SysSecAuditConfigEntity> list = this.sysSecAuditConfigService.findList(sysSecAuditConfigEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysSecAuditConfigEntity sysSecAuditConfigEntity) {
        IPage<SysSecAuditConfigEntity> list = this.sysSecAuditConfigService.findByPage(sysSecAuditConfigEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysSecAuditConfigEntity sysSecAuditConfigEntity){
        this.sysSecAuditConfigService.insert(sysSecAuditConfigEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @PutMapping("/update")
    public R update(@RequestBody SysSecAuditConfigEntity sysSecAuditConfigEntity){
        this.sysSecAuditConfigService.update(sysSecAuditConfigEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{itemNo}")
    public R findById(@PathVariable("itemNo") String itemNo){
        SysSecAuditConfigEntity sysSecAuditConfigEntity = this.sysSecAuditConfigService.findById(itemNo);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysSecAuditConfigEntity);
    }

    @DeleteMapping("/deleteById/{itemNo}")
    public R deleteById(@PathVariable("itemNo") String itemNo){
        this.sysSecAuditConfigService.deleteById(itemNo);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    /***
     * 校验密码
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/validatePWAjax")
    public R validatePWAjax(@RequestBody Map<String,String> parm) throws Exception {
        JSONObject result = this.sysSecAuditConfigService.validatePWAjax(parm.get("opNo"),parm.get("password"));
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data",result);
    }
}