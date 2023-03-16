/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.logger.MFLogger;
import cn.mftcc.common.sysutils.MD5Util;
import cn.mftcc.common.utils.DateUtil;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.sys.common.constant.PltConstant;
import cn.mftcc.sys.common.utils.JwtUtil;
import cn.mftcc.sys.common.utils.UserAgentParser;
import cn.mftcc.sys.components.sys.entity.SysDeptEntity;
import cn.mftcc.sys.components.sys.entity.SysLoginLogEntity;
import cn.mftcc.sys.components.sys.entity.SysMenuEntity;
import cn.mftcc.sys.components.sys.entity.SysRoleEntity;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.service.SysDeptService;
import cn.mftcc.sys.components.sys.service.SysLoginLogService;
import cn.mftcc.sys.components.sys.service.SysMenuService;
import cn.mftcc.sys.components.sys.service.SysModelService;
import cn.mftcc.sys.components.sys.service.SysRoleService;
import cn.mftcc.sys.components.sys.service.SysSecAuditConfigService;
import cn.mftcc.sys.components.sys.service.SysSecUserMarkInfoService;
import cn.mftcc.sys.components.sys.service.SysUserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysModelService sysModelService;

    @Autowired
    private SysSecUserMarkInfoService sysSecUserMarkInfoService;

    @Autowired
    private SysSecAuditConfigService sysSecAuditConfigService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RequestUtil requestUtil;

    @Value("${casInterfaceFlag:0}")
    private String casInterfaceFlag;

    @Value("${mftcc.super-admin.name:}")
    private String superadminName;

    @Value("${mftcc.super-admin.password:}")
    private String superadminPassword;

    @PostMapping("login")
    public R login(@RequestBody SysUserEntity sysUserEntity) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String opNo = sysUserEntity.getOpNo();

        if(StringUtils.isBlank(opNo)){
            return R.error("用户名不能为空！");
        }
        if(StringUtils.isBlank(sysUserEntity.getPassword())){
            return R.error("密码不能为空！");
        }
        // SysUserEntity sysUserEntityLogin = sysUserService.findById(opNo); // 20200310新表主键调整
        SysUserEntity sysUserEntityLogin =sysUserService.findByOpNo(opNo);
        if(sysUserEntityLogin == null){
            if(opNo.equals(superadminName)){
                if(sysUserEntity.getPassword().equals(superadminPassword)){
                    sysUserEntityLogin = new SysUserEntity();
                    sysUserEntityLogin.setOpNo(PltConstant.SUPER_ADMIN);
                    sysUserEntityLogin.setOpName("超级管理员");
                    sysUserEntityLogin.setCorpId("superCorp");
                    sysUserEntityLogin.setIsSuper(true);
                }else{
                    return R.error("密码错误");
                }
            }else{
                return R.error("该用户不存在！");
            }
        }
        if(sysUserEntityLogin!=null){
            /*炒鸡管理员*/
            if(sysUserEntityLogin.getIsSuper()!=null){
                //生成token
                Map<String, Object> userInfo = new HashMap<String, Object>();
                userInfo.put("opNo",sysUserEntityLogin.getOpNo());
                String tokenStr = jwtUtil.generateToken(userInfo);

                Map<String, Object> tokenMap = new HashMap<String, Object>();
                tokenMap.put("userInfo",userInfo);
                tokenMap.put("token",tokenStr);
                String refreshTokenStr = jwtUtil.generateRefreshToken(tokenMap);
                //redis全局用户数据
                try {
                    requestUtil.initLogin(String.valueOf(sysUserEntityLogin.getOpNo()));
                    requestUtil.setUserInfo("opNo",sysUserEntityLogin.getOpNo());
                    requestUtil.setUserInfo("opName",sysUserEntityLogin.getOpName());
                }catch (Exception e){
                    return R.error("缓存数据加载失败");
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", tokenStr);
                jsonObject.put("refreshToken", refreshTokenStr);
                //获取菜单
                List<SysMenuEntity> sysMenuList = sysMenuService.findAllList();

                jsonObject.put("sysMenuList", sysMenuList);

                JSONObject sysUserInfo = new JSONObject();
                sysUserInfo.put("opName",sysUserEntityLogin.getOpName());
                sysUserInfo.put("opNo",sysUserEntityLogin.getOpNo());
                sysUserInfo.put("isSuper",sysUserEntityLogin.getIsSuper());

                jsonObject.put("sysUserInfo", sysUserInfo);

                //记录登录日志
                sysLoginLogInsert(sysUserEntityLogin,tokenStr);
                return R.ok().put("data",jsonObject);
            }
            if(sysUserEntityLogin.getUserSts()==null || "0".equals(sysUserEntityLogin.getUserSts())){
                return R.error("该用户已失效,无法登陆!");
            }else if(sysUserEntityLogin.getUserSts()==null ||"2".equals(sysUserEntityLogin.getUserSts())){
                return R.error("该用户已经被注销,无法登陆！");
            }
            R r = doCasAuth(sysUserEntity.getOpNo(),sysUserEntity.getPassword(),sysUserEntityLogin);
            if(500==(Integer) r.get("code")){
                String msg = (String)r.get("msg");
                if("密码错误".equals(msg)){
                    sysSecUserMarkInfoService.insertOrUpdate(opNo, msg);
                    JSONObject remeg = sysSecAuditConfigService.SecurityPwd(opNo,"pwdError");//XL安全审计调用校验接口
                    if("error".equals(remeg.getString("flag"))){
                        msg = remeg.getString("msg");
                        return R.error(msg).put("identify",true);
                    }
                }
                return r;
            }
            JSONObject mustUpdate = sysSecAuditConfigService.SecurityPwd(opNo,"mustUpdate");//XL安全审计调用校验接口
            if("update".equals(mustUpdate.getString("flag"))){
                return R.error(mustUpdate.getString("msg")).put("code",300);
            }
            JSONObject remeg = sysSecAuditConfigService.SecurityPwd(opNo,"pwdRight");//XL安全审计调用校验接口
            String msg = remeg.getString("msg");
            if("error".equals(remeg.getString("flag"))){
                return R.error(msg);
            }else if("toup".equals(remeg.getString("flag"))){
                return R.error(msg).put("code",300);
            }
            sysSecUserMarkInfoService.insertOrUpdate(opNo, "updatePassword");
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
                    SysRoleEntity sysRoleEntity = sysRoleService.findByRoleNo(role);
                    roleIds.add(sysRoleEntity.getCorpId()+role);
                }
            }

            List<SysRoleEntity> sysRoleList = sysRoleService.findListByRoleIds(roleIds);
            StringBuffer roleTypes = new StringBuffer();
            for(SysRoleEntity sysRoleEntity:sysRoleList){
                roleTypes.append(sysRoleEntity.getRoleType()).append("|");
            }
            SysDeptEntity sysDeptEntityCorp = sysDeptService.findByCorpId(sysUserEntityLogin.getCorpId());
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
                requestUtil.setUserInfo("corpName",sysDeptEntityCorp.getBrName());
                List<String> corpIdChildren = sysDeptService.getChildrenCorpIdList(sysUserEntityLogin.getCorpId());
                requestUtil.setUserInfo("corpIdChildren",corpIdChildren);
                requestUtil.setUserInfo("token",tokenStr);
            }catch (Exception e){
                return R.error("缓存数据加载失败");
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
            sysUserInfo.put("corpName",sysDeptEntityCorp.getBrName());
            sysUserInfo.put("tel",sysUserEntityLogin.getTel());

            jsonObject.put("sysUserInfo", sysUserInfo);

            List<String> sysModelList = sysModelService.findListByRoleIds(roleIds);
            //按钮权限
            //TODO 获取所有按钮权限
            jsonObject.put("buttons", sysModelList);
            //记录登录日志
            sysLoginLogInsert(sysUserEntityLogin,tokenStr);
            return R.ok().put("data",jsonObject);
        }else{
            return R.error("该用户不存在！");
        }
    }

    @PostMapping("login4dev")
    public R login4dev(@RequestBody SysUserEntity sysUserEntity) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String opNo = sysUserEntity.getOpNo();

        SysUserEntity sysUserEntityLogin =sysUserService.findByOpNo(opNo);
        if(sysUserEntityLogin == null){
            if(opNo.equals(superadminName)){
                if(sysUserEntity.getPassword().equals(superadminPassword)){
                    sysUserEntityLogin = new SysUserEntity();
                    sysUserEntityLogin.setOpNo(PltConstant.SUPER_ADMIN);
                    sysUserEntityLogin.setOpName("超级管理员");
                    sysUserEntityLogin.setCorpId("superCorp");
                    sysUserEntityLogin.setIsSuper(true);
                }else{
                    return R.error("密码错误");
                }
            }else{
                return R.error("该用户不存在！");
            }
        }
        if(sysUserEntityLogin!=null){
            /*炒鸡管理员*/
            //生成token
            Map<String, Object> userInfo = new HashMap<String, Object>();
            userInfo.put("opNo",sysUserEntityLogin.getOpNo());
            userInfo.put("clientId", "form-dev");
            String tokenStr = jwtUtil.generateToken(userInfo);

            Map<String, Object> tokenMap = new HashMap<String, Object>();
            tokenMap.put("userInfo",userInfo);
            tokenMap.put("token",tokenStr);
            tokenMap.put("clientId", "form-dev");
            String refreshTokenStr = jwtUtil.generateRefreshToken(tokenMap);
            //redis全局用户数据
            try {
                requestUtil.initLogin(String.valueOf(sysUserEntityLogin.getOpNo()));
                requestUtil.setUserInfo("opNo",sysUserEntityLogin.getOpNo());
                requestUtil.setUserInfo("opName",sysUserEntityLogin.getOpName());
                requestUtil.setUserInfo("roleNo",sysUserEntityLogin.getRoleNo());
                requestUtil.setUserInfo("brNo",sysUserEntityLogin.getBrNo());
                List<String> brNoChildren = sysDeptService.getChildrenBrNoList(sysUserEntityLogin.getBrNo(),sysUserEntityLogin.getCorpId());
                requestUtil.setUserInfo("brNoChildren",brNoChildren);
                requestUtil.setUserInfo("corpId",sysUserEntityLogin.getCorpId());
                List<String> corpIdChildren = sysDeptService.getChildrenCorpIdList(sysUserEntityLogin.getCorpId());
                requestUtil.setUserInfo("corpIdChildren",corpIdChildren);
                requestUtil.setUserInfo("token",tokenStr);
            }catch (Exception e){
                return R.error("缓存数据加载失败");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", tokenStr);
            jsonObject.put("refreshToken", refreshTokenStr);

            JSONObject sysUserInfo = new JSONObject();
            sysUserInfo.put("opName",sysUserEntityLogin.getOpName());
            sysUserInfo.put("opNo",sysUserEntityLogin.getOpNo());
            sysUserInfo.put("isSuper",sysUserEntityLogin.getIsSuper());

            jsonObject.put("sysUserInfo", sysUserInfo);

            return R.ok().put("data",jsonObject);
        }else{
            return R.error("该用户不存在！");
        }
    }

    @PostMapping("refreshIdentifyCode")
    public R refreshIdentifyCode() {
        try{
            String identifyCode = sysSecAuditConfigService.createIdentifyCode(4,true,"",true,2,1);
            return R.ok().put("identify",identifyCode);
        }catch (Exception e){
            return R.error("获取验证码失败");
        }
    }

    @PostMapping("loginOut")
    public R loginOut(@RequestBody SysUserEntity sysUserEntity) {
        try{
            //清除后台登录状态
            requestUtil.initLogin(sysUserEntity.getOpNo());
            //登出日志
            sysLoginLogUpdate();
            sysSecUserMarkInfoService.insertOrUpdate(sysUserEntity.getOpNo(),"logout");
            return R.ok("退出成功");
        }catch (Exception e){
            return R.error("退出失败");
        }
    }

   private void sysLoginLogInsert(SysUserEntity sysUserEntity,String token) {
        HttpServletRequest request = requestUtil.getRequest();
        String  agent = request.getHeader("User-agent");//返回客户端浏览器的版本号、类型
        try {
            SysLoginLogEntity sysLoginLogEntity =new SysLoginLogEntity();
            sysLoginLogEntity.setToken(token);
            sysLoginLogEntity.setBrNo(sysUserEntity.getBrNo());
            sysLoginLogEntity.setOpNo(sysUserEntity.getOpNo());
            sysLoginLogEntity.setOpName(sysUserEntity.getOpName());
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            sysLoginLogEntity.setLoginDate(format.format(new Date()));
            sysLoginLogEntity.setLoginTime(DateUtil.getTime());
            try {
                String ip = UserAgentParser.getClientIP(request);
                sysLoginLogEntity.setLoginIp(ip);
            } catch (Exception e) {
                sysLoginLogEntity.setLoginIp("0.0.0.0");
            }
            try {
                String [] os = UserAgentParser.getOS(agent).split(" ");
                if(os.length==2){
                    sysLoginLogEntity.setOsName(os[0]);
                    sysLoginLogEntity.setOsVersion(os[1]);
                }else{
                    sysLoginLogEntity.setOsName(UserAgentParser.getOS(agent));
                    sysLoginLogEntity.setOsVersion(UserAgentParser.getOS(agent));
                }
            } catch (Exception e) {
                sysLoginLogEntity.setOsName("未知的操作系统");
                sysLoginLogEntity.setOsVersion("0.0");
            }
            try {
                String [] ie = UserAgentParser.getBrowser(agent).split(" ");
                if(ie.length==2){
                    sysLoginLogEntity.setIeName(ie[0]);
                    sysLoginLogEntity.setIeVersion(ie[1]);
                }else{
                    sysLoginLogEntity.setIeName(UserAgentParser.getBrowser(agent));
                    sysLoginLogEntity.setIeVersion(UserAgentParser.getBrowser(agent));
                }
            } catch (Exception e) {
                sysLoginLogEntity.setIeName("未知的浏览器");
                sysLoginLogEntity.setIeVersion("0.0");
            }
            sysLoginLogService.insert(sysLoginLogEntity);
        }catch(Exception e) {
            MFLogger.error(token,e);
        }
    }

    private void sysLoginLogUpdate() {
        HttpServletRequest request = requestUtil.getRequest();
        String token= request.getHeader("token");//返回客户端浏览器的版本号、类型
        try {
            SysLoginLogEntity sysLoginLogEntity =new SysLoginLogEntity();
            sysLoginLogEntity.setToken(token);
            sysLoginLogEntity.setLogoutTime(DateUtil.getDateTime());
            sysLoginLogService.update(sysLoginLogEntity);
        }catch(Exception e) {
            MFLogger.error(token,e);
        }
    }

    private R doCasAuth(String opNo,String password,SysUserEntity sysUserEntityLogin) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String msg = "密码错误";
	    /*if(Constant.YES_NO_Y.equals(casInterfaceFlag)){
            //开启CAS接口验证
            //调用顺风的CAS接口验证
            boolean userAuthFlag = false;
            try {
                userAuthFlag = thirdFeignClient.userAuth(opNo,password);
                if(userAuthFlag){
                    return R.ok();
                }else {
                    //CAS接口验证不通过不允许其进行登录，并给予提示信息
                    return R.error(msg);
                }
            }catch (Exception e){
                return R.error("调用CAS接口验证失败"+e.getMessage());
            }
        }else{*/
            /**
             * 不开启顺风CAS接口调用时进行系统自身密码校验逻辑
             */
            //密码加密
            password = MD5Util.md5(password);
            if(sysUserEntityLogin.getPassword().equals(password)){
                return R.ok();
            }else{
                return R.error(msg);
            }
//        }
    }

}
