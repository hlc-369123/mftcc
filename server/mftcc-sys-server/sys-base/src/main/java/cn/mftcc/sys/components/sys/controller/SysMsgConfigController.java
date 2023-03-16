/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.sysutils.MsgCacheUtil;
import cn.mftcc.sys.common.runner.CacheUtil;
import cn.mftcc.sys.components.sys.entity.SysMsgConfigEntity;
import cn.mftcc.sys.components.sys.service.SysMsgConfigService;
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
 * 信息码配置表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-27 14:10:21
 */
@RestController
@RequestMapping("sys/sysMsgConfig")
public class SysMsgConfigController {

    @Autowired
    private SysMsgConfigService sysMsgConfigService;

    @Autowired
    private MsgCacheUtil msgCacheUtil;

    @Autowired
    private CacheUtil cacheUtil;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysMsgConfigEntity sysMsgConfigEntity) {
        IPage<SysMsgConfigEntity> list = this.sysMsgConfigService.findByPage(sysMsgConfigEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysMsgConfigEntity sysMsgConfigEntity){
        try {
            SysMsgConfigEntity sysMsgConfigEntitys = this.sysMsgConfigService.findById(sysMsgConfigEntity.getMsgCode());
            if(sysMsgConfigEntitys == null){
                this.sysMsgConfigService.insert(sysMsgConfigEntity);
                return R.ok();
            }else{
                return R.error("新增失败,数据已存在");
            }
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysMsgConfigEntity sysMsgConfigEntity){
        try {
            this.sysMsgConfigService.update(sysMsgConfigEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{msgCode}")
    public R findById(@PathVariable("msgCode") String msgCode){
        try {
            SysMsgConfigEntity sysMsgConfigEntity = this.sysMsgConfigService.findById(msgCode);
            return R.ok().put("data", sysMsgConfigEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{msgCode}")
    public R deleteById(@PathVariable("msgCode") String msgCode){
        try {
            this.sysMsgConfigService.deleteById(msgCode);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    @PostMapping("/initCaChe")
    public R initCaChe() {
        try {
            cacheUtil.initMsgCaChe();
            return R.ok();
        }catch (Exception e){
            return R.error();
        }
    }
}