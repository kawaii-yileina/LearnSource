package com.elaina.MySpringSource.annotation;

import com.elaina.MySpringSource.annotation.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Service
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/9 22:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Service {
    String value() default "";
}
