/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.exception;

import lombok.Data;

@Data
public class SecurityException extends RuntimeException{

    private Integer code;
    private static String msg = "登录超时";

    public SecurityException(){
        super(msg);
    }

    public SecurityException(String message){
        super(message);
    }

    public SecurityException(int code){
        super(msg);
        this.code = code;
    }

    public SecurityException(int code,String message){
        super(message);
        this.code = code;
    }
}
