/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.swagger.params;

import cn.mftcc.common.logger.MFLogger;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.util.Map;

@Component
@Order
public class MapApiReader extends ClassLoader implements ParameterBuilderPlugin {

    @Override
    public void apply(ParameterContext parameterContext) {
        Map<String, Object> maps = SwaggerMapContext.getMap();
        if(maps == null || maps.size() == 0){
            return;
        }
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        OperationContext operationContext = parameterContext.getOperationContext();
        String requestMappingPatternName = operationContext.requestMappingPattern();
        Optional<String> parameterNameOptional = methodParameter.defaultName();
        String parameterName = parameterNameOptional.get();

        if (methodParameter.getParameterType().canCreateSubtype(Map.class) || methodParameter.getParameterType().canCreateSubtype(String.class)) {
            String name = SwaggerASMUtil.returnClassName(requestMappingPatternName,parameterName);
            ApiJsonProperty[] properties = (ApiJsonProperty[]) maps.get(requestMappingPatternName);
            if(properties == null){
                return;
            }
            byte[] cs = SwaggerASMUtil.createRefModel(properties,name);
            Class hw = this.defineClass(name, cs, 0, cs.length);
            TypeResolver typeResolver = new TypeResolver();
            parameterContext.getDocumentationContext().getAdditionalModels().add(typeResolver.resolve(hw));
            parameterContext.parameterBuilder().parameterType("body").modelRef(new ModelRef(name)).name("入参数据");
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
