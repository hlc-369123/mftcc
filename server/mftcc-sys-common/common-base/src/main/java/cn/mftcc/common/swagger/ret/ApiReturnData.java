/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.swagger.ret;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: ApiReturnJsonProData
 * @Description: 每一个字段的定义备注说明 (描述这个类的作用)
 * @author
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiReturnData {

    String key() default "";//key

    String example() default "";

    Class<?> dataType() default String.class;

    String description() default "";

}
