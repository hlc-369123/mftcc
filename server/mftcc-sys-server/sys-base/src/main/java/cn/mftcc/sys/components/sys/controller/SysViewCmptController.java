/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.sys.components.sys.entity.SysViewCmptEntity;
import cn.mftcc.sys.components.sys.service.SysViewCmptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 综合视图组件定义
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-06 15:41:56
 */
@RestController
@RequestMapping("sys/sysViewCmpt")
public class SysViewCmptController {

    @Autowired
    private SysViewCmptService sysViewCmptService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysViewCmptEntity sysViewCmptEntity) {
        IPage<SysViewCmptEntity> list = this.sysViewCmptService.findByPage(sysViewCmptEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysViewCmptEntity sysViewCmptEntity){
        try {
            this.sysViewCmptService.insert(sysViewCmptEntity);
            return R.ok().put("viewCmptId",sysViewCmptEntity.getViewCmptId());
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysViewCmptEntity sysViewCmptEntity){
        try {
            this.sysViewCmptService.update(sysViewCmptEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{viewCmptId}")
    public R findById(@PathVariable("viewCmptId") String viewCmptId){
        try {
            SysViewCmptEntity sysViewCmptEntity = this.sysViewCmptService.findById(viewCmptId);
            return R.ok().put("data", sysViewCmptEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @GetMapping("/deleteById/{viewCmptId}")
    public R deleteById(@PathVariable("viewCmptId") String viewCmptId){
        try {
            this.sysViewCmptService.deleteById(viewCmptId);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}