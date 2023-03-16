/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.feign;


import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.logger.MFLogger;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.common.utils.JwtUtil;
import cn.mftcc.sys.components.sys.entity.SysDeptEntity;
import cn.mftcc.sys.components.sys.entity.SysMenuEntity;
import cn.mftcc.sys.components.sys.entity.SysRoleEntity;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.service.SysDeptService;
import cn.mftcc.sys.components.sys.service.SysMenuService;
import cn.mftcc.sys.components.sys.service.SysModelService;
import cn.mftcc.sys.components.sys.service.SysRoleService;
import cn.mftcc.sys.components.sys.service.SysUserService;
import cn.mftcc.sys.feign.api.SysInterface;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SysUserFeignController implements SysInterface {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysModelService sysModelService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RequestUtil requestUtil;

    @Override
    public String createToken(String opNo) {
        try {
            Map<String, Object> userInfo = new HashMap<String, Object>();
            userInfo.put("opNo",opNo);
            String tokenStr = jwtUtil.generateToken(userInfo);
            requestUtil.initLogin(opNo);
            requestUtil.setUserInfo("opNo",opNo);
            return tokenStr;
        } catch(Exception e){
            MFLogger.error("token创建失败",e);
        }
        return null;
    }

    @Override
    public String simulationLogin(String opNo) {
        SysUserEntity sysUserEntityLogin =sysUserService.findByOpNo(opNo);
        if(sysUserEntityLogin == null){
            return R.error("该用户不存在！").toString();
        }
        if(sysUserEntityLogin.getUserSts()==null || "0".equals(sysUserEntityLogin.getUserSts())){
            return R.error("该用户已失效,无法登陆!").toString();
        }else if(sysUserEntityLogin.getUserSts()==null || "2".equals(sysUserEntityLogin.getUserSts())){
            return R.error("该用户已经被注销,无法登陆！").toString();
        }

        //生成token
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("opNo",sysUserEntityLogin.getOpNo());
        String tokenStr = jwtUtil.generateToken(userInfo);

        Map<String, Object> tokenMap = new HashMap<String, Object>();
        tokenMap.put("userInfo",userInfo);
        tokenMap.put("token",tokenStr);
        String refreshTokenStr = jwtUtil.generateRefreshToken(tokenMap);
        SysDeptEntity sysDeptEntity = sysDeptService.findByBrNo(sysUserEntityLogin.getBrNo());

        String [] roleArray = sysUserEntityLogin.getRoleNo().split("\\|");
        List<String> roleIds = new ArrayList<>();
        for(String role : roleArray){
            if(!StringUtils.isEmpty(role)){
                roleIds.add(sysUserEntityLogin.getCorpId()+role);
            }
        }

        List<SysRoleEntity> sysRoleList = sysRoleService.findListByRoleIds(roleIds);
        StringBuffer roleTypes = new StringBuffer();
        for(SysRoleEntity sysRoleEntity:sysRoleList){
            roleTypes.append(sysRoleEntity.getRoleType()).append("|");
        }
        //redis全局用户数据
        try {
            requestUtil.initLogin(String.valueOf(sysUserEntityLogin.getOpNo()));
            requestUtil.setUserInfo("opNo",sysUserEntityLogin.getOpNo());
            requestUtil.setUserInfo("opName",sysUserEntityLogin.getOpName());
            requestUtil.setUserInfo("roleNo",sysUserEntityLogin.getRoleNo());
            requestUtil.setUserInfo("roleType",roleTypes.toString());
            requestUtil.setUserInfo("brNo",sysUserEntityLogin.getBrNo());
            requestUtil.setUserInfo("brName",sysDeptEntity.getBrName());
            List<String> brNoChildren = sysDeptService.getChildrenBrNoList(sysUserEntityLogin.getBrNo(),sysUserEntityLogin.getCorpId());
            requestUtil.setUserInfo("brNoChildren",brNoChildren);
            requestUtil.setUserInfo("corpId",sysUserEntityLogin.getCorpId());
            List<String> corpIdChildren = sysDeptService.getChildrenCorpIdList(sysUserEntityLogin.getCorpId());
            requestUtil.setUserInfo("corpIdChildren",corpIdChildren);
        }catch (Exception e){
            return R.error("缓存数据加载失败").toString();
        }

        JSONObject buttonAuth = new JSONObject();
        buttonAuth.put("regOpNo",sysUserEntityLogin.getOpNo());
        buttonAuth.put("regBrNo",sysUserEntityLogin.getBrNo());
        buttonAuth.put("corpId",sysUserEntityLogin.getCorpId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", tokenStr);
        jsonObject.put("refreshToken", refreshTokenStr);
        jsonObject.put("buttonAuth", buttonAuth);

        //获取菜单
        List<SysMenuEntity> sysMenuList = sysMenuService.findListByRoleIds(roleIds);

        jsonObject.put("sysMenuList", sysMenuList);

        JSONObject sysUserInfo = new JSONObject();
        sysUserInfo.put("brName",sysDeptEntity.getBrName());
        sysUserInfo.put("brNo",sysUserEntityLogin.getBrNo());
        sysUserInfo.put("opName",sysUserEntityLogin.getOpName());
        sysUserInfo.put("opNo",sysUserEntityLogin.getOpNo());
        sysUserInfo.put("roleName",sysUserEntityLogin.getRoleName());
        sysUserInfo.put("roleNo",sysUserEntityLogin.getRoleNo());
        sysUserInfo.put("corpId",sysDeptEntity.getCorpId());
        sysUserInfo.put("tel",sysUserEntityLogin.getTel());

        jsonObject.put("sysUserInfo", sysUserInfo);

        List<String> sysModelList = sysModelService.findListByRoleIds(roleIds);
        //按钮权限
        //TODO 获取所有按钮权限
        jsonObject.put("buttons", sysModelList);
        //记录登录日志
//        sysLoginLogInsert(sysUserEntityLogin,tokenStr);
        return R.ok().put("data",jsonObject).toString();
    }


    @Override
    public String findUserByOpNo(String opNo) {
        SysUserEntity sysUserEntity = sysUserService.findByOpNo(opNo);
        if(sysUserEntity!=null){
            SysDeptEntity sysDeptEntity = sysDeptService.findByBrNo(sysUserEntity.getBrNo());
            sysUserEntity.setBrName(sysDeptEntity.getBrName());
            return JSONObject.toJSONString(sysUserEntity);
        }
        return null;
    }

    @Override
    public String findByRoleNo4MQ(String roleNo) {
        List<SysUserEntity> list = sysUserService.findByRoleNo4MQ(roleNo);
        return JSONArray.toJSONString(list);
    }

    @Override
    public String findDeptList() {
        List<SysDeptEntity> list = sysDeptService.getList(new SysDeptEntity());
        return JSONArray.toJSONString(list);
    }

    @Override
    public String findUserList() {
        List<SysUserEntity> list = sysUserService.getList(new SysUserEntity());
        return JSONArray.toJSONString(list);
    }

    @Override
    public String findUserListByBrRole(String brNo, String roleNo) {
        List<SysUserEntity> list = sysUserService.findUserListByBrRole(brNo,roleNo);
        return JSONArray.toJSONString(list);
    }

    @Override
    public String findCorpCompantInfo(String corpId) {
        return null;
    }

    @Override
    public String getUserListByRoleNo(String roleNo) {
        return null;
    }

    @Override
    public String getUserListByCoopUserType(String coopUserType) {
        return null;
    }

    @Override
    public String findUpBrByBrNo(String brNo) {
        return null;
    }

    @Override
    public String findCorpName(String corpId) {
        return null;
    }

    @Override
    public String findBrByBrNo(String brNo) {
        return null;
    }

    @Override
    public Boolean flowableWaitDemo(JSONObject jsonObject) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String insertDept(JSONObject jsonObject) {
        //判断机构号是否存在
        SysDeptEntity sysDeptEntity = jsonObject.toJavaObject(SysDeptEntity.class);
        SysDeptEntity tmp=new SysDeptEntity();
        tmp.setBrNo(sysDeptEntity.getBrNo());
        if(this.sysDeptService.getList(tmp).size()>0){
            return R.error("机构号已存在").toString();
        }else{
            tmp=new SysDeptEntity();
            tmp.setCorpId(sysDeptEntity.getCorpId());
            if("1".equals(sysDeptEntity.getIsCorp())&&this.sysDeptService.getList(tmp).size()>0){
                return R.error("法人机构号已存在").toString();
            }
            sysDeptEntity.setBrSts("1");
            this.sysDeptService.insert(sysDeptEntity);
            return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS).put("brId",sysDeptEntity.getBrId()).toString();
        }
    }

    @Override
    public String updateUserByOpNo(@RequestBody JSONObject jsonObject){
        SysUserEntity sysUserEntity = jsonObject.toJavaObject(SysUserEntity.class);
        SysUserEntity tmp = sysUserService.findByOpNo(sysUserEntity.getOpNo());
        sysUserEntity.setUserId(tmp.getUserId());
        this.sysUserService.update(sysUserEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS).toString();
    }

    @Override
    public String findNoDeptNoRoleUserList(){
        List<SysUserEntity> list = sysUserService.findNoDeptNoRoleUserList();
        return JSONArray.toJSONString(list);
    }


    @Override
    public String findListByRoleType(JSONObject jsonObject){
        SysRoleEntity sysRoleEntity=new SysRoleEntity();
        sysRoleEntity.setRoleType(jsonObject.getString("roleType"));
        sysRoleEntity.setRoleLvl(jsonObject.getInteger("roleLvl"));
        List<SysRoleEntity> list = sysRoleService.selectList(sysRoleEntity);
        return JSONArray.toJSONString(list);
    }




    @Override
    public String getAuthByRoles(String[] roleArray) {

        List<String> roleIds = new ArrayList<>();
        for(String role : roleArray){
            if(!StringUtils.isEmpty(role)){
                roleIds.add(role);
            }
        }
        List<SysRoleEntity> sysRoleList = sysRoleService.findListByRoleIds(roleIds);
        StringBuffer roleTypes = new StringBuffer();
        for(SysRoleEntity sysRoleEntity:sysRoleList){
            roleTypes.append(sysRoleEntity.getRoleType()).append("|");
        }
        //redis全局用户数据
        JSONObject jsonObject = new JSONObject();
        //获取菜单
        List<SysMenuEntity> sysMenuList = sysMenuService.findListByRoleIds(roleIds);
        jsonObject.put("sysMenuList", sysMenuList);
        List<String> sysModelList = sysModelService.findListByRoleIds(roleIds);
        //按钮权限
        jsonObject.put("buttons", sysModelList);
        return R.ok().put("data",jsonObject).toString();
    }


}
