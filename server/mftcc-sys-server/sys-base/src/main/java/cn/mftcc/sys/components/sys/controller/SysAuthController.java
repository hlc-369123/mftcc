/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.sys.components.sys.entity.SysMenuEntity;
import cn.mftcc.sys.components.sys.entity.SysViewCmptEntity;
import cn.mftcc.sys.components.sys.entity.SysViewEntity;
import cn.mftcc.sys.components.sys.service.SysAuthService;
import cn.mftcc.sys.components.sys.service.SysImportService;
import cn.mftcc.sys.components.sys.service.SysMenuService;
import cn.mftcc.sys.components.sys.service.SysModelService;
import cn.mftcc.sys.components.sys.service.SysViewCmptService;
import cn.mftcc.sys.components.sys.service.SysViewService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统功能权限
 *
 * @author guohanchen
 * @email 
 * @date 2020-03-05 13:36:55
 */
@RestController
@RequestMapping("sys/sysAuth")
public class SysAuthController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysViewService sysViewService;
    @Autowired
    private SysViewCmptService sysViewCmptService;
    @Autowired
    private SysModelService sysModelService;
    @Autowired
    private SysImportService sysImportService;
    @Autowired
    private SysAuthService sysAuthService;



    @GetMapping("/getAll")
    public R getAll() {
        List<SysMenuEntity> menuList = sysMenuService.findAllList();
        List<SysViewEntity> viewList = sysViewService.findAllList();
        List<SysViewCmptEntity> viewCmptList = sysViewCmptService.findAllList();
        JSONArray modelList = sysModelService.findListJionImport();
        return R.ok()
                .put("menuList", menuList)
                .put("viewList", viewList)
                .put("viewCmptList",viewCmptList)
                .put("modelList",modelList);
    }

    @PostMapping("/deleteNodes")
    public R deleteNodes(@RequestBody JSONObject jsonObject){
        JSONArray nodes = jsonObject.getJSONArray("nodes");
        for(int i = 0 ; i < nodes.size(); i++){
            JSONObject node = nodes.getJSONObject(i);
            String type = node.getString("type");
            String nodeId = node.getString("nodeId");
            switch (type){
                case "menu":
                    sysMenuService.deleteById(nodeId);
                    break;
                case "view":
                    sysViewService.deleteById(nodeId);
                    break;
                case "viewMenu":
                case "viewBtn":
                    sysViewCmptService.deleteById(nodeId);
                    break;
                case "model":
                    sysModelService.deleteById(nodeId);
                    sysImportService.deleteByIdUpId(nodeId,node.getString("upCmptId"));
                    break;
                default:
                    break;
            }
        }
        JSONArray imports = jsonObject.getJSONArray("imports");
        for(int i = 0 ; i < imports.size(); i++){
            String nodeIdStr = imports.getString(i);
            String nodeId = nodeIdStr.split(",")[0];
            String upCmptId = nodeIdStr.split(",")[1];
            sysImportService.deleteByIdUpId(nodeId,upCmptId);
        }
        return R.ok();
    }

    @PostMapping("/auth")
    public R auth(@RequestBody JSONObject jsonObject) {
        sysAuthService.auth(jsonObject);
        return R.ok();
    }

    @GetMapping("/getAuthByRole/{roleId}")
    public R getAuthByRole(@PathVariable String roleId) {
        JSONObject jsonObject = sysAuthService.getAuthByRole(roleId);
        return R.ok().put("data",jsonObject);
    }

}