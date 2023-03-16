/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author guohanchen
 * @email 
 * @date 2020-11-16 14:00:20
 */
public interface DemoService {

    void saveEditTable(JSONObject jsonObject);

    List<Map<String, Object>> SysAreaList(JSONObject jsonObject);
}


