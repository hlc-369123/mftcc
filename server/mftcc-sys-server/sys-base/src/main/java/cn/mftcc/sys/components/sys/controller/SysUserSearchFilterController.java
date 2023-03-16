/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.sys.entity.SysUserSearchFilterEntity;
import cn.mftcc.sys.components.sys.service.SysUserSearchFilterService;

import java.util.List;

/**
 * 筛选条件存储表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-05-23 09:42:01
 */
@RestController
@RequestMapping("sys/sysUserSearchFilter")
public class SysUserSearchFilterController {

    @Autowired
    private SysUserSearchFilterService sysUserSearchFilterService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysUserSearchFilterEntity sysUserSearchFilterEntity) {
        IPage<SysUserSearchFilterEntity> list = this.sysUserSearchFilterService.findByPage(sysUserSearchFilterEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysUserSearchFilterEntity sysUserSearchFilterEntity){
        sysUserSearchFilterEntity.setFilterNo(null);
        SysUserSearchFilterEntity sysUserSearchFilterEntitys = this.sysUserSearchFilterService.insert(sysUserSearchFilterEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS).put("data",sysUserSearchFilterEntitys);
    }

    @PostMapping("/update")
    public R update(@RequestBody SysUserSearchFilterEntity sysUserSearchFilterEntity){
        this.sysUserSearchFilterService.update(sysUserSearchFilterEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{filterNo}")
    public R findById(@PathVariable("filterNo") String filterNo){
        SysUserSearchFilterEntity sysUserSearchFilterEntity = this.sysUserSearchFilterService.findById(filterNo);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysUserSearchFilterEntity);
    }

    @GetMapping("/findByVue/{vue}")
    public R findByVue(@PathVariable("vue") String vue){
        List<SysUserSearchFilterEntity> sysUserSearchFilterEntity = this.sysUserSearchFilterService.findByVue(vue);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", sysUserSearchFilterEntity);
    }

    @GetMapping("/deleteById/{filterNo}")
    public R deleteById(@PathVariable("filterNo") String filterNo){
        this.sysUserSearchFilterService.deleteById(filterNo);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }
}