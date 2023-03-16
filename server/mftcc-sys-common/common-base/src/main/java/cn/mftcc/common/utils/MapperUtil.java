/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.utils;

import cn.mftcc.common.exception.DataSourceException;
import cn.mftcc.common.logger.MFLogger;
import cn.mftcc.common.sysutils.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName MasterSlaveUtil
 * @Description
 * @Author 郭涵晨
 * @Date 2020/2/6 14:35
 */
@RefreshScope
@Component
public class MapperUtil {

    @Value("${spring.redis.db.form:4}")
    private int db;
    @Value("${spring.redis.expire.form:-1}")
    private long expire;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 主从表新增
     * @param childEntity 从表实体
     * @param childMapper 从表Mapper
     * @param masterMapper 主表Mapper
     */
    public static void insert4MasterSlave(Object childEntity, BaseMapper childMapper, BaseMapper masterMapper) {
        try {
            childMapper.insert(childEntity);
            Class DBClass = (Class)childEntity.getClass().getMethod("getClz").invoke(childEntity);
            Object masterEntity = DBClass.newInstance();
            convertEntity(childEntity, masterEntity);
            ObjectMapper objectMapper = new ObjectMapper();
            masterMapper.insert(objectMapper.convertValue(masterEntity, DBClass));
        } catch (Exception e){
            throw new DataSourceException("主从表新增异常");
        }
    }
    /**
     * 主从表删除
     * @param id 主键
     * @param childMapper 从表Mapper
     * @param masterMapper 主表Mapper
     */
    public static void delete4MasterSlave(String id, BaseMapper childMapper, BaseMapper masterMapper) {
        try {
            childMapper.deleteById(id);
            masterMapper.deleteById(id);
        } catch (Exception e){
            throw new DataSourceException("主从表删除异常");
        }
    }
    /**
     * 主从表更新
     * @param childEntity 从表实体
     * @param childMapper 从表Mapper
     * @param masterMapper 主表Mapper
     */
    public static void update4MasterSlave(Object childEntity, BaseMapper childMapper, BaseMapper masterMapper) {
        try {
            childMapper.updateById(childEntity);
            Class DBClass = (Class)childEntity.getClass().getMethod("getClz").invoke(childEntity);
            Object masterEntity = DBClass.newInstance();
            convertEntity(childEntity, masterEntity);
            ObjectMapper objectMapper = new ObjectMapper();
            masterMapper.updateById(objectMapper.convertValue(masterEntity, DBClass));
        } catch (Exception e){
            MFLogger.error(e.getMessage(),e);
            throw new DataSourceException("主从表更新异常");
        }
    }
    /**
     * 主从查询findById
     * @param id 主键
     * @param childMapper 从表Mapper
     * @param masterMapper 主表Mapper
     * @return
     */
    public static Object findById4MasterSlave(String id, BaseMapper childMapper, BaseMapper masterMapper) {
        try {
            Object childEntity = childMapper.selectById(id);
            Object masterEntity = masterMapper.selectById(id);
            JSONObject jsonObject = mergeEntity(childEntity, masterEntity);
            return jsonObject;
        } catch (Exception e){
            throw new DataSourceException("主从表查询异常");
        }
    }

    /**
     * 主从实体数据转换
     * @param childEntity 从表实体
     * @param masterEntity 主表实体
     * @throws Exception
     */
    private static void convertEntity(Object childEntity, Object masterEntity) throws Exception{
        Class DBClass = masterEntity.getClass();
        BeanInfo childBeanInfo = Introspector.getBeanInfo(childEntity.getClass());
        BeanInfo masterBeanInfo = Introspector.getBeanInfo(DBClass);
        PropertyDescriptor[] childPD = childBeanInfo.getPropertyDescriptors();
        PropertyDescriptor[] masterPD = masterBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor mp : masterPD) {
            String masterKey = mp.getName();
            for (PropertyDescriptor cp : childPD) {
                String childKey = cp.getName();
                if(masterKey.equals(childKey) && !"class".equals(masterKey)){
                    Object value = cp.getReadMethod().invoke(childEntity);
                    mp.getWriteMethod().invoke(masterEntity,value);
                }
            }
        }
    }

    /**
     * 主从表数据合并
     * @param childEntity 从表实体
     * @param masterEntity 主表实体
     * @return
     * @throws Exception
     */
    private static JSONObject mergeEntity(Object childEntity, Object masterEntity) throws Exception{
        JSONObject jsonObject = new JSONObject();
        BeanInfo childBeanInfo = Introspector.getBeanInfo(childEntity.getClass());
        PropertyDescriptor[] childPD = childBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor cp : childPD) {
            String childKey = cp.getName();
            if(!"clz".equals(childKey) && !"class".equals(childKey)){
                Object value = cp.getReadMethod().invoke(childEntity);
                jsonObject.put(childKey, value);
            }
        }
        BeanInfo masterBeanInfo = Introspector.getBeanInfo(masterEntity.getClass());
        PropertyDescriptor[] masterPD = masterBeanInfo.getPropertyDescriptors();
        for (PropertyDescriptor mp : masterPD) {
            String masterKey = mp.getName();
            if(!"clz".equals(masterKey) && !"class".equals(masterKey)) {
                Object value = mp.getReadMethod().invoke(masterEntity);
                jsonObject.put(masterKey, value);
            }
        }
        return jsonObject;
    }

    /**
     * 动态查询 - 多列
     * @param queryWrapper mybatisplus查询构造器
     * @param query 查询数据
     * @param fields 需要查询的字段
     */
    public static void dynamicQuery(QueryWrapper queryWrapper,  String query, String...fields) {
        if(StringUtils.isEmpty(query)){
            return;
        }

        queryWrapper.nested(new Function<QueryWrapper, QueryWrapper>() {
            @Override
            public QueryWrapper apply(QueryWrapper wrapper) {
                for(int i = 0; i<fields.length; i++){
                    wrapper.like(fields[i],query);
                    if(i < fields.length-1){
                        wrapper.or();
                    }
                }
                return wrapper;
            }
        });
    }

    /***
     * 实体类字段映射成数据库字段
     * @param fieldName
     * @return
     */
    public static String changeColForSqlCol(String fieldName){
        if(fieldName!=null){
            StringBuffer sb = new StringBuffer();
            char fieldChar[] = fieldName.toCharArray();
            for(char letter:fieldChar){
                if(Character.isUpperCase(letter)){//判断是大写
                    sb.append("_");
                }
                sb.append(Character.toUpperCase(letter));
            }
            fieldName = sb.toString();
        }
        return fieldName;
    }

    /**
     * 自定义查询
     * @param queryWrapper mybatisplus查询构造器
     * @param queryArray 查询数据
     */
    public static void customQuery(QueryWrapper queryWrapper,  JSONArray queryArray) {

        for(int i=0;i<queryArray.size();i++){
            JSONObject jsonObject = queryArray.getJSONObject(i);
            String andor = jsonObject.getString("andor");
            String fieldName = jsonObject.getString("fieldName");
            String sqlField = changeColForSqlCol(fieldName);
            String conditions = jsonObject.getString("conditions");
            Object valueObj = jsonObject.get("value");
            String value = valueObj+"";

            if("or".equals(andor)){
                queryWrapper.or();
            }
            if("eq".equals(conditions)){
                //等于
                queryWrapper.eq(!StringUtils.isEmpty(value),sqlField,value);
            }else if("ne".equals(conditions)){
                //不等于
                queryWrapper.ne(!StringUtils.isEmpty(value),sqlField,value);
            }else if("like".equals(conditions)){
                //包含
                queryWrapper.like(!StringUtils.isEmpty(value),sqlField,value);
            }else if("notLike".equals(conditions)){
                //不包含
                queryWrapper.notLike(!StringUtils.isEmpty(value),sqlField,value);
            }else if("gt".equals(conditions)){
                //大于
                queryWrapper.gt(!StringUtils.isEmpty(value),sqlField,value);
            }else if("lt".equals(conditions)){
                //小于
                queryWrapper.lt(!StringUtils.isEmpty(value),sqlField,value);
            }else if("ge".equals(conditions)){
                //大于等于
                queryWrapper.ge(!StringUtils.isEmpty(value),sqlField,value);
            }else if("le".equals(conditions)){
                //小于等于
                queryWrapper.le(!StringUtils.isEmpty(value),sqlField,value);
            }else if("eqMultiple".equals(conditions)){
                //多选等于
                queryWrapper.nested(new Function<QueryWrapper, QueryWrapper>() {
                    @Override
                    public QueryWrapper apply(QueryWrapper wrapper) {
                        JSONArray valueArr = (JSONArray)valueObj;
                        for(int v=0;v<valueArr.size();v++){
                            String value = valueArr.getString(v);
                            wrapper.eq(!StringUtils.isEmpty(value),sqlField,value);
                        }
                        return wrapper;
                    }
                });
            }else if("neMultiple".equals(conditions)){
                //多选不等于
                queryWrapper.nested(new Function<QueryWrapper, QueryWrapper>() {
                    @Override
                    public QueryWrapper apply(QueryWrapper wrapper) {
                        JSONArray valueArr = (JSONArray)valueObj;
                        for(int v=0;v<valueArr.size();v++){
                            String value = valueArr.getString(v);
                            wrapper.ne(!StringUtils.isEmpty(value),sqlField,value);
                        }
                        return wrapper;
                    }
                });
            }else if("in".equals(conditions)){
                //多选包含
                JSONArray valueArr = (JSONArray)valueObj;
                queryWrapper.in(valueArr.size()>0,sqlField,valueArr);
            }else if("notIn".equals(conditions)){
                //多选不包含
                JSONArray valueArr = (JSONArray)valueObj;
                queryWrapper.notIn(valueArr.size()>0,sqlField,valueArr);
            }else if("between".equals(conditions)){
                //在...之间
                JSONArray valueArr = (JSONArray)valueObj;
                if(valueArr.size()==2){
                    queryWrapper.between(sqlField,valueArr.getString(0),valueArr.getString(1));
                }
            }
        }
    }

    /**
     * 自定义查询递归算法
     * @param queryWrapper mybatisplus查询构造器
     * @param queryArray 递归数据
     */
    private static void convertCustomQuery(QueryWrapper queryWrapper, JSONArray queryArray) {
        for(int i=0;i<queryArray.size();i++){
            Object obj = queryArray.get(i);
            if(obj instanceof String){
                if("or".equals(obj)){
                    queryWrapper.or();
                }else if("and".equals(obj)){
                    //默认操作
                }
            }else if(obj instanceof JSONObject){
                JSONObject jsonObject = (JSONObject)obj;
                String field = jsonObject.getString("field");
                String operator = jsonObject.getString("operator");
                String value = jsonObject.getString("value");
                switch (operator){
                    case "=":
                        queryWrapper.eq(field,value);
                        break;
                    default:
                        break;
                }
            }if(obj instanceof JSONArray){
                queryWrapper.nested(new Function<QueryWrapper, QueryWrapper>() {
                    @Override
                    public QueryWrapper apply(QueryWrapper wrapper) {
                        convertCustomQuery(wrapper,(JSONArray)obj);
                        return wrapper;
                    }
                });

            }
        }
    }

    /**
     * 转换大小写
     *
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
            }
            sb.append(Character.toUpperCase(c));  //统一都转大写
        }
        return sb.toString();
    }

    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            Method method = o.getClass().getMethod("getSort", new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 列表表单通用查询
     * @param queryWrapper mybatisplus查询构造器
     * @param queryEntity 查询数据
     */
    public void tableQuery(QueryWrapper queryWrapper,Object queryEntity) {
        Object result = null;
        Object initQuery = null;
        Object tableId = "";
        Object sort = "";
        Object customQuery = null;
        Class cls = queryEntity.getClass();
        String selectKey = "";
        String selectValue = "";
        Object queryTab = null;
        try {
            Method getDynamicQuery = cls.getMethod("getDynamicQuery");
            Method getCustomQuery = cls.getMethod("getCustomQuery");
            Method getInitQuery = cls.getMethod("getInitQuery");
            Method getTableId = cls.getMethod("getTableId");
            Method getSort = cls.getMethod("getSort");
            Method getQueryTab = cls.getMethod("getQueryTab");
            result = getDynamicQuery.invoke(queryEntity);
            initQuery = getInitQuery.invoke(queryEntity);
            customQuery = getCustomQuery.invoke(queryEntity);
            tableId = getTableId.invoke(queryEntity);
            sort = getSort.invoke(queryEntity);
            queryTab = getQueryTab.invoke(queryEntity);
            Method getSelectKey = cls.getMethod("getSelectKey");
            Method getSelectValue = cls.getMethod("getSelectValue");
            Object selectKeyObj = getSelectKey.invoke(queryEntity);
            Object selectValueObj = getSelectValue.invoke(queryEntity);
            if(selectKeyObj!=null){
                selectKey = selectKeyObj.toString();
                selectValue = selectValueObj.toString();
            }
        } catch (Exception e){
            MFLogger.error(e.getMessage(),e);
        }

        if(!StringUtils.isEmpty(selectValue)&& !StringUtils.isEmpty(selectKey)){
            String[] valueArr = selectValue.split(",");
            String fieldName = humpToLine(selectKey);
            queryWrapper.in(fieldName, valueArr);
            return;
        }

        //排序解析
        if(!StringUtils.isEmpty(sort) && !"[]".equals(sort) && sort != ""){
            List<String> fileNameAsc = new ArrayList<>();
            List<String> fileNameDesc = new ArrayList<>();
            String[] arrAsc = new String[0];
            String[] arrDesc = new String[0];
            //json串转数组
            JSONArray sortArray = JSONArray.parseArray(sort.toString());
            //遍历
            for(int i = 0;i < sortArray.size();i++){
                JSONObject object = (JSONObject) sortArray.get(i);
                //与前端配置条件做比较 判断ASC OR DESC
                if ("ascending".equals(object.get("order"))){
                    //实体类字段转为数据库字段
                    fileNameAsc.add(camelToUnderline(object.get("filedName").toString()));
                    //集合转为数组 mybatisplus需要传递数组
                    arrAsc = fileNameAsc.toArray(arrAsc);
                }else if ("descending".equals(object.get("order"))){
                    fileNameDesc.add(camelToUnderline(object.get("filedName").toString()));
                    arrDesc = fileNameDesc.toArray(arrDesc);
                }
            }
            queryWrapper.orderByAsc(arrAsc);
            queryWrapper.orderByDesc(arrDesc);
        }

        if(StringUtils.isEmpty(tableId)){
            return;
        }
        JSONObject tableData = getTableDataJson(tableId.toString());
        if(StringUtils.isEmpty(tableData)){
            return;
        }
        if(!StringUtils.isEmpty(initQuery)){
            JSONObject initQueryObject = JSONObject.parseObject(initQuery.toString());
            for(String key : initQueryObject.keySet()){
                String value = initQueryObject.getString(key);
                String fieldName = humpToLine(key);
                queryWrapper.eq(!StringUtils.isEmpty(value),fieldName, value);
            }
        }

        String modelType = tableData.getString("modelType");
        if("queryModel".equals(modelType)||"expandModel".equals(modelType)){
            JSONArray columns = tableData.getJSONArray("columns");
            for(int i=0;i<columns.size();i++){
                JSONObject colObj = columns.getJSONObject(i);
                boolean isQuery = colObj.getBoolean("isQuery");
                Object isOtherQuery = colObj.get("isOtherQuery");
                if(isQuery||(isOtherQuery !=null && (boolean)isOtherQuery)){
                    String queryType = colObj.getString("queryType");
                    String prop = colObj.getString("prop");
                    String queryTableAlias = colObj.getString("queryTableAlias");
                    String fieldMethod = "get" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
                    Object queryResult = null;
                    try {
                        Method getFieldMethod = cls.getMethod(fieldMethod);
                        queryResult = getFieldMethod.invoke(queryEntity);
                    } catch (Exception e){
                        MFLogger.error(e.getMessage(),e);
                    }
                    if(queryResult == null || "".equals(queryResult)){
                        continue;
                    }
                    String fieldName = humpToLine(prop);
                    if(!StringUtils.isEmpty(queryTableAlias)){
                        fieldName = queryTableAlias + "." + fieldName;
                    }
                    if(colObj.get("isBetween")!=null&&colObj.getBoolean("isBetween")){
                        JSONArray resultArray = JSONArray.parseArray(queryResult.toString());
                        queryWrapper.between(!StringUtils.isEmpty(queryResult),fieldName, resultArray.getString(0), resultArray.getString(1));
                    }else if("1".equals(queryType)){
                        //精确查询
                        queryWrapper.eq(!StringUtils.isEmpty(queryResult),fieldName, queryResult.toString());
                    }else if("2".equals(queryType)){
                        //模糊查询
                        queryWrapper.like(!StringUtils.isEmpty(queryResult),fieldName, queryResult.toString());
                    }
                }
            }
        }
        if("dynamicModel".equals(modelType)||"expandModel".equals(modelType)){
            JSONObject queryObj = tableData.getJSONObject("dynamicQuery");
            JSONArray fieldNames = queryObj.getJSONArray("fieldNames");
            if(result == null ){
                return;
            }
            String finalQuery = result.toString();
            if(!StringUtils.isEmpty(finalQuery)){
                queryWrapper.nested(new Function<QueryWrapper, QueryWrapper>() {
                    @Override
                    public QueryWrapper apply(QueryWrapper wrapper) {
                        for(int i = 0; i<fieldNames.size(); i++){
                            String fieldName = humpToLine(fieldNames.getString(i));
                            if("dynamicModel".equals(modelType)){
                                /*wrapper.nested(new Function<QueryWrapper, QueryWrapper>() {
                                    @Override
                                    public QueryWrapper apply(QueryWrapper wrapperInner) {
                                        int length = finalQuery.length();
                                        for(int i=0;i<length;i++){
                                            char c = finalQuery.charAt(i);
                                            String s = String.valueOf(c);
                                            wrapperInner.like(!StringUtils.isEmpty(s),fieldName, s);
                                        }
                                        return wrapperInner;
                                    }
                                });*/
                                int length = finalQuery.length();
                                String queryStr = "";
                                for(int f=0;f<length;f++){
                                    char c = finalQuery.charAt(f);
                                    String s = String.valueOf(c);
                                    queryStr += s + "%";
                                }
                                if(queryStr.length()>0){
                                    queryStr = queryStr.substring(0,queryStr.length()-1);
                                }
                                wrapper.like(!StringUtils.isEmpty(queryStr),fieldName, queryStr);
                            }else{
                                wrapper.like(!StringUtils.isEmpty(finalQuery),fieldName, finalQuery);
                            }
                            if(i < fieldNames.size()-1){
                                wrapper.or();
                            }
                        }
                        return wrapper;
                    }
                });
            }

            if(customQuery!=null){
                JSONArray jsonArray = JSONArray.parseArray(customQuery.toString());
                customQuery(queryWrapper,jsonArray);
            }else{
                /*筛选表达式*/
                if(!StringUtils.isEmpty(queryTab)){
                    String exprStr = queryTab.toString();
                    //替换中英文（(
                    if (exprStr.contains("（") || exprStr.contains("）")|| exprStr.contains("！")){
                        exprStr = exprStr.replace("（","(").replace("）",")").replace("！","!");
                    }
                    String regex = "\\$\\{([^}]*)\\}";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(exprStr);
                    while (matcher.find()) {
                        exprStr = exprStr.replaceAll("\\$\\{"+matcher.group(1)+"\\}", humpToLine(matcher.group(1)));
                    }
                    //替换特殊字符为逻辑符号
                    exprStr = exprStr.replace("=="," = ");
                    exprStr = exprStr.replace("&&"," and ");
                    exprStr = exprStr.replace("||"," or ");
                    queryWrapper.apply(exprStr);
                }else{
                    JSONArray queryTabs = tableData.getJSONArray("queryTabs");
                    Map<String,String> map = new HashMap<>();
                    for(int i=0;i<queryTabs.size();i++){
                        if("expr".equals(queryTabs.getJSONObject(i).getString("filterType"))){
                            continue;
                        }
                        String fieldName = queryTabs.getJSONObject(i).getString("fieldName");
                        if(StringUtils.isEmpty(fieldName)){
                            continue;
                        }
                        if(map.get(fieldName) == null){
                            map.put(fieldName,fieldName);
                        }else{
                            continue;
                        }
                        String fieldMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        try {
                            Method getFieldMethod = cls.getMethod(fieldMethod);
                            result = getFieldMethod.invoke(queryEntity);
                        } catch (Exception e){
                            MFLogger.error(e.getMessage(),e);
                        }
                        if(result == null){
                            continue;
                        }
                        fieldName = humpToLine(fieldName);
                        queryWrapper.eq(!StringUtils.isEmpty(result),fieldName, result.toString());
                    }
                }
            }



        }else if("nothingModel".equals(modelType)){

        }
    }

    private JSONObject getTableDataJson(String tableId){
        JSONObject tableJson = null;
        try {
            if(!StringUtils.isEmpty(tableId)){
                tableId = tableId.replaceAll("\\\\","/");
            }else{
                throw new Exception("列表编号为空");
            }
            tableJson = redisUtil.get(tableId,JSONObject.class,expire,db);
            if(tableJson == null){
                tableId = tableId.replaceAll("/","\\\\");
                tableJson = redisUtil.get(tableId,JSONObject.class,expire,db);
                if(tableJson == null){
                    throw new Exception(tableId + "文件不存在或内容格式错误<br/>");
                }
            }
        } catch (Exception e){
            MFLogger.error(e.getMessage(),e);
        }
        return tableJson;
    }

    /**
     * 字段驼峰转下划线
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        String pStr = "[A-Z]";
        Pattern humpPattern = Pattern.compile(pStr);
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
