/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.exception;

import cn.mftcc.common.logger.MFLogger;
import cn.mftcc.common.sysutils.MsgCacheUtil;
import cn.mftcc.common.utils.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> result(HttpServletRequest request, HttpServletResponse response, SecurityException e){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", e.getCode() == null?"401":e.getCode());
        jsonObject.put("message",e.getMessage());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataSourceException.class)
    public ResponseEntity<String> result(DataSourceException e){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","901");
        jsonObject.put("message",e.getMessage());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> result(ServiceException e){
        JSONObject jsonObject = new JSONObject();
        String msgCode = e.getCode();
        String msgStr = e.getMessage();
        Map<String,String> msgCnt = e.getMsgCnt();
        if(msgCode == null){
            jsonObject.put("code","600");
            jsonObject.put("message",msgStr);
            jsonObject.put("msgType","error");
            jsonObject.put("msgLvl","1");
        }else{
            MsgCacheUtil msgCacheUtil = (MsgCacheUtil) SpringUtil.getBean("msgCacheUtil");
            JSONObject sysMsgConfigEntity = msgCacheUtil.getMsg(msgCode);
            jsonObject.put("code", "600");

            if(sysMsgConfigEntity != null){
                msgStr = sysMsgConfigEntity.getString("msgCnt");
                String regex="\\{(.*?)\\}";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(msgStr);
                while(m.find()){
                    if(msgCnt.containsKey(m.group(1))){
                        msgStr = msgStr.replace(m.group(),msgCnt.get(m.group(1)));
                    }
                }
                jsonObject.put("msgLvl", sysMsgConfigEntity.getString("sgLvl"));
                jsonObject.put("msgType", sysMsgConfigEntity.getString("msgType"));
                jsonObject.put("message", msgStr);

            }else{
                jsonObject.put("message",msgStr);
                jsonObject.put("msgType","error");
                jsonObject.put("msgLvl","1");
            }
        }
        MFLogger.error(e.getTraceNo()+"",msgStr,e);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}