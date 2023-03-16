/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.utils;

import cn.mftcc.common.logger.MFLogger;
import cn.mftcc.common.sysutils.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RefreshScope
@Component
public class FormUtil {

    @Value("${mftcc.form.mode:}")
    private String mode;
    @Value("${mftcc.form.file.path:}")
    private String path;
    @Value("${spring.redis.db.form:4}")
    private int db;
    @Value("${spring.redis.expire.form:-1}")
    private long expire;
    @Value("${spring.redis.keyPrefix.form:form}")
    public String keyPrefix;
    @Autowired
    private RedisUtil redisUtils;

    public Map<String,Object> validateFormData(String formId, Object object){
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder message = new StringBuilder();
        Boolean validate = true;
        JSONObject formjson = new JSONObject();
        try {
            if("file".equals(mode)){
                File file = new File(path + File.separatorChar + "form" + File.separatorChar + formId + ".json");
                if (file.exists()) {
                    String formStr = FileUtils.readFileToString(file);
                    formjson = JSONObject.parseObject(formStr);
                } else {
                    validate = false;
                    message.append(formId + "不存在<br/>");
                }
            }else if("redis".equals(mode)){
                if(redisUtils == null){
                    redisUtils = new RedisUtil();
                }
                formjson = redisUtils.get(keyPrefix + "_" + formId,JSONObject.class,expire,db);
            }else{
                validate = false;
                message.append("mode配置错误<br/>");
            }
        }catch (Exception e){
            validate = false;
            message.append(formId + "文件不存在或内容格式错误<br/>");
        }
        if(validate == true){
            try{
                JSONArray formItemReal = formjson.getJSONArray("formItem");
                JSONArray formItems = new JSONArray();
                for(int i = 0;i < formItemReal.size();i++){
                    JSONObject formItem = formItemReal.getJSONObject(i);
                    if("group".equals(formItem.getString("fieldType"))){
                        JSONArray formGroupItems = formItem.getJSONArray("group");
                        for(int j = 0; j < formGroupItems.size(); j++){
                            JSONObject formGroupItem = formGroupItems.getJSONObject(j);
                            formGroupItem.put("label",formItem.getString("label"));
                            formItems.add(formGroupItem);
                        }
                    }else{
                        formItems.add(formItem);
                    }
                }
                for(int i = 0;i < formItems.size();i++){
                    JSONObject formItem = formItems.getJSONObject(i);
                    Object formItemValue = getFieldValueByName(formItem.getString("fieldName"),object);
                    String fieldType = getFiledType(formItem.getString("fieldName"),object);
                    JSONArray rules = formItem.getJSONArray("rules");
                    for(int j = 0;j < rules.size();j++){
                        JSONObject rule = rules.getJSONObject(j);
                        Map<String,Object> checkField = checkField(formItemValue,fieldType,rule,formItem.getString("label"));
                        if((Boolean) checkField.get("validate") == false){
                            validate = false;
                            message.append(checkField.get("message"));
                        }
                    }
                }
            }catch (Exception e){
                MFLogger.error(e.getMessage(),e);
                validate = false;
                message = new StringBuilder();
                message.append(formId + "解析出错<br/>");
            }
        }
        map.put("flag",validate);
        map.put("msg",message);
        return  map;
    }

    public Map<String,Object> checkField(Object formItemValue,String fieldType,JSONObject rule ,String label) throws Exception {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("validate",true);
        double lenght = -1.0;
        if("byte".equals(fieldType) || "class java.lang.Byte".equals(fieldType) ||
                "int".equals(fieldType) || "class java.lang.Integer".equals(fieldType) ||
                "short".equals(fieldType) || "class java.lang.Short".equals(fieldType) ||
                "long".equals(fieldType) || "class java.lang.Long".equals(fieldType) ||
                "double".equals(fieldType) || "class java.lang.Double".equals(fieldType) ||
                "float ".equals(fieldType) || "class java.lang.Float".equals(fieldType)){
            lenght = Double.parseDouble(formItemValue.toString());
        }else if("class java.lang.String".equals(fieldType) || "char".equals(fieldType) || "class java.lang.Character".equals(fieldType)){
            lenght = String.valueOf(formItemValue).length();
        }else if("boolean".equals(fieldType) || "class java.lang.Boolean".equals(fieldType)){
            lenght = -1.0;
            //目前不单独处理booblean
        }else{
            resultMap.put("validate",false);
            resultMap.put("message",label + "不支持复杂数据类型校验<br/>");
            return resultMap;
        }

        if(rule.containsKey("min") && lenght != -1.0){
            Double min = rule.getDouble("min");
            if(formItemValue == null || lenght < min){
                resultMap.put("validate",false);
                resultMap.put("message",label + "不能小于" + min + "<br/>");
            }
        }
        if(rule.containsKey("max") && lenght != -1.0){
            Double max = rule.getDouble("max");
            if(formItemValue == null || lenght > max){
                resultMap.put("validate",false);
                resultMap.put("message",label + "不能大于" + max + "<br/>");
            }
        }

        String type = rule.getString("type");
        String fieldValue = "";
        if(formItemValue != null){
            fieldValue = String.valueOf(formItemValue);
        }
        if("required".equals(type) && StringUtils.isEmpty(fieldValue)){
            resultMap.put("validate",false);
            resultMap.put("message",label + "不能为空" + "<br/>");
        }else if("email".equals(type)){
            if(!checkEmail(fieldValue)){
                resultMap.put("validate",false);
                resultMap.put("message",label + "格式不正确" + "<br/>");
            }
        }else if("phone".equals(type)){
            Map<String,Object> result = validPhoneNum("0",fieldValue);
            if((boolean)result.get("flag") == false){
                resultMap.put("validate",false);
                resultMap.put("message",label + result.get("msg") + "<br/>");
            }
        }else if("mobile".equals(type)){
            Map<String,Object> result = validPhoneNum("1",fieldValue);
            if((boolean)result.get("flag") == false){
                resultMap.put("validate",false);
                resultMap.put("message",label + result.get("msg") + "<br/>");
            }
        }else if("idNo".equals(type)){
            Map<String,Object> result = IDCardValidate(fieldValue);
            if((boolean)result.get("flag") == false){
                resultMap.put("validate",false);
                resultMap.put("message",label + result.get("msg") + "<br/>");
            }
        }
        return resultMap;
    }

    /**
     *校验邮箱格式
     */
    public boolean checkEmail(String value){
        boolean flag=false;
        String pMail = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern patternEmail = Pattern.compile(pMail);
        Matcher m = null;
        m = patternEmail.matcher(value);
        flag = m.matches();
        return flag;
    }
    /**
     * @param checkType 校验类型：0校验手机号码，1校验座机号码，2两者都校验满足其一就可
     * @param phoneNum
     * */
    public Map<String,Object> validPhoneNum(String checkType,String phoneNum){
        Map<String,Object> result = new HashMap<String,Object>();
        Matcher m = null;
        String pStr1 = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\\d{8})?$";
        String pStr2 = "^(0[0-9]{2,3}\\-)?([1-9][0-9]{6,7})$";
        Pattern p1 = Pattern.compile(pStr1);
        Pattern p2 = Pattern.compile(pStr2);
        if("0".equals(checkType)){
            System.out.println(phoneNum.length());
            if(phoneNum.length()!=11){
                result.put("flag",false);
                result.put("msg","手机号应为11位数");
            }else{
                m = p1.matcher(phoneNum);
                if(m.matches()){
                    result.put("flag",true);
                }else{
                    result.put("flag",false);
                    result.put("msg","请填入正确的手机号");
                }
            }
        }else if("1".equals(checkType)){
            if(phoneNum.length()<11||phoneNum.length()>=16){
                result.put("flag",false);
                result.put("msg","请填入正确的电话号");
            }else{
                m = p2.matcher(phoneNum);
                if(m.matches()){
                    result.put("flag",true);
                }else{
                    result.put("flag",false);
                    result.put("msg","请填入正确的电话号");
                }
            }
        }else if("2".equals(checkType)){
            if(!((phoneNum.length() == 11 && p1.matcher(phoneNum).matches())||(phoneNum.length()<16&&p2.matcher(phoneNum).matches()))){
                result.put("flag",false);
                result.put("msg","请填入正确的电话号");
            }else{
                result.put("flag",true);
            }
        }else{
            result.put("flag",false);
            result.put("msg","参数错误");
        }
        return result;
    }
    /**
     * 功能：身份证的有效验证
     */
    public Map<String,Object> IDCardValidate(String IDStr) throws ParseException {
        Map<String,Object> result = new HashMap<String,Object>();
        IDStr = IDStr.trim().toUpperCase();
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            result.put("flag",false);
            result.put("msg","身份证号码长度应该为15位或18位");
            return result;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            result.put("flag",false);
            result.put("msg","身份证号码格式不正确");
            return result;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            //身份证生日无效。
            result.put("flag",false);
            result.put("msg","身份证号码格式不正确");
            return result;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            //身份证生日不在有效范围。
            result.put("flag",false);
            result.put("msg","身份证生日不在有效范围。");
            return result;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            //身份证月份无效
            result.put("flag",false);
            result.put("msg","身份证月份无效");
            return result;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            //身份证日期无效
            result.put("flag",false);
            result.put("msg","身份证日期无效");
            return result;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            //身份证地区编码错误。
            result.put("flag",false);
            result.put("msg","身份证地区编码错误");
            return result;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                //身份证无效，不是合法的身份证号码
                result.put("flag",false);
                result.put("msg","身份证无效，不是合法的身份证号码");
                return result;
            }
        } else {
            result.put("flag",true);
            return result;
        }
        // =====================(end)=====================
        result.put("flag",true);
        return result;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     */
    public boolean isDataFormat(String str) {
        boolean flag = false;
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 功能：判断字符串是否为数字
     */
    private boolean isNumeric(String str) {
        String pStr= "[0-9]*";
        Pattern pattern = Pattern.compile(pStr);
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：设置地区编码
     */
    private Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 根据属性名获取属性值
     * */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     * */
    private String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            System.out.println(fields[i].getType());
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    private String getFiledType(String fieldName,Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String type = "";
        for(int i=0;i<fields.length;i++){
            if(fields[i].getName().equals(fieldName)){
                type = fields[i].getType().toString();
            }
        }
        return type;
    }

    /**
     * 获取属性类型
     * */
    private List getFiledsInfo(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        List list = new ArrayList();
        Map infoMap=null;
        for(int i=0;i<fields.length;i++){
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     * */
    public Object[] getFiledValues(Object o){
        String[] fieldNames=getFiledName(o);
        Object[] value=new Object[fieldNames.length];
        for(int i=0;i<fieldNames.length;i++){
            value[i]=getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }
}
