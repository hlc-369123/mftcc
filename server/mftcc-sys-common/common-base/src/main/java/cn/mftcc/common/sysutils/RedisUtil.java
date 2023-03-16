/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
/**
 * 
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cn.mftcc.common.sysutils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 */
@Component
@RefreshScope
public class RedisUtil {


    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60L * 60L * 24L;

    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    @Autowired
    public Map<Integer,RedisTemplate<String, Object>> redisTemplateMap = new HashMap<>();

    public RedisTemplate gteRedisTemplate(int db) {
        RedisTemplate redisTemplate = redisTemplateMap.get(db);
        return redisTemplate;
    }

    public void set(String key, Object value, long expire,int db){
        RedisTemplate redisTemplate = gteRedisTemplate(db);
        redisTemplate.opsForValue().set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value,int db){
        set(key, value, NOT_EXPIRE,db);
    }

    public <T> T get(String key, Class<T> clazz, long expire,int db) {
        RedisTemplate redisTemplate = gteRedisTemplate(db);
        Object value = redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz,int db) {
        return get(key, clazz, NOT_EXPIRE,db);
    }

    /*
     * 取得一个值，并重新刷新过期时间
     */
    public String get(String key, long expire, int db) {
        RedisTemplate redisTemplate = gteRedisTemplate(db);
        Object value = redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return toJson(value);
    }

    public String get(String key, int db) {
        return get(key, NOT_EXPIRE, db);
    }

    public void delete(String key,int db) {
        RedisTemplate redisTemplate = gteRedisTemplate(db);
        redisTemplate.delete(key);
    }

    public void deleteByPrex(String prefix, int db){
        RedisTemplate redisTemplate = gteRedisTemplate(db);
        Set<String> keys = redisTemplate.keys(prefix + "*");
        redisTemplate.delete(keys);
    }

    public Set<String> getKeyByPrex(String prefix, int db){
        RedisTemplate redisTemplate = gteRedisTemplate(db);
        Set<String> keys = redisTemplate.keys(prefix + "*");
        return keys;
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(Object object, Class<T> clazz){
        return JSON.parseObject(toJson(object), clazz);
    }
}
