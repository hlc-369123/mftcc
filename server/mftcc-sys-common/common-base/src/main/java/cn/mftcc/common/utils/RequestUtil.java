/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.utils;

import cn.mftcc.common.logger.MFLogger;
import cn.mftcc.common.sysutils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RefreshScope
@Component
public class RequestUtil {

    @Value("${spring.redis.db.user:0}")
    private int userDB;

    @Value("${spring.redis.expire.user:-1}")
    private long userExpire;

    @Value("${spring.redis.keyPrefix.user:user}")
    public String userKeyPrefix; //缓存在内存中命名前缀

    @Value("${spring.redis.db.temp:1}")
    private int tempDB;

    @Value("${spring.redis.expire.temp:-1}")
    private long tempExpire;

    @Value("${spring.redis.keyPrefix.temp:temp}")
    public String tempKeyPrefix; //缓存在内存中命名前缀

    @Autowired
    private RedisUtil redisUtil;

    public HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request;
    }

    public HttpServletResponse getResponse(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        return response;
    }

    public void initLogin(String opNo) throws Exception {
        HttpServletRequest request = getRequest();
        if(request != null && !StringUtils.isEmpty(opNo)){
            request.setAttribute("opNo",opNo);
            try{
                redisUtil.delete(this.userKeyPrefix + "_" + opNo,userDB);
                redisUtil.delete(this.tempKeyPrefix + "_" + opNo,tempDB);
            }catch (Exception e){
                MFLogger.error(e.getMessage(),e);
                throw new Exception("redis error");
            }
        }else{
            throw new Exception("request or opNo is empty");
        }
    }

    public void setUserInfo(String key,Object value) throws Exception {
        HttpServletRequest request = getRequest();
        if(request != null){
            Object opNo = request.getAttribute("opNo");
            if(opNo != null){
                try{
                    JSONObject jsonObject  = redisUtil.get(this.userKeyPrefix + "_" + opNo,JSONObject.class,userExpire,userDB);
                    if(jsonObject == null ){
                        jsonObject = new JSONObject();
                    }
                    jsonObject.put(key,value);
                    redisUtil.set(this.userKeyPrefix + "_" + opNo,jsonObject,userExpire,userDB);
                }catch (Exception e){
                    MFLogger.error(e.getMessage(),e);
                    throw new Exception("redis error");
                }
            }else{
                throw new Exception("opNo is empty");
            }
        }else{
            throw new Exception("request is empty");
        }
    }

    public Object getUserInfo(String key) {
        Object result = null;
        try{
            HttpServletRequest request = getRequest();
            if(request != null){
                Object opNo = request.getAttribute("opNo");
                if(opNo != null){
                    JSONObject jsonObject = redisUtil.get(this.userKeyPrefix + "_" + opNo,JSONObject.class,userExpire,userDB);
                    if(jsonObject != null ){
                        result = jsonObject.get(key);
                    }
                }else{
                    throw new Exception("opNo获取失败");
                }
            }else{
                throw new Exception("request获取失败");
            }
        }catch (Exception e){
            MFLogger.error(e.getMessage(),e);
        }
        return result;
    }

    public void removeUserInfo(String key) throws Exception {
        HttpServletRequest request = getRequest();
        if(request != null){
            Object opNo = request.getAttribute("opNo");
            if(opNo != null){
                try{
                    JSONObject jsonObject  = redisUtil.get(this.userKeyPrefix + "_" + opNo,JSONObject.class,userExpire,userDB);
                    if(jsonObject == null ){
                        jsonObject = new JSONObject();
                    }
                    if(jsonObject.containsKey(key)){
                        jsonObject.remove(key);
                    }
                    redisUtil.set(this.userKeyPrefix + "_" + opNo,jsonObject,userExpire,userDB);
                }catch (Exception e){
                    MFLogger.error(e.getMessage(),e);
                    throw new Exception("redis error");
                }
            }else{
                throw new Exception("opNo is empty");
            }
        }else{
            throw new Exception("request is empty");
        }
    }

    public void deleteUserInfo() throws Exception {
        HttpServletRequest request = getRequest();
        if(request != null){
            Object opNo = request.getAttribute("opNo");
            if(opNo != null) {
                try {
                    redisUtil.delete(this.userKeyPrefix + "_" + opNo,userDB);
                } catch (Exception e) {
                    MFLogger.error(e.getMessage(),e);
                    throw new Exception("redis error");
                }
            }else{
                throw new Exception("opNo is empty");
            }
        }else{
            throw new Exception("request is empty");
        }
    }

    public void setTemp(String key,Object value) throws Exception {
        HttpServletRequest request = getRequest();
        if(request != null){
            Object opNo = request.getAttribute("opNo");
            if(opNo != null) {
                try {
                    JSONObject jsonObject  = redisUtil.get(this.tempKeyPrefix + "_" + opNo,JSONObject.class,tempExpire,tempDB);
                    if(jsonObject == null ){
                        jsonObject = new JSONObject();
                    }
                    jsonObject.put(key,value);
                    redisUtil.set(this.tempKeyPrefix + "_" + opNo,jsonObject,tempExpire,tempDB);
                } catch (Exception e) {
                    MFLogger.error(e.getMessage(),e);
                    throw new Exception("redis error");
                }
            }else{
                throw new Exception("opNo is empty");
            }
        }else{
            throw new Exception("request is empty");
        }
    }

    public Object getTemp(String key){
        Object result = null;
        HttpServletRequest request = getRequest();
        if(request != null){
            Object opNo = request.getAttribute("opNo");
            if(opNo != null){
                try{
                    JSONObject jsonObject = redisUtil.get(this.tempKeyPrefix + "_" + opNo,JSONObject.class,tempExpire,tempDB);
                    if(jsonObject != null ){
                        result = jsonObject.get(key);
                    }
                }catch (Exception e){
                    MFLogger.error(e.getMessage(),e);
                }
            }
        }
        return result;
    }

    public void deleteTemp() throws Exception {
        HttpServletRequest request = getRequest();
        if(request != null){
            Object opNo = request.getAttribute("opNo");
            if(opNo != null) {
                try {
                    redisUtil.delete(this.tempKeyPrefix + "_" + opNo,tempDB);
                } catch (Exception e) {
                    MFLogger.error(e.getMessage(),e);
                    throw new Exception("redis error");
                }
            }else{
                throw new Exception("opNo is empty");
            }
        }else{
            throw new Exception("request is empty");
        }
    }

    public void removeTemp(String key) throws Exception {
        HttpServletRequest request = getRequest();
        if(request != null){
            Object opNo = request.getAttribute("opNo");
            if(opNo != null){
                try{
                    JSONObject jsonObject  = redisUtil.get(this.tempKeyPrefix + "_" + opNo,JSONObject.class,tempExpire,tempDB);
                    if(jsonObject == null ){
                        jsonObject = new JSONObject();
                    }
                    if(jsonObject.containsKey(key)){
                        jsonObject.remove(key);
                    }
                    redisUtil.set(this.tempKeyPrefix + "_" + opNo,jsonObject,tempExpire,tempDB);
                }catch (Exception e){
                    MFLogger.error(e.getMessage(),e);
                    throw new Exception("redis error");
                }
            }else{
                throw new Exception("opNo is empty");
            }
        }else{
            throw new Exception("request is empty");
        }
    }

}
