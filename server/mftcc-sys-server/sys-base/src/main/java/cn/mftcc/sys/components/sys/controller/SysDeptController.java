/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;


import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.sys.components.sys.entity.SysDeptEntity;
import cn.mftcc.sys.components.sys.service.SysDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
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

@RestController
@RequestMapping("sys/sysDept")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysDeptEntity sysDeptEntity) {
        IPage<SysDeptEntity> list = this.sysDeptService.findByPage(sysDeptEntity);
        R r = R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
        if(StringUtils.isNotBlank(sysDeptEntity.getBrNo())){
            List<String> brNoChildren = sysDeptService.getChildrenBrNoList(sysDeptEntity.getBrNo(),sysDeptEntity.getCorpId());
            r.put("brNoChildren",brNoChildren);
        }
        return r;
    }

    @PostMapping("/findAllByPage")
    public R findAllByPage(@RequestBody SysDeptEntity sysDeptEntity) {
        IPage<SysDeptEntity> list = this.sysDeptService.findAllByPage(sysDeptEntity);
        R r = R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
        return r;
    }

    @PostMapping("/findByPage4admin")
    public R findByPage4admin(@RequestBody SysDeptEntity sysDeptEntity) {
        IPage<SysDeptEntity> list = this.sysDeptService.findByPage4admin(sysDeptEntity);
        R r = R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
        return r;
    }

    @GetMapping("/getList")
    public R getList() {
        List<SysDeptEntity> list = this.sysDeptService.getAll();
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/getAll")
    public R getAll() {
        List<SysDeptEntity> list = this.sysDeptService.getList(new SysDeptEntity());
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysDeptEntity sysDeptEntity) throws Exception {
        //判断机构号是否存在
        SysDeptEntity tmp=new SysDeptEntity();
        tmp.setBrNo(sysDeptEntity.getBrNo());
        if(this.sysDeptService.getList(tmp).size()>0){
            return R.error("机构号已存在");
        }else{
            tmp=new SysDeptEntity();
            tmp.setCorpId(sysDeptEntity.getCorpId());
            if("1".equals(sysDeptEntity.getIsCorp())&&this.sysDeptService.getList(tmp).size()>0){
                return R.error("法人机构号已存在");
            }
            sysDeptEntity.setBrSts("1");
            this.sysDeptService.insert(sysDeptEntity);
            return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS).put("brId",sysDeptEntity.getBrId());
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysDeptEntity sysDeptEntity){
        //判断机构号是否存在
        SysDeptEntity tmp=new SysDeptEntity();
        tmp.setBrNo(sysDeptEntity.getBrNo());
        List list=this.sysDeptService.getList(tmp);
        if(list.size()>0){
            tmp=(SysDeptEntity)list.get(0);
            if(!tmp.getBrId().equals(sysDeptEntity.getBrId())){
                return R.error("机构号已存在");
            }
        }
        this.sysDeptService.update(sysDeptEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{moldId}")
    public R findById(@PathVariable("moldId") String moldId){
        SysDeptEntity sysDeptEntity = this.sysDeptService.findById(moldId);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysDeptEntity);
    }

    @DeleteMapping("/deleteById/{brId}")
    public R deleteById(@PathVariable("brId") String brId){
        this.sysDeptService.deleteById(brId);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @DeleteMapping("/delBatch/{brId}")
    public R delBatch(@PathVariable("brId") Integer brId){
        this.sysDeptService.delBatch(brId);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @GetMapping("/getListByCorpId")
    public R getListByCorpId() {
        List<SysDeptEntity> list = this.sysDeptService.getListByCorpId();
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @GetMapping("/findByBrNo/{brNo}")
    public R findByBrNo(@PathVariable("brNo") String brNo){
        SysDeptEntity sysDeptEntity = this.sysDeptService.findByBrNo(brNo);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", sysDeptEntity);
    }
}
