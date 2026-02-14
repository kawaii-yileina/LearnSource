package com.elaina.MySpringSource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Autowired
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/6 22:00
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    String value() default "";
    boolean required() default true;
}
