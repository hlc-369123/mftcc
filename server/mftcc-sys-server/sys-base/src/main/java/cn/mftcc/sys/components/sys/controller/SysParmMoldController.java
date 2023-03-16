/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.sys.components.sys.entity.SysParmMoldEntity;
import cn.mftcc.sys.components.sys.service.SysParmMoldService;
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
 * 字典项类别
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-25 18:18:49
 */
@RestController
@RequestMapping("sys/sysParmMold")
public class SysParmMoldController {

    @Autowired
    private SysParmMoldService sysParmMoldService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysParmMoldEntity sysParmMoldEntity) {
        IPage<SysParmMoldEntity> list = this.sysParmMoldService.findByPage(sysParmMoldEntity);
        return R.ok().put("list", list);
    }

    @GetMapping("/getList")
    public R getList() {
        List<SysParmMoldEntity> list = this.sysParmMoldService.getList();
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysParmMoldEntity sysParmMoldEntity){
        try {
            this.sysParmMoldService.insert(sysParmMoldEntity);
            return R.ok().put("moldId",sysParmMoldEntity.getMoldId());
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysParmMoldEntity sysParmMoldEntity){
        try {
            this.sysParmMoldService.update(sysParmMoldEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{moldId}")
    public R findById(@PathVariable("moldId") String moldId){
        try {
            SysParmMoldEntity parmMoldEntity = this.sysParmMoldService.findById(moldId);
            return R.ok().put("data", parmMoldEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{moldId}")
    public R deleteById(@PathVariable("moldId") String moldId){
        try {
            this.sysParmMoldService.deleteById(moldId);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    @DeleteMapping("/delBatch/{moldId}")
    public R delBatch(@PathVariable("moldId") String moldId){
        try {
            this.sysParmMoldService.delBatch(moldId);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}