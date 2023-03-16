/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.sys.components.sys.entity.SysMsgMoldEntity;
import cn.mftcc.sys.components.sys.service.SysMsgMoldService;
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
 * 错误码类别表
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-27 16:03:49
 */
@RestController
@RequestMapping("sys/sysMsgMold")
public class SysMsgMoldController {

    @Autowired
    private SysMsgMoldService sysMsgMoldService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysMsgMoldEntity sysMsgMoldEntity) {
        IPage<SysMsgMoldEntity> list = this.sysMsgMoldService.findByPage(sysMsgMoldEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysMsgMoldEntity sysMsgMoldEntity){
        try {
            this.sysMsgMoldService.insert(sysMsgMoldEntity);
            return R.ok().put("moldId",sysMsgMoldEntity.getMoldId());
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysMsgMoldEntity sysMsgMoldEntity){
        try {
            this.sysMsgMoldService.update(sysMsgMoldEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{moldId}")
    public R findById(@PathVariable("moldId") String moldId){
        try {
            SysMsgMoldEntity sysMsgMoldEntity = this.sysMsgMoldService.findById(moldId);
            return R.ok().put("data", sysMsgMoldEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @GetMapping("/deleteById/{moldId}")
    public R deleteById(@PathVariable("moldId") String moldId){
        try {
            this.sysMsgMoldService.deleteById(moldId);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    @GetMapping("/getList")
    public R getList() {
        List<SysMsgMoldEntity> list = this.sysMsgMoldService.getList();
        return R.ok().put("list", list);
    }

    @DeleteMapping("/delBatch/{moldId}")
    public R delBatch(@PathVariable("moldId") String moldId){
        try {
            this.sysMsgMoldService.delBatch(moldId);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}