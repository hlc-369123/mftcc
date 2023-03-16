/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONTokener;

/**
 * @ClassName MFLogger
 * @Description
 * @Author 郭涵晨
 * @Date 2020/4/3 10:31
 */
public class MFLogger {
    // 当前日志类名
    private final static String logClassName = MFLogger.class.getName();

    /**
     * 获取最原始被调用的堆栈信息
     */
    private static StackTraceElement getCaller() {

        // 获取堆栈信息
        StackTraceElement[] traceElements = Thread.currentThread()
                .getStackTrace();
        if (null == traceElements) {
            return null;
        }

        // 最原始被调用的堆栈信息
        StackTraceElement caller = null;

        // 循环遍历到日志类标识
        boolean isEachLogFlag = false;

        // 遍历堆栈信息，获取出最原始被调用的方法信息
        // 当前日志类的堆栈信息完了就是调用该日志类对象信息
        for (StackTraceElement element : traceElements) {
            // 遍历到日志类
            if (element.getClassName().equals(logClassName)) {
                isEachLogFlag = true;
            }

            // 下一个非日志类的堆栈，就是最原始被调用的方法
            if (isEachLogFlag) {
                if (!element.getClassName().equals(logClassName)) {
                    caller = element;
                    break;
                }
            }
        }

        return caller;
    }

    /**
     * 自动匹配请求类名，生成logger对象
     */
    private static Logger log() {
        // 最原始被调用的堆栈对象
        StackTraceElement caller = getCaller();
        // 空堆栈处理
        if (caller == null) {
            return LoggerFactory.getLogger(Logger.class);
        }

        // 取出被调用对象的类名，并构造一个Logger对象返回
        return LoggerFactory.getLogger(caller.getClassName());
    }

    private static void removeMDC(){
        MDC.remove("traceNo");
    }
    /**
     * 封装的方法
     */
    public static void info(String traceNo, String message){
        message = encryptionStr(message);
        MDC.put("traceNo", traceNo);
        log().info(message);
        removeMDC();
    };
    /*public static void info(String traceNo, String message, Throwable throwable){
        message = encryptionStr(message);
        MDC.put("traceNo", traceNo);
        log().info(message,throwable);
        removeMDC();
    };*/
    /*public static void info(String traceNo, Throwable throwable){
        MDC.put("traceNo", traceNo);
        log().info(throwable.getMessage(),throwable);
        removeMDC();
    };*/
    public static void info(String message){
        log().info(message);
        removeMDC();
    };
    /*public static void info(Throwable throwable){
        log().info(throwable.getMessage(),throwable);
        removeMDC();
    };*/
    public static void warn(String traceNo, String message){
        message = encryptionStr(message);
        MDC.put("traceNo", traceNo);
        log().warn(message);
        removeMDC();
    };
    /*public static void warn(String traceNo, String message, Throwable throwable){
        message = encryptionStr(message);
        MDC.put("traceNo", traceNo);
        log().warn(message,throwable);
        removeMDC();
    };*/
    /*public static void warn(String traceNo, Throwable throwable){
        MDC.put("traceNo", traceNo);
        log().warn(throwable.getMessage(),throwable);
        removeMDC();
    };*/
    public static void warn(String message){
        log().warn(message);
        removeMDC();
    };
    /*public static void warn(Throwable throwable){
        log().warn(throwable.getMessage(),throwable);
        removeMDC();
    };*/
    public static void debug(String traceNo, String message){
        message = encryptionStr(message);
        MDC.put("traceNo", traceNo);
        log().debug(message);
        removeMDC();
    };
    /*public static void debug(String traceNo, String message, Throwable throwable){
        message = encryptionStr(message);
        MDC.put("traceNo", traceNo);
        log().debug(message,throwable);
        removeMDC();
    };*/
    /*public static void debug(String traceNo, Throwable throwable){
        MDC.put("traceNo", traceNo);
        log().debug(throwable.getMessage(),throwable);
        removeMDC();
    };*/
    public static void debug(String message){
        log().debug(message);
        removeMDC();
    };
    /*public static void debug(Throwable throwable){
        log().debug(throwable.getMessage(),throwable);
        removeMDC();
    };*/
    /*public static void error(String traceNo, String message){
        message = encryptionStr(message);
        MDC.put("traceNo", traceNo);
        log().error(message);
        removeMDC();
    };*/
    public static void error(String traceNo, String message, Throwable throwable){
        message = encryptionStr(message);
        MDC.put("traceNo", traceNo);
        log().error(message,throwable);
        removeMDC();
    };
    public static void error(String message, Throwable throwable){
        log().error(message,throwable);
        removeMDC();
    };
    /*public static void error(String message){
        log().error(message);
        removeMDC();
    };*/
    public static void error(Throwable throwable){
        log().error(throwable.getMessage(),throwable);
        removeMDC();
    };


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

    private static JSONObject encryption(JSONObject jsonObject) throws JSONException {
        String encriptString = "cusName,cusTel,idNum,accountNo,address,commAddress,contactsName,contactsTel,cusAddr,expressTel,monthTel,regHomeAddre,reservedPhone,tel,productName";
        String[] encripts = encriptString.split(",");
        for (int i = 0; i < encripts.length; i++) {
            if(jsonObject.has(encripts[i])){
                String value = jsonObject.getString("tel");
                value = DesensitizeUtil.around(value,3,3);
                jsonObject.put(encripts[i],value);
            }

        }

        return jsonObject;
    }


}
