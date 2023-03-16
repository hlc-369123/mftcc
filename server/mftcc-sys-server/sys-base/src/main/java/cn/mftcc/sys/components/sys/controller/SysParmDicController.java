/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.utils.ParmCacheUtil;
import cn.mftcc.sys.common.runner.CacheUtil;
import cn.mftcc.sys.components.sys.entity.SysParmDicEntity;
import cn.mftcc.sys.components.sys.service.SysParmDicService;
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
 * 字典项
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-26 10:34:15
 */
@RestController
@RequestMapping("sys/sysParmDic")
public class SysParmDicController {

    @Autowired
    private SysParmDicService sysParmDicService;

    @Autowired
    private ParmCacheUtil parmCacheUtil;

    @Autowired
    private CacheUtil cacheUtil;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysParmDicEntity sysParmDicEntity) {
        IPage<SysParmDicEntity> list = this.sysParmDicService.findByPage(sysParmDicEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysParmDicEntity sysParmDicEntity){
        try {
            SysParmDicEntity sysParmDicEntitys = this.sysParmDicService.findByKeyNameAndOptCode(sysParmDicEntity);
            if(sysParmDicEntitys == null){
                this.sysParmDicService.insert(sysParmDicEntity);
                return R.ok();
            }else{
                return R.error("新增失败,数据已存在");
            }
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysParmDicEntity sysParmDicEntity){
        try {
            this.sysParmDicService.update(sysParmDicEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{keyName}")
    public R findById(@PathVariable("keyName") String keyName){
        try {
            SysParmDicEntity parmDicEntity = this.sysParmDicService.findById(keyName);
            return R.ok().put("data", parmDicEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{keyName}")
    public R deleteById(@PathVariable("keyName") String keyName){
        try {
            this.sysParmDicService.deleteById(keyName);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    @PostMapping("/initCaChe")
    public R initCaChe() {
        try {
            cacheUtil.initParmCaChe();
            return R.ok();
        }catch (Exception e){
            return R.error();
        }
    }
}