/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.sysutils;

import cn.mftcc.common.logger.MFLogger;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RefreshScope
public class MsgCacheUtil {
    @Value("${spring.redis.db.msgCache:3}")
    private int msgCacheDB;

    @Value("${spring.redis.expire.msgCache:-1}")
    private long msgCacheExpire;

    @Value("${spring.redis.keyPrefix.msgCache:msgCache}")
    public String keyPrefix; //缓存在内存中命名前缀

    @Autowired
    private RedisUtil redisUtil;

    public void setMsg(String key,Object value) {
        redisUtil.set(this.keyPrefix + "_" + key,value,msgCacheExpire,msgCacheDB);
    }

    public JSONObject getMsgAll() {
        JSONObject result = new JSONObject();
        try{
            Set<String> keys = redisUtil.getKeyByPrex(this.keyPrefix + "_",msgCacheDB);
            for (String str : keys) {
                JSONObject resultArray  = redisUtil.get(str,JSONObject.class,msgCacheExpire,msgCacheDB);
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

    public JSONObject getMsg(String key) {
        JSONObject sysMsgConfigEntity = null;
        try {
            sysMsgConfigEntity = redisUtil.get(this.keyPrefix + "_" + key,JSONObject.class,msgCacheExpire,msgCacheDB);
        } catch (Exception e) {
            MFLogger.error(e.getMessage(),e);
        }
        return sysMsgConfigEntity;
    }

    //全部清空，慎用
    public void deleteAll() {
        redisUtil.deleteByPrex(this.keyPrefix + "_",msgCacheDB);
    }


}
