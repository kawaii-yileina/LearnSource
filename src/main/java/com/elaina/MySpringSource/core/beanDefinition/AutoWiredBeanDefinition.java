package com.elaina.MySpringSource.core.beanDefinition;

import com.elaina.MySpringSource.utils.AnnotationUtil;
import com.elaina.MySpringSource.core.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * ClassName: AutoWiredBeanDefinition
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/8 21:38
 */
public class AutoWiredBeanDefinition extends AbstractBeanDefinition {
    private final Field field;
    private final boolean required;


    public Field getField() {
        return field;
    }

    public boolean isRequired() {
        return required;
    }

    private AutoWiredBeanDefinition(Field field) {
//        field.setAccessible(true);
//        Autowired autowired = field.getDeclaredAnnotation(Autowired.class);
//        this.name = autowired.value().isEmpty() ? "" : autowired.value();
        Annotation annotation = AnnotationUtil.getAnnotationPresent(field, Autowired.class);
        try {
            this.name = AnnotationUtil.getAnnotationTargetValue(annotation, "value", String.class);
            this.required = AnnotationUtil.getAnnotationTargetValue(annotation, "required", Boolean.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        beanType = field.getType();
        this.field = field;
    }

    public static AutoWiredBeanDefinition build(Field field) {
        return new AutoWiredBeanDefinition(field);
    }
}
