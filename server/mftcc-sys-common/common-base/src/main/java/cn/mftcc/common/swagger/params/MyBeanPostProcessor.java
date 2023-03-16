/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.swagger.params;

import cn.mftcc.common.logger.MFLogger;
import cn.mftcc.common.swagger.ret.ApiReturn;
import cn.mftcc.common.swagger.ret.ApiReturnJson;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Configuration
public class MyBeanPostProcessor implements BeanPostProcessor {

    public static final String filterPackage = "feign.controller";

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            Class clazz =  bean.getClass();
            Package packageStr = clazz.getPackage();
            String packAgeName = packageStr.getName();
            if(!packAgeName.contains(filterPackage)){
                return bean;
            }
            if(clazz.getAnnotation(RestController.class) == null && clazz.getAnnotation(Controller.class) == null){
                return bean;
            }
            RequestMapping controllerRequestMapping;
            if(clazz.getInterfaces().length==0){
                controllerRequestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            }else{
                controllerRequestMapping = (RequestMapping) clazz.getInterfaces()[0].getAnnotation(RequestMapping.class);
            }
            String classRequestUrl = "";
            if(controllerRequestMapping!=null){
                classRequestUrl =  Arrays.toString(controllerRequestMapping.value());
            }

            List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
//            List<Method> methodsInterface = Arrays.asList(clazz.getInterfaces()[0].getDeclaredMethods());
//            for(int i=0;i<methods.size();i++){
//                Method method = methods.get(i);
//                String methodRequest = getRequestUrl(method);
//                if("".equals(methodRequest)){
//
//                }
//            }
            Iterator<Method> iterator = methods.iterator();
            while(iterator.hasNext()){
                Method method = iterator.next();
                String methodRequest = getRequestUrl(method);
                if("".equals(methodRequest)){
                    Method pMethod = clazz.getInterfaces()[0].getDeclaredMethod(method.getName(),method.getParameterTypes());
                    methodRequest = getRequestUrl(pMethod);
                }
                String key = classRequestUrl + methodRequest ;
                key = key.replaceAll("\\[","").replaceAll("\\]","");
                //param map
                ApiJsonObject annotation = method.getAnnotation(ApiJsonObject.class);
                if(annotation != null){
                    ApiJsonProperty[] values = annotation.value();
                    SwaggerMapContext.getMap().put(key,values);
                }
                //return map
                ApiReturn annotation2 = method.getAnnotation(ApiReturn.class);
                if(annotation2 != null){
                    ApiReturnJson[] values = annotation2.value();
                    SwaggerRetMapContext.getMap().put(key,values);
                }
            }
        }catch (Exception e){
            MFLogger.error(e);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 这边只做简单打印   原样返回bean
//        if(AutoConfigurationPackages.class.getName().equals(beanName)){
//            System.out.println("postProcessBeforeInitialization===="+beanName);
//        }
        return bean;
    }

    private String getRequestUrl(Method method){
        String methodRequest = "";
        if(method.getAnnotation(RequestMapping.class) != null) {
            methodRequest = Arrays.toString(method.getAnnotation(RequestMapping.class).value());
        }
        if(method.getAnnotation(PutMapping.class) != null) {
            methodRequest = Arrays.toString(method.getAnnotation(PutMapping.class).value());
        }
        if(method.getAnnotation(DeleteMapping.class) != null) {
            methodRequest = Arrays.toString(method.getAnnotation(DeleteMapping.class).value());
        }
        if(method.getAnnotation(GetMapping.class) != null) {
            methodRequest = Arrays.toString(method.getAnnotation(GetMapping.class).value());
        }
        if(method.getAnnotation(PatchMapping.class) != null) {
            methodRequest = Arrays.toString(method.getAnnotation(PatchMapping.class).value());
        }
        if(method.getAnnotation(PostMapping.class) != null) {
            methodRequest = Arrays.toString(method.getAnnotation(PostMapping.class).value());
        }
        return methodRequest;
    }
}
