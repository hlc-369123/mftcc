/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.sys.controller;

import cn.mftcc.common.R;
import cn.mftcc.common.utils.ParmCacheUtil;
import cn.mftcc.sys.common.runner.CacheUtil;
import cn.mftcc.sys.components.sys.entity.SysParmKeyEntity;
import cn.mftcc.sys.components.sys.service.SysParmKeyService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.io.IOUtils;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 字典项键值
 *
 * @author liwei
 * @email 614226243@qq.com
 * @date 2020-02-26 10:20:27
 */
@RestController
@RequestMapping("sys/sysParmKey")
public class SysParmKeyController {

    @Autowired
    private SysParmKeyService sysParmKeyService;

    @Autowired
    private ParmCacheUtil parmCacheUtil;

    @Autowired
    private CacheUtil cacheUtil;

    @PostMapping("/findByPage")
    public R findByPage(@RequestBody SysParmKeyEntity sysParmKeyEntity) {
        IPage<SysParmKeyEntity> list = this.sysParmKeyService.findByPage(sysParmKeyEntity);
        return R.ok().put("list", list);
    }

    @PostMapping("/insert")
    public R insert(@RequestBody SysParmKeyEntity sysParmKeyEntity){
        try {
            SysParmKeyEntity parmKeyEntitys = this.sysParmKeyService.findById(sysParmKeyEntity.getKeyName());
            if(parmKeyEntitys == null){
                this.sysParmKeyService.insert(sysParmKeyEntity);
                return R.ok();
            }else{
                return R.error("新增失败,数据已存在");
            }
        } catch (Exception e) {
            return R.error("新增失败");
        }
    }

    @PutMapping("/update")
    public R update(@RequestBody SysParmKeyEntity sysParmKeyEntity){
        try {
            this.sysParmKeyService.update(sysParmKeyEntity);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    @GetMapping("/findById/{keyName}")
    public R findById(@PathVariable("keyName") String keyName){
        try {
            SysParmKeyEntity parmKeyEntity = this.sysParmKeyService.findById(keyName);
            return R.ok().put("data", parmKeyEntity);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @DeleteMapping("/deleteById/{keyName}")
    public R deleteById(@PathVariable("keyName") String keyName){
        try {
            this.sysParmKeyService.deleteById(keyName);
            return R.ok();
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    @PostMapping("getParmDicFile")
    public void getParmDicFile(@RequestBody JSONObject ajaxData, HttpServletResponse response) throws IOException {
        cacheUtil.initParmCaChe();
        JSONObject jsonObject = new JSONObject();
        JSONArray parmKeyArray = ajaxData.getJSONArray("parmKeyArray");
        if(parmKeyArray.size() != 0){
            for(int i = 0;i < parmKeyArray.size();i++){
                String keyName = parmKeyArray.getJSONObject(i).getString("keyName");
                JSONArray value = parmCacheUtil.getDict(keyName);
                jsonObject.put(keyName,value);
            }
        }else{
            String [] moldIds = {};
            if(StringUtils.isNotBlank(ajaxData.getString("moldId"))){
                moldIds = ajaxData.getString("moldId").split("\\|");
            }
            List<SysParmKeyEntity> parmKeyEntityList = new ArrayList<>();
            if(moldIds.length == 0){
                parmKeyEntityList = this.sysParmKeyService.findAll();
            }else{
                parmKeyEntityList = this.sysParmKeyService.findByMoldIds(Arrays.asList(moldIds));
            }

            for(SysParmKeyEntity parmKeyEntity : parmKeyEntityList){
                JSONArray value = parmCacheUtil.getDict(parmKeyEntity.getKeyName());
                jsonObject.put(parmKeyEntity.getKeyName(),value);
            }
        }

        // 配置文件下载
        byte[] data = JSONObject.toJSONBytes(jsonObject);

        response.resetBuffer();
        response.setHeader("Content-Disposition", "attachment");
        response.addHeader("file-name","parmDic.json");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/blob; charset=UTF-8");

        IOUtils.write(data,response.getOutputStream());

    }

    @PostMapping("getParmDic")
    public R getParmDic(@RequestBody JSONObject ajaxData) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray parmKeyArray = ajaxData.getJSONArray("parmKeys");
            for(int i = 0;i < parmKeyArray.size();i++){
                String keyName = parmKeyArray.getString(i);
                JSONArray value = parmCacheUtil.getDict(keyName);
                if(value == null){
                    value = new JSONArray();
                }
                List<JSONObject> list = JSONArray.parseArray(value.toJSONString(), JSONObject.class);
                Collections.sort(list, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        int a = o1.getInteger("seqn");
                        int b = o2.getInteger("seqn");
                        if (a < b) {
                            return -1;
                        } else if(a == b) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
                jsonObject.put(keyName,list);
            }

            return R.ok().put("data",jsonObject);
        } catch (Exception e) {
            return R.error("获取字典项失败");
        }
    }
}