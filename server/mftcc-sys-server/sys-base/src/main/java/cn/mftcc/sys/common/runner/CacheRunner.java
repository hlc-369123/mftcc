/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.common.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 这里通过设定value的值来指定执行顺序
 */
@Component
@Order(value = 1)
public class CacheRunner implements CommandLineRunner{

    @Autowired
    private CacheUtil cacheUtil;


    @Override
    public void run(String... strings) throws Exception{
        System.out.println(">>>>>>>>>>>>>>>加载数据字典<<<<<<<<<<<<<");
        try {
            cacheUtil.initParmCaChe();
            System.out.println(">>>>>>>>>>>>>>>加载数据字典成功<<<<<<<<<<<<<");
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>>>加载数据字典失败<<<<<<<<<<<<<");
        }
        System.out.println(">>>>>>>>>>>>>>>加载数据字典转换<<<<<<<<<<<<<");
        try {
            cacheUtil.initParmTransCaChe();
            System.out.println(">>>>>>>>>>>>>>>加载数据字典转换成功<<<<<<<<<<<<<");
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>>>加载数据字典转换失败<<<<<<<<<<<<<");
        }
        System.out.println(">>>>>>>>>>>>>>>加载错误码<<<<<<<<<<<<<");
        try {
            cacheUtil.initMsgCaChe();
            System.out.println(">>>>>>>>>>>>>>>加载错误码成功<<<<<<<<<<<<<");
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>>>加载错误码失败<<<<<<<<<<<<<");
        }

        System.out.println(">>>>>>>>>>>>>>>加载数据权限、鉴权url<<<<<<<<<<<<<");
        try {
            cacheUtil.initUrlAuth();
            System.out.println(">>>>>>>>>>>>>>>加载数据权限、鉴权url成功<<<<<<<<<<<<<");
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>>>加载数据权限、鉴权url失败<<<<<<<<<<<<<");
        }

    }
}
