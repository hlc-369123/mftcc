/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.sysutils.MD5Util;
import cn.mftcc.sys.components.sys.entity.SysDeptEntity;
import cn.mftcc.sys.components.sys.entity.SysRoleEntity;
import cn.mftcc.sys.components.sys.entity.SysUserEntity;
import cn.mftcc.sys.components.sys.service.SysDeptService;
import cn.mftcc.sys.components.sys.service.SysRoleService;
import cn.mftcc.sys.components.sys.service.SysSecAuditConfigService;
import cn.mftcc.sys.components.sys.service.SysSecUserMarkInfoService;
import cn.mftcc.sys.components.sys.service.SysUserService;
import com.alibaba.fastjson.JSONObject;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-21 16:30:33
 */
@RestController
@RequestMapping("sys/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysSecAuditConfigService sysSecAuditConfigService;

    @Autowired
    private SysSecUserMarkInfoService sysSecUserMarkInfoService;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysUserEntity sysUserEntity) {
        IPage<SysUserEntity> list = this.sysUserService.findByPage(sysUserEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/findByPage4admin")
    public R findByPage4admin(@RequestBody SysUserEntity sysUserEntity) {
        IPage<SysUserEntity> list = this.sysUserService.findByPage4admin(sysUserEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/findByPage4Dialog")
    public R findByPage4Dialog(@RequestBody JSONObject sysUserEntity) {
        IPage<SysUserEntity> list = this.sysUserService.findByPage4Dialog(sysUserEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/findAllByPage")
    public R findAllByPage(@RequestBody JSONObject sysUserEntity) {
        IPage<SysUserEntity> list = this.sysUserService.findAllByPage(sysUserEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/findAll")
    public R findAll(@RequestBody JSONObject sysUserEntity) {
        List<SysUserEntity> list = this.sysUserService.findAll(sysUserEntity);
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysUserEntity sysUserEntity) throws Exception{
        // 验证密码输入一致性
        /*if (!sysUserEntity.getPassword().equals(sysUserEntity.getPasswordChk())) {
            return R.error("确认密码与密码不相符，请重新录入");
        }*/

        SysUserEntity ifExistsysUser = sysUserService.findByOpNo(sysUserEntity.getOpNo());
        if (ifExistsysUser != null) {
            return R.error("用户已经存在");
        } else {
            sysUserEntity.setOpNo(sysUserEntity.getOpNo().toLowerCase());
            //对用户密码进行加密
            sysUserEntity.setPassword(MD5Util.md5("0000")); // 密码默认
            //判断roleNo中是否存在  |
            String roleNo = sysUserEntity.getRoleNo();
            roleNo = roleNo.replaceAll(",","\\|");
            sysUserEntity.setRoleNo(roleNo);
            if (StringUtils.isEmpty(sysUserEntity.getUserSts())) {
                sysUserEntity.setUserSts("0"); // 新增默认“0-未生效”
            }
            this.sysUserService.insert(sysUserEntity);
            return R.ok(SysConstant.MSG_CONFIG_SAVE_SUCCESS);
        }
    }

    // 新增并启用
    @PostMapping("/insertEnable")
    public R insertEnable(@RequestBody SysUserEntity sysUserEntity) throws Exception {
        sysUserEntity.setUserSts("1"); // 新增并启用默认“1-启用”
        return insert(sysUserEntity);
    }

    @PutMapping("/update")
    public R update(@RequestBody SysUserEntity sysUserEntity) {
        SysUserEntity tmp = sysUserService.findById(sysUserEntity.getUserId());
        // 如果修改用户名，校验用户名是否存在
        if (!sysUserEntity.getOpNo().equals(tmp.getOpNo())) {
            if (sysUserService.findByOpNo(sysUserEntity.getOpNo()) != null) {
                return R.error("用户名已存在");
            }
        }
        // 用户登录名改为小写
        sysUserEntity.setOpNo(sysUserEntity.getOpNo().toLowerCase());
        String roleNo = sysUserEntity.getRoleNo();
        roleNo = roleNo.replaceAll(",","\\|");
        sysUserEntity.setRoleNo(roleNo);
        // 如果更改的用户信息时更改了用户密码
        /*String pwd = tmp.getPassword();
        if (!sysUserEntity.getPassword().equals(pwd)) {
            sysUserEntity.setPassword(Encryption.md5(sysUserEntity.getPassword()));
        }*/
        this.sysUserService.update(sysUserEntity);
        return R.ok(SysConstant.MSG_CONFIG_UPDATE_SUCCESS);
    }

    // 启用
    @GetMapping("/updateStart/{userId}")
    public R updateStart(@PathVariable("userId") int userId) {
        SysUserEntity sysUserEntity = this.sysUserService.findById(userId);
        // 0-未生效可启用
        if ("0".equals(sysUserEntity.getUserSts())) {
            sysUserEntity.setUserId(userId);
            sysUserEntity.setUserSts("1"); // 1-启用
            this.sysUserService.update(sysUserEntity);
        } else {
            return R.error("用户未生效时可启用");
        }
        return R.ok();
    }

    // 注销
    @GetMapping("/updateLogout/{userId}")
    public R updateLogout(@PathVariable("userId") int userId) {
        SysUserEntity sysUserEntity = this.sysUserService.findById(userId);
        // 1-已启用可注销
        if ("1".equals(sysUserEntity.getUserSts())) {
            sysUserEntity.setUserId(userId);
            sysUserEntity.setUserSts("2"); // 2-注销
            this.sysUserService.update(sysUserEntity);
        } else {
            return R.error("用户启用时可注销");
        }
        return R.ok();
    }

    @GetMapping("/findById/{userId}")
    public R findById(@PathVariable("userId") int userId) {
        SysUserEntity sysUserEntity = this.sysUserService.findById(userId);

        sysUserEntity.setPasswordChk(sysUserEntity.getPassword());
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("data", sysUserEntity);
        String roleNo = sysUserEntity.getRoleNo();
        if(StringUtils.isNotBlank(roleNo)){
            String[] roleNos = roleNo.split("\\|");
            List<String> list = Arrays.asList(roleNos);
            List<SysRoleEntity> roleList = this.sysRoleService.findListByRoleNos(list);
            String roleName = "";
            for(SysRoleEntity sre : roleList){
                roleName += sre.getRoleName()+",";
            }
            if(roleName.endsWith(",")){
                roleName = roleName.substring(0,roleName.length()-1);
            }
            sysUserEntity.setRoleName(roleName);
            roleNo = roleNo.replaceAll("\\|",",");
            sysUserEntity.setRoleNo(roleNo);
        }
        String brNo = sysUserEntity.getBrNo();
        if(StringUtils.isNotBlank(brNo)){
            SysDeptEntity sysDeptEntity = this.sysDeptService.findByBrNo(brNo);
            sysUserEntity.setBrName(sysDeptEntity.getBrName());
        }
        return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("data",sysUserEntity);
    }

    @GetMapping("/getUserByRoleType/{roleType}")
    public R getUserByRoleType(@PathVariable("roleType") String roleType) {
        try{
            List<SysUserEntity> sysUserList = this.sysUserService.getUserByRoleTypes(roleType);
            return R.ok(SysConstant.MSG_CONFIG_SELECT_SUCCESS).put("list",sysUserList);
        }catch (Exception e){
            return R.error(SysConstant.MSG_CONFIG_SELECT_ERROR);
        }
    }

    @DeleteMapping("/deleteById/{userId}")
    public R deleteById(@PathVariable("userId") int userId) {
        SysUserEntity sysUserEntity = this.sysUserService.findById(userId);
        // 0-未生效可删除
        if ("0".equals(sysUserEntity.getUserSts())) {
            this.sysUserService.deleteById(userId);
        } else {
            return R.error("用户未生效时可删除");
        }
        return R.ok(SysConstant.MSG_CONFIG_DELETE_SUCCESS);
    }

    @PostMapping("validatePassword")
    public R validatePassword(@RequestBody SysUserEntity sysUserEntity) {
        try{
            SysUserEntity sysUserEntityLogin =sysUserService.findByOpNo(sysUserEntity.getOpNo());
            if(sysUserEntityLogin!=null){
                if(sysUserEntityLogin.getUserSts()==null || "0".equals(sysUserEntityLogin.getUserSts())){
                    return R.error("该用户已失效,密码校验失败!");
                }else if(sysUserEntityLogin.getUserSts()==null || "2".equals(sysUserEntityLogin.getUserSts())){
                    return R.error("该用户已经被注销,密码校验失败！");
                }
                //密码加密
                String password = MD5Util.md5(sysUserEntity.getPassword());
                if(sysUserEntityLogin.getPassword().equals(password)){
                    return R.ok("密码校验成功");
                }else{
                    return R.error("密码错误！");
                }
            }else{
                return R.error("用户不存在,密码校验失败！");
            }
        }catch (Exception e){
            return R.error("密码校验失败,请联系管理员");
        }
    }

    @PostMapping("updatePassword")
    public R updatePassword(@RequestBody Map<String,String> parm) {
        try{
            String opNo = parm.get("opNo");
            String passwords = parm.get("password");
            String newPassword = parm.get("newPassword");
            String newPasswordChk = parm.get("newPasswordChk");
            SysUserEntity sysUserEntityLogin = sysUserService.findByOpNo(opNo);
            if(sysUserEntityLogin!=null){
                if(sysUserEntityLogin.getUserSts()==null || "0".equals(sysUserEntityLogin.getUserSts())){
                    return R.error("该用户已失效,密码修改失败!");
                }else if(sysUserEntityLogin.getUserSts()==null || "2".equals(sysUserEntityLogin.getUserSts())){
                    return R.error("该用户已经被注销,密码修改失败！");
                }else if(!newPassword.equals(newPasswordChk)){
                    return R.error("两次录入密码不一致,密码修改失败！");
                }

                JSONObject result = sysSecAuditConfigService.validatePWAjax(opNo,newPassword);
                if("error".equals(result.getString("flag"))){
                    return R.error(result.getString("msg"));
                }

                //密码加密
                String password = MD5Util.md5(passwords);
                if(sysUserEntityLogin.getPassword().equals(password)){
                    SysUserEntity sysUserEntity = new SysUserEntity();
                    sysUserEntity.setOpNo(opNo);
                    String newPasswords = MD5Util.md5(newPassword);
                    sysUserEntity.setPassword(newPasswords);
                    sysUserService.updatePassword(sysUserEntity);
                    sysSecUserMarkInfoService.insertOrUpdate(opNo, "updatePassword");
                    return R.ok("密码修改成功,请重新登陆！");
                }else{
                    return R.error("密码错误,密码修改失败！");
                }
            }else{
                return R.error("用户不存在,密码修改失败！");
            }
        }catch (Exception e){
            return R.error("密码校验失败,请联系管理员");
        }
    }

    @PutMapping("resetPassword")
    public R resetPassword(@RequestBody SysUserEntity sysUserEntity) {
        try{
            SysUserEntity sysUserEntityLogin = sysUserService.findByOpNo(sysUserEntity.getOpNo());
            if(sysUserEntityLogin!=null){
                //密码加密
                String password = MD5Util.md5("000000");
                sysUserEntityLogin.setPassword(password);
                sysUserService.updatePassword(sysUserEntityLogin);
                return R.ok();
            }else{
                return R.error("用户不存在,重置密码失败！");
            }
        }catch (ServiceException e){
            throw e;
        }catch (Exception e){
            return R.error("重置密码失败,请联系管理员");
        }
    }
}