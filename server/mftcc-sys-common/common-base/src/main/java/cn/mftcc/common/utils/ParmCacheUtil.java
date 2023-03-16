/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.utils;

import cn.mftcc.common.logger.MFLogger;
import cn.mftcc.common.sysutils.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RefreshScope
public class ParmCacheUtil {
    @Value("${spring.redis.db.parmCache:2}")
    private int parmCacheDB;

    @Value("${spring.redis.expire.parmCache:-1}")
    private long parmCacheExpire;

    @Value("${spring.redis.keyPrefix.parmCache:parmCache}")
    public String keyPrefix; //缓存在内存中命名前缀

    @Autowired
    private RedisUtil redisUtil;

    public void setDict(String key,Object value) {
        redisUtil.set(this.keyPrefix + "_" + key,value,parmCacheExpire,parmCacheDB);
    }

    public JSONArray getDict(String key) {
        JSONArray result = new JSONArray();
        if(!StringUtils.isEmpty(key)){
            try{
                result  = redisUtil.get(this.keyPrefix + "_" + key,JSONArray.class,parmCacheExpire,parmCacheDB);
            }catch (Exception e){
                MFLogger.error(e.getMessage(),e);
            }
        }
        return result;
    }

    public JSONObject getDictAll() {
        JSONObject result = new JSONObject();
        try{
            Set<String> keys = redisUtil.getKeyByPrex(this.keyPrefix + "_",parmCacheDB);
            for (String str : keys) {
                JSONArray resultArray  = redisUtil.get(str,JSONArray.class,parmCacheExpire,parmCacheDB);
                String prefix = this.keyPrefix + "_";
                String key = "";
                if (str.matches("^" + prefix + ".*")) {
                    key = str.substring(prefix.length());//截取前缀后面的字符串
                }
                result.put(key,resultArray);
            }
        }catch (Exception e){
            MFLogger.error(e.getMessage(),e);
        }
        return result;
    }

    public String getDict(String key,String code) {
        String value = "";
        try {
            JSONArray sysDictArray = getDict(key);
            for(int i = 0;i < sysDictArray.size();i++){
                JSONObject sysDictEntity =  sysDictArray.getJSONObject(i);
                if(code != null && code.equals(sysDictEntity.getString("optCode"))){
                    value = sysDictEntity.getString("optName");
                    break;
                }
            }
        } catch (Exception e) {
            value = "";
        }
        return value;
    }

    public String getOptName(String key,String code) {
        String value = "";
        try {
            JSONArray sysDictArray = getDict(key);
            for(int i = 0;i < sysDictArray.size();i++){
                JSONObject sysDictEntity =  sysDictArray.getJSONObject(i);
                if(code != null && code.equals(sysDictEntity.getString("optCode"))){
                    value = sysDictEntity.getString("optName");
                    break;
                }
            }
        } catch (Exception e) {
            value = "";
        }
        return value;
    }

    //全部清空，慎用
    public void deleteAll() {
        redisUtil.deleteByPrex(this.keyPrefix + "_",parmCacheDB);
    }

    /**
    *@desc 根据字典项名称获取字典项返回map
    *@author lwq        
    *@date 2019/5/22 15:13
    *@parm [key]
    *@return java.util.Map<java.lang.Object,java.lang.Object>
    **/
    public Map<Object,Object> getDicMap(String key){
        Map<Object,Object> dicMap;
        try {
            JSONArray sysDictArray = getDict(key);
            dicMap = new HashMap<>();
            for(int i = 0;i < sysDictArray.size();i++){
                JSONObject sysDictEntity =  sysDictArray.getJSONObject(i);
                dicMap.put(sysDictEntity.get("optCode"),sysDictEntity.get("optName"));
            }
        } catch (Exception e) {
            dicMap = null;
        }
        return dicMap;
    }

}
