/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.utils;

import cn.mftcc.common.exception.ServiceException;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RefreshScope
public class AuthorityUtil {
    @Value("${spring.redis.db.auth:5}")
    private int authDB;

    @Value("${spring.redis.expire.auth:-1}")
    private long authExpire;

    @Value("${spring.redis.keyPrefix.auth:auth}")
    public String authPrefix; //缓存在内存中命名前缀

    @Autowired
    private RedisUtil redisUtil;

    public void setAuth(String roleNo,String key,Object value) throws Exception {
        try{
            JSONObject jsonObject  = redisUtil.get(this.authPrefix + "_" + roleNo,JSONObject.class,authExpire,authDB);
            if(jsonObject == null ){
                jsonObject = new JSONObject();
            }
            jsonObject.put(key,value);
            redisUtil.set(this.authPrefix + "_" + roleNo,jsonObject,authExpire,authDB);
        }catch (Exception e){
            MFLogger.error(e.getMessage(),e);
            throw new Exception("redis error");
        }
    }


    public Object getAuth(String roleNo,String key) {
        Object result = null;
        if(!StringUtils.isEmpty(key)){
            try{
                JSONObject jsonObject = redisUtil.get(this.authPrefix + "_" + roleNo,JSONObject.class,authExpire,authDB);
                if(jsonObject != null ){
                    result = jsonObject.get(key);
                }
            }catch (Exception e){
                MFLogger.error(e.getMessage(),e);
            }
        }
        return result;
    }

    public JSONArray getAuthArray(String roleNo,String key) {
        JSONArray result = new JSONArray();
        if(!StringUtils.isEmpty(key)){
            try{
                JSONObject jsonObject = redisUtil.get(this.authPrefix + "_" + roleNo,JSONObject.class,authExpire,authDB);
                if(jsonObject != null ){
                    result = jsonObject.getJSONArray(key);
                }
            }catch (Exception e){
                MFLogger.error(e.getMessage(),e);
            }
        }
        return result;
    }

    /**
     * 获取自定义查询条件
     * @param opNo   当前登录人编号
     * @param roleNo 当前登录人角色编号
     * @param brNo 当前登录人部门编号
     * @param corpId 当前登录人法人
     * @param mapperPath  本次查询调用类的Mapper路径
     * @param methodName 本次查询调用类的Mapper方法
     * @param brNoList  当前登录人部门及子部门的列表
     * @return
     * @throws ServiceException
     */
    public  String getRightQuerySql(String opNo,String roleNo,String brNo,String corpId,String mapperPath,String methodName,List<String> brNoList)throws ServiceException{
        String sql="";
            JSONArray authArray = new JSONArray();
//        JSONObject jsonObject = null;
            String[] roleNoArr = roleNo.split("\\|");
            for(String rno : roleNoArr){
                String roleId = corpId + rno;
                JSONArray jsonArray = getAuthArray(roleId,"dataAuth");
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String mapperPaths = object.getString("mapperPath");
                    if((mapperPath+"."+methodName).equals(mapperPaths)){
//                    if(jsonObject == null ){
//                        jsonObject = object;
//                    }
                        authArray.add(object);
                    }
                }
            }

            Map<String,Object> columnMap = new HashMap<>();
            if(authArray.size()>0){
                for(int a=0;a<authArray.size();a++){
                    JSONObject jsonObject = authArray.getJSONObject(a);
                    JSONArray dataAuthArr = jsonObject.getJSONArray("dataAuth");
                    String authType = jsonObject.getString("authType");
                    //全部
                    if("99".equals(authType)){
                        return sql;
                    }
                    String authField = "";
                    String type = "";
                    JSONArray brNos = new JSONArray();
                    if(!"0".equals(authType)){
                        for(int i=0;i<dataAuthArr.size();i++){
                            if(authType.equals(dataAuthArr.getJSONObject(i).getString("id"))){
                                authField = dataAuthArr.getJSONObject(i).getString("field");
                                type = dataAuthArr.getJSONObject(i).getString("type");
                                brNos = dataAuthArr.getJSONObject(i).getJSONArray("brNos");
                                break;
                            }
                        }
                    }
                    switch (authType){
                        //本人 - reg_op_no
                        case "1":
                            columnMap.put(authField,opNo);
                            break;
                        //本级 - reg_br_no
                        case "4":
                            columnMap.put(authField,brNo);
                            break;
                        //本级及下级 - reg_br_no
                        case "7":
                            columnMap.put(authField,brNoList);
                            break;
                        //本法人 - corp_id
                        case "10":
                            columnMap.put(authField,corpId);
                            break;
                        default:
                            Object value = "";
                            switch (type){
                                case "1":
                                    value = opNo;
                                    break;
                                case "4":
                                    value = brNo;
                                    break;
                                case "7":
                                    value = brNoList;
                                    break;
                                case "10":
                                    value = corpId;
                                    break;
                                case "88":
                                    List<String> brNosList = brNos.toJavaList(String.class);
                                    value = brNosList;
                                    break;
                                default:
                                    break;
                            }
                            columnMap.put(authField,value);
                            break;
                    }
                }
            }else{
                columnMap.put("1","0");
            }

        Set<String> keys = columnMap.keySet();
        Iterator<String> keyIter = keys.iterator();
        StringBuilder constraintsBuffer=new StringBuilder();
        while(keyIter.hasNext()) {
            String key = keyIter.next();
            String[] fieldArr = key.split(",");
            Object value = columnMap.get(key);
            if(StringUtils.isEmpty(value.toString())){
                constraintsBuffer.append(" 1=1 ");
                break;
            }
            for(int f=0;f<fieldArr.length;f++){
                String field = fieldArr[f];
                if(value instanceof String){
                    constraintsBuffer.append(field).append(" = '" + value + "'");
                }else if(value instanceof List){
                    List<String> list = (List<String>)value;
                    if(list!=null&&list.size()>0){
                        String str = "(";
                        for(int i=0;i<list.size();i++){
                            str += "'"+list.get(i)+"'";
                            if(i<list.size()-1){
                                str += ",";
                            }
                        }
                        str += ")";
                        constraintsBuffer.append(field).append(" in " + str);
                    }
                }
                if(f<fieldArr.length-1){
                    constraintsBuffer.append(" or ");
                }
            }
            if(keyIter.hasNext()){
                constraintsBuffer.append(" or ");
            }
        }
            sql=constraintsBuffer.toString();
            return sql;
        }


}


