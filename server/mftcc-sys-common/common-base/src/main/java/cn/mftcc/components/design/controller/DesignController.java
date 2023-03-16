/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.components.design.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.utils.RequestUtil;
import cn.mftcc.components.design.service.DesignService;
import cn.mftcc.components.design.utils.JwtUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/design")
public class DesignController {

    @Autowired
    private DesignService service;
    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("list/{productType}/{type}")
    public R list(@PathVariable String productType, @PathVariable String type){
        return R.ok().put("data", service.list(productType,type));
    }

    @GetMapping("open/{productType}/{type}/{name}")
    public R open(@PathVariable String productType, @PathVariable String type, @PathVariable String name){
        return R.ok().put("data", service.open(productType, type, name));
    }

    @GetMapping("find/{productType}/{type}/{name}")
    public R find(@PathVariable String productType, @PathVariable String type, @PathVariable String name){
        return R.ok().put("data", service.find(productType, type, name));
    }

    @PostMapping("save/{productType}/{type}/{name}")
    public R save(@PathVariable String productType, @PathVariable String type, @PathVariable String name, @RequestBody JSONObject body){
        service.save(productType, type, name, JSONObject.toJSONString(body, true));
        return R.ok();
    }

    @DeleteMapping("del/{productType}/{type}/{name}")
    public R del(@PathVariable String productType, @PathVariable String type, @PathVariable String name){
        boolean flag = service.del(productType, type, name);
        return R.ok().put("flag", flag);
    }

    @PostMapping("login4dev")
    public R login4dev() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String opNo = "formDesigner";
        //生成token
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("opNo",opNo);
        userInfo.put("clientId", "form-dev");
        String tokenStr = jwtUtil.generateToken(userInfo);

        Map<String, Object> tokenMap = new HashMap<String, Object>();
        tokenMap.put("userInfo",userInfo);
        tokenMap.put("token",tokenStr);
        tokenMap.put("clientId", "form-dev");
        String refreshTokenStr = jwtUtil.generateRefreshToken(tokenMap);
        //redis全局用户数据
        try {
            requestUtil.initLogin(opNo);
            requestUtil.setUserInfo("opNo",opNo);
            requestUtil.setUserInfo("opName",opNo);
            requestUtil.setUserInfo("corpId","");
        }catch (Exception e){
            return R.error("缓存数据加载失败");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", tokenStr);
        jsonObject.put("refreshToken", refreshTokenStr);

        JSONObject sysUserInfo = new JSONObject();
        sysUserInfo.put("opName",opNo);
        sysUserInfo.put("opNo",opNo);


        jsonObject.put("sysUserInfo", sysUserInfo);

        return R.ok().put("data",jsonObject);
    }
}
