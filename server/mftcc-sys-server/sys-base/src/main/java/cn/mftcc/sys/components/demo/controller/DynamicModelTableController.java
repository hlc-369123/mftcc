/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.components.demo.entity.DynamicModelTableEntity;
import cn.mftcc.sys.components.demo.service.DynamicModelTableService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 融资项目表
 *
 * @author zhanglingnan
 * @email 1670680235@qq.com
 * @date 2020-07-03 16:25:37
 */
@RestController
@RequestMapping("demo/dynamicModelTable")
public class DynamicModelTableController {

    @Autowired
    private DynamicModelTableService dynamicModelTableService;

    @Autowired
    private RequestUtil requestUtil;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody DynamicModelTableEntity leaseFinProjectEntity) {
        IPage<DynamicModelTableEntity> list = this.dynamicModelTableService.findByPage(leaseFinProjectEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    /*@RequestMapping("/input/{finId}")
    public R input(@PathVariable("finId") String finId){
        DynamicModelTableEntity leaseFinProjectEntity = new DynamicModelTableEntity();
        leaseFinProjectEntity.setRegNo(requestUtil.getUserInfo("opNo").toString());
        leaseFinProjectEntity.setRegName(requestUtil.getUserInfo("opName").toString());
        leaseFinProjectEntity.setOrgNo(requestUtil.getUserInfo("brNo").toString());
        leaseFinProjectEntity.setOrgName(requestUtil.getUserInfo("brName").toString());
        leaseFinProjectEntity.setRegDate(DateUtil.getSysDate());
        leaseFinProjectEntity.setLstDate(DateUtil.getSysDate());
        leaseFinProjectEntity.setLstRegNo(requestUtil.getUserInfo("opNo").toString());
        leaseFinProjectEntity.setLstRegName(requestUtil.getUserInfo("opName").toString());
        leaseFinProjectEntity.setLstOrgNo(requestUtil.getUserInfo("brNo").toString());
        leaseFinProjectEntity.setLstOrgName(requestUtil.getUserInfo("brName").toString());
        leaseFinProjectEntity.setRegTime(null);
        leaseFinProjectEntity.setLstTime(null);
        leaseFinProjectEntity.setFinNo(CreateKeyUtil.getSimpleSerNum(PUBParm.LEASE_FIN_PROJECT_SEQ, 10));
        leaseFinProjectEntity.setFinProSts(PUBParm.LEASE_FIN_PROJECT_STS_0);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", leaseFinProjectEntity);
    }*/

    @PostMapping("/insert")
    public R insert(@RequestBody DynamicModelTableEntity leaseFinProjectEntity){
        leaseFinProjectEntity.setCorpId(requestUtil.getUserInfo("corpId").toString());
        this.dynamicModelTableService.insert(leaseFinProjectEntity);
        return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS).put("data", leaseFinProjectEntity);
    }

    @PostMapping("/update")
    public R update(@RequestBody DynamicModelTableEntity leaseFinProjectEntity){
        this.dynamicModelTableService.update(leaseFinProjectEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    @GetMapping("/findById/{finId}")
    public R findById(@PathVariable("finId") String finId){
        DynamicModelTableEntity leaseFinProjectEntity = this.dynamicModelTableService.findById(finId);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data", leaseFinProjectEntity);
    }

    @GetMapping("/deleteById/{finId}")
    public R deleteById(@PathVariable("finId") String finId){
        this.dynamicModelTableService.deleteById(finId);
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }


}
