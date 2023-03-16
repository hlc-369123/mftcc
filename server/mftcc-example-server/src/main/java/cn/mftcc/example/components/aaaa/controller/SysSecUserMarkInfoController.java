package cn.mftcc.example.components.aaaa.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.example.components.aaaa.entity.SysSecUserMarkInfoEntity;
import cn.mftcc.example.components.aaaa.service.SysSecUserMarkInfoService;

/**
 * 用户记录信息
 *
 * @author guohanchen
 * @email 
 * @date 2023-03-15 14:53:47
 */
@RestController
@RequestMapping("aaaa/sysSecUserMarkInfo")
public class SysSecUserMarkInfoController {

    @Autowired
    private SysSecUserMarkInfoService sysSecUserMarkInfoService;

    @RequestMapping("/findByPage")
    public R findByPage(@RequestBody SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity) {
        IPage<SysSecUserMarkInfoEntity> list = this.sysSecUserMarkInfoService.findByPage(sysSecUserMarkInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @RequestMapping("/insert")
    public R insert(@RequestBody SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity){
        this.sysSecUserMarkInfoService.insert(sysSecUserMarkInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
    }

    @RequestMapping("/update")
    public R update(@RequestBody SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity){
        this.sysSecUserMarkInfoService.update(sysSecUserMarkInfoEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @RequestMapping("/findById/{opNo}")
    public R findById(@PathVariable("opNo") String opNo){
        SysSecUserMarkInfoEntity sysSecUserMarkInfoEntity = this.sysSecUserMarkInfoService.findById(opNo);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysSecUserMarkInfoEntity);
    }

    @RequestMapping("/deleteById/{opNo}")
    public R deleteById(@PathVariable("opNo") String opNo){
        this.sysSecUserMarkInfoService.deleteById(opNo);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }
}