/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.exception;


public class DataSourceException extends RuntimeException{

    public DataSourceException(){
        super("DataSource Error");
    }

    public DataSourceException(String message){
        super(message);
    }
}
