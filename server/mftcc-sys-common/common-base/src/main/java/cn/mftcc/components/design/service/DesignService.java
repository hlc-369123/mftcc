/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.components.design.service;

import cn.mftcc.common.sysutils.RedisUtil;
import cn.mftcc.components.design.utils.FileHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service("designService")
public class DesignService {

    @Value("${mftcc.design.file-path:}")
    private String filePath;
    @Value("${spring.redis.db.form:4}")
    private int db;
    @Value("${spring.redis.expire.form:-1}")
    private long expire;
    @Autowired
    private RedisUtil redisUtil;

    private final String SUFFIX = ".json";

    public JSONArray list(String productType,String type) {
        String path = filePath + File.separator + type;
        JSONArray r = FileHelper.readDir(path, type, null);
        return r;
    }

    public Object open(String productType, String type, String fileName) {
        String path = filePath + File.separator + type + File.separator + productType + File.separator + fileName;
        String content = FileHelper.readFile(path);
        return JSONObject.parseObject(content);
    }


    public void save(String productType, String type, String name, String content) {
        String path = filePath + File.separator + type + File.separator + productType + File.separator + name + SUFFIX;
        FileHelper.writeFile(path, content);
        redisUtil.set(productType+"\\"+name,content,expire,db);
    }

    public Object find(String productType, String type, String name) {
        String path = filePath + File.separator + type;
        JSONArray r = FileHelper.readDir(path, type, name);
        return r;
    }

    public boolean del(String productType, String type, String name) {
        String path = filePath + File.separator + type + File.separator + productType + File.separator + name + SUFFIX;
        boolean flag = FileHelper.delFile(path);
        redisUtil.delete(productType+"\\"+name,db);
        return flag;
    }
}
