/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.service.impl;

import cn.mftcc.common.constant.SysConstant;
import cn.mftcc.common.exception.ServiceException;
import cn.mftcc.common.utils.MapperUtil;
import cn.mftcc.sys.components.demo.entity.DemoLeaseAppProjectInitEntity;
import cn.mftcc.sys.components.demo.mapper.DemoLeaseAppProjectInitMapper;
import cn.mftcc.sys.components.demo.service.DemoLeaseAppProjectInitService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目登记表
 *
 * @author guohanchen
 * @email
 * @date 2021-03-19 10:42:18
 */
@Service("demoLeaseAppProjectInitService")
@Transactional(rollbackFor = ServiceException.class)
public class DemoLeaseAppProjectInitServiceImpl implements DemoLeaseAppProjectInitService {

    @Autowired
    private DemoLeaseAppProjectInitMapper demoLeaseAppProjectInitMapper;
    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public IPage<DemoLeaseAppProjectInitEntity> findByPage(DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity) throws ServiceException {
        try{
            //翻页
            IPage<DemoLeaseAppProjectInitEntity> page = new Page<>();
            page.setCurrent(demoLeaseAppProjectInitEntity.getPageNo());
            page.setSize(demoLeaseAppProjectInitEntity.getPageSize());
            QueryWrapper<DemoLeaseAppProjectInitEntity> queryWrapper = new QueryWrapper<>();
            mapperUtil.tableQuery(queryWrapper,demoLeaseAppProjectInitEntity);
            return demoLeaseAppProjectInitMapper.selectPage(page,queryWrapper);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,demoLeaseAppProjectInitEntity.getAppId(),e);
        }
    }

    @Override
    public void insert(DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity) throws ServiceException {
        try{
            demoLeaseAppProjectInitMapper.insert(demoLeaseAppProjectInitEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SAVE_ERROR,demoLeaseAppProjectInitEntity.getAppId(),e);
        }
    }

    @Override
    public void update(DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity) throws ServiceException {
        try{
            demoLeaseAppProjectInitMapper.updateById(demoLeaseAppProjectInitEntity);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_UPDATE_ERROR,demoLeaseAppProjectInitEntity.getAppId(),e);
        }
    }

    @Override
    public DemoLeaseAppProjectInitEntity findById(String appId) throws ServiceException {
        try{
            return demoLeaseAppProjectInitMapper.selectById(appId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_SELECT_ERROR,appId,e);
        }
    }

    @Override
    public void deleteById(String appId) throws ServiceException {
        try{
            demoLeaseAppProjectInitMapper.deleteById(appId);
        }catch (Exception e){
            throw new ServiceException(SysConstant.MSG_CONFIG_DELETE_ERROR,appId,e);
        }
    }

    @Override
    public void saveEditTable(JSONObject jsonObject) {
        JSONArray insertData = jsonObject.getJSONArray("insertData");
        JSONArray updateData = jsonObject.getJSONArray("updateData");
        String editType = jsonObject.getString("editType");
        for(int i=0;i<insertData.size();i++){
            DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity = insertData.getJSONObject(i).toJavaObject(DemoLeaseAppProjectInitEntity.class);
            demoLeaseAppProjectInitMapper.insert(demoLeaseAppProjectInitEntity);
        }
        for(int i=0;i<updateData.size();i++){
            DemoLeaseAppProjectInitEntity demoLeaseAppProjectInitEntity = updateData.getJSONObject(i).toJavaObject(DemoLeaseAppProjectInitEntity.class);
            demoLeaseAppProjectInitMapper.updateById(demoLeaseAppProjectInitEntity);
        }
    }

    @Override
    public void sendMessage(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        map.put("msg",jsonObject.get("msg"));
        String opNo = jsonObject.getString("opNo");
        String[] subs = opNo.split(",");
//        TmpMessage.sendTmpMessage("25e429bb834249dab739fc771e50cfd2",map,subs);
        map.put("test",jsonObject.get("msg"));
//        transitMessageUtil.sendTransitMessage(
//                JSON.toJSONString(map),
//                new String[]{"consumer-appserver"},
//                "com.mftcc.app.modules.consumer.MQCallPushMsg");
    }

}
