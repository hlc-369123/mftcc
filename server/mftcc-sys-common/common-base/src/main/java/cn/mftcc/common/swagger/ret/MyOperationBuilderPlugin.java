/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.swagger.ret;

import cn.mftcc.common.swagger.params.SwaggerASMUtil;
import cn.mftcc.common.swagger.params.SwaggerRetMapContext;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import jdk.internal.org.objectweb.asm.ClassWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 3)//一定要大一点   //plugin加载顺序，默认是最后加载
public class MyOperationBuilderPlugin extends ClassLoader implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext operationContext) {
        TypeResolver typeResolver = new TypeResolver();
        Map<String, Object> maps = SwaggerRetMapContext.getMap();
        if(maps == null || maps.size() == 0){
            return;
        }
        String requestMappingPatternName = operationContext.requestMappingPattern();

//        String[] strArr = requestMappingPatternName.split("/");
//        String mappingName = "";
//        for(int i=0;i<strArr.length;i++){
//            if(i>1){
//                mappingName += "/"+strArr[i];
//            }
//        }

        //if(operationContext.getReturnType().isInstanceOf(Map.class)) {
        if(maps.get(requestMappingPatternName) != null){
            //根据参数上的ApiJsonObject注解中的参数动态生成Class
            Optional<ApiReturn> optional = operationContext.findAnnotation(ApiReturn.class);
            try {
//                Method method = Swagger2.class.getMethod("restApi");//系统默认取该处的全局变量
//                ApiReturnJson apiReturnJson = method.getAnnotation(ApiReturnJson.class);
//                String name = apiReturnJson.key()+"_"+operationContext.getName();
//                if (optional.isPresent())
//                    name = optional.get().key();  //model 名称

                ApiReturnJson[] properties = (ApiReturnJson[]) maps.get(requestMappingPatternName);
                if(properties == null){
                    return;
                }
//                String name = operationContext.getName() + requestMappingPatternName.replaceAll("/","_");
                String name = operationContext.getName();

                for (ApiReturnJson property : properties){
                    ApiReturnData[] apiReturnDatas = property.data();
                    if(apiReturnDatas.length>1||!"".equals(apiReturnDatas[0].key())){
                        String key = property.key();
                        String dataName = name+key;
//                        String dataName = className;
                        ClassWriter cwd = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        //创建类
                        SwaggerASMUtil.createClazz(cwd,dataName);

                        //创建构造方法
                        SwaggerASMUtil.createConstructor(cwd);

                        //循环处理 getter 和 setter 方法 创建字段和注解
                        SwaggerASMUtil.doParseFieldAndMethod(cwd,apiReturnDatas,dataName);

                        cwd.visitEnd();

                        byte[] code = cwd.toByteArray();
                        Class hwDataClass = this.defineClass(dataName, code, 0, code.length);
                        ResolvedType rt = typeResolver.resolve(hwDataClass);
                        // 像documentContext的Models中添加我们新生成的Class
                        operationContext.getDocumentationContext().getAdditionalModels().add(rt);
                    }
                }

                byte[] cs = SwaggerASMUtil.createRefModel(properties, name);
                Class hw = this.defineClass(name, cs, 0, cs.length);
                ResolvedType rt = typeResolver.resolve(hw);
                // 像documentContext的Models中添加我们新生成的Class
                operationContext.getDocumentationContext().getAdditionalModels().add(rt);
                //




                Set<ResponseMessage> set = new HashSet<ResponseMessage>();
                ModelRef mr = new ModelRef(name);
                set.add(new ResponseMessage(200,
                        "{\"code\":\"0\",\"msg\":\"success\",\"data\":{}}",
                        mr,null,null));
                operationContext.operationBuilder().responseMessages(set);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
