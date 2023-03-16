/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.exception;

import lombok.Data;

import java.util.Map;

@Data
public class ServiceException extends RuntimeException{

    private String code;
    private Object traceNo;
    private static String message = "系统异常";

    private Map<String,String> msgCnt;

    public ServiceException(String code, Object traceNo,String message){
        super(message);
        this.code = code;
        this.traceNo = traceNo;

    }


    public ServiceException(String code, Object traceNo, Exception e){
        super(e);
        this.code = code;
        this.traceNo = traceNo;

    }

    public ServiceException(String code, Object traceNo, Exception e, Map<String,String> msgCnt){
        super(e);
        this.code = code;
        this.traceNo = traceNo;
        this.msgCnt = msgCnt;

    }

}
