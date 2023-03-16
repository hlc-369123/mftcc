/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.initStyle.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.initStyle.entity.SysInitStyleEntity;
import cn.mftcc.sys.components.initStyle.service.SysInitStyleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 *
 * @author guohanchen
 * @email
 * @date 2021-05-27 08:34:16
 */
@RestController
@RequestMapping("sys/sysInitStyle")
public class SysInitStyleController {

    @Autowired
    private SysInitStyleService sysInitStyleService;

    @RequestMapping("/findByPage")
    public R findByPage(@RequestBody SysInitStyleEntity sysInitStyleEntity) {
        IPage<SysInitStyleEntity> list = this.sysInitStyleService.findByPage(sysInitStyleEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @RequestMapping("/insert")
    public R insert(@RequestBody SysInitStyleEntity sysInitStyleEntity){
        this.sysInitStyleService.insert(sysInitStyleEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @RequestMapping("/update")
    public R update(@RequestBody SysInitStyleEntity sysInitStyleEntity){
        this.sysInitStyleService.update(sysInitStyleEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @RequestMapping("/findById/{id}")
    public R findById(@PathVariable("id") Integer id){
        SysInitStyleEntity sysInitStyleEntity = this.sysInitStyleService.findById(id);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysInitStyleEntity);
    }

    @RequestMapping("/deleteById/{id}")
    public R deleteById(@PathVariable("id") Integer id){
        this.sysInitStyleService.deleteById(id);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @RequestMapping("/findList")
    public R findList(@RequestBody Map<String,String> map){
        String companyId = map.get("companyId");
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", this.sysInitStyleService.findListByCompanyId(companyId));
    }
}
