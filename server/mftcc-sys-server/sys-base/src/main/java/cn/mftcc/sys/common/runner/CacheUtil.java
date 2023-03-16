/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.common.runner;

import cn.mftcc.common.sysutils.MsgCacheUtil;
import cn.mftcc.common.utils.AuthorityUtil;
import cn.mftcc.common.utils.ParmCacheUtil;
import cn.mftcc.sys.components.sys.entity.*;
import cn.mftcc.sys.components.sys.service.*;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CacheUtil
 * @Description
 * @Author 郭涵晨
 * @Date 2020/4/27 17:23
 */
@Component
public class CacheUtil {

    @Autowired
    private MsgCacheUtil msgCacheUtil;

    @Autowired
    private ParmCacheUtil parmCacheUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

    @Autowired
    private SysMsgConfigService sysMsgConfigService;

    @Autowired
    private SysParmDicService sysParmDicService;

    @Autowired
    private SysParmKeyService sysParmKeyService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysModelService sysModelService;

    @Autowired
    private SysDataAuthService sysDataAuthService;

    @Autowired
    private SysParmTransService sysParmTransService;

    public void initMsgCaChe(){
        List<SysMsgConfigEntity> sysMsgConfigEntityList = sysMsgConfigService.findAll();
        msgCacheUtil.deleteAll();
        for (SysMsgConfigEntity sysMsgConfigEntity : sysMsgConfigEntityList) {
            msgCacheUtil.setMsg(sysMsgConfigEntity.getMsgCode(), sysMsgConfigEntity);
        }
    }


    public void initParmCaChe(){
        List<SysParmKeyEntity> sysParmKeyEntityList = sysParmKeyService.findAll();
        List<String> keyNameList = new ArrayList<>();
        for(SysParmKeyEntity sysParmKeyEntity : sysParmKeyEntityList){
            keyNameList.add(sysParmKeyEntity.getKeyName());
        }
        if(keyNameList.size()>0){
            List<SysParmDicEntity> sysParmDicEntityList = sysParmDicService.findByKeyName(keyNameList);
            String key;
            List<SysParmDicEntity> listTmp;
            Map<String, List<SysParmDicEntity>> map = new HashMap<String, List<SysParmDicEntity>>();
            for (SysParmDicEntity sysParmDicEntity : sysParmDicEntityList) {
                String delFlag = sysParmDicEntity.getSts();
                if("1".equals(delFlag)){
                    key = sysParmDicEntity.getKeyName();//按这个属性分组，map的Key
                    listTmp = map.get(key);
                    if (null == listTmp) {
                        listTmp = new ArrayList<SysParmDicEntity>();
                        map.put(key, listTmp);
                    }
                    listTmp.add(sysParmDicEntity);
                }
            }
            parmCacheUtil.deleteAll();
            for(String type : map.keySet()){
                parmCacheUtil.setDict(type, map.get(type));
            }
        }
    }

    public void initUrlAuth() throws Exception {
        List<SysRoleEntity> sysRoleList = sysRoleService.findAll();
        for(SysRoleEntity sysRoleEntity : sysRoleList){
            String roleId = sysRoleEntity.getRoleId();
            JSONArray urlAuth = sysModelService.getUrlAuthByRoleId(roleId);
            authorityUtil.setAuth(roleId,"urlAuth",urlAuth);
            JSONArray dataAuth = sysDataAuthService.getDataAuthByRoleId(roleId);
            authorityUtil.setAuth(roleId,"dataAuth",dataAuth);
        }
    }
    public void initParmTransCaChe(){
        List<SysParmTransEntity> sysParmTransEntityList = sysParmTransService.findAll();
        if(sysParmTransEntityList.size()>0) {
            String key;
            List<SysParmTransEntity> listTmp;
            Map<String, List<SysParmTransEntity>> map = new HashMap<String, List<SysParmTransEntity>>();
            for (SysParmTransEntity sysParmTransEntity : sysParmTransEntityList) {
                key = sysParmTransEntity.getParmKey();//按这个属性分组，map的Key
                listTmp = map.get(key);
                if (null == listTmp) {
                    listTmp = new ArrayList<SysParmTransEntity>();
                    map.put(key, listTmp);
                }
                listTmp.add(sysParmTransEntity);
            }
            parmCacheUtil.deleteAll();
            for (String type : map.keySet()) {
                parmCacheUtil.setDict(type, map.get(type));
            }
        }
    }
}
