/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import net.logstash.logback.marker.LogstashBasicMarker;
import org.slf4j.Marker;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONTokener;

public class LoggerMaskFilter extends TurboFilter {

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable throwable) {
        if(level.isGreaterOrEqual(Level.INFO)){
            if(marker!=null &&marker.contains("isMasked")){
                return FilterReply.NEUTRAL;
            }
            if(marker==null){
                marker = new LogstashBasicMarker("isMasked");
            }else{
                marker.add(new LogstashBasicMarker("isMasked"));
            }
            if(params!=null){
                for (int i = 0; i < params.length; i++) {
                    Object o = params[i];
                    if(o instanceof String) {
                        params[i] = encryptionStr((String) o);
                    }
                }
            }
            logger.log(marker,logger.getClass().getName(),Level.toLocationAwareLoggerInteger(level), format, params,throwable);
            return FilterReply.DENY;
        }else{
            return FilterReply.NEUTRAL;

        }
    }

    private static JSONObject encryption(JSONObject jsonObject) throws JSONException {
        String encriptString = "cusName,cusTel,idNum,accountNo,address,commAddress,contactsName,contactsTel,cusAddr,expressTel,monthTel,regHomeAddre,reservedPhone,tel";
        String[] encripts = encriptString.split(",");

        String encriptTypeString = "USER_NAME,MOBILE,CERT_NO,CARD_NO,ADDRESS,ADDRESS,USER_NAME,MOBILE,ADDRESS,MOBILE,MOBILE,ADDRESS,MOBILE,MOBILE";
        String[] encriptTypes = encriptTypeString.split(",");
        for (int i = 0; i < encripts.length; i++) {
            if(jsonObject.has(encripts[i])){
                String value = jsonObject.getString(encripts[i]);
                if("USER_NAME".equals(encriptTypes[i])){
                    value = DesensitizeUtil.left(value,1);
                }else {
                    value = DesensitizeUtil.around(value,3,3);
                }
                jsonObject.put(encripts[i],value);
            }

        }

        return jsonObject;
    }

    private static String encryptionStr(String message){
        try{
            Object json = new JSONTokener(message).nextValue();
            if(json instanceof JSONObject){
                JSONObject jsonObject = (JSONObject)json;
                return encryption(jsonObject).toString();
            }else if (json instanceof JSONArray){
                JSONArray jsonArray = (JSONArray)json;
                JSONArray result = new JSONArray();
                for(int i = 0; i < jsonArray.length(); i++){
                    result.put(encryption(jsonArray.getJSONObject(i)));
                }
                return result.toString();
            }else{
                return message;
            }
        }catch (Exception e){
            return message;
        }
    }
}