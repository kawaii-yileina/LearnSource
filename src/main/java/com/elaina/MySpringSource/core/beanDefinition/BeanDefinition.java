package com.elaina.MySpringSource.core.beanDefinition;

import com.elaina.MySpringSource.utils.AnnotationUtil;
import com.elaina.MySpringSource.core.annotation.Autowired;
import com.elaina.MySpringSource.core.annotation.Component;
import com.elaina.MySpringSource.core.annotation.PostConstruct;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * ClassName: BeanDefinition
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/4 21:45
 */
@SuppressWarnings("all")
public class BeanDefinition extends AbstractBeanDefinition {
    private final Constructor<?> constructor;
    private final List<Method> postConstructMethods;

    private final List<AutoWiredBeanDefinition> autowiredBeans;

    public List<AutoWiredBeanDefinition> getAutowiredBeans() {
        return autowiredBeans;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public List<Method> getPostConstructMethods() {
        return postConstructMethods;
    }

    public BeanDefinition(Class<?> type) {
        this.beanType = type;
        try {
            String name = AnnotationUtil.getComponentValue(type);
            this.name = "".equals(name) || name == null ? type.getSimpleName() : name;
            this.constructor = type.getConstructor();
            postConstructMethods = Arrays.stream(type.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(PostConstruct.class)).toList();
            autowiredBeans = Arrays.stream(type.getDeclaredFields()).filter(field -> AnnotationUtil.isAnnotationPresent(field, Autowired.class)).map(AutoWiredBeanDefinition::build).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public BeanDefinition(Class<?> type, String name) {
        this.beanType = type;
        try {
            this.name = "".equals(name) ? type.getSimpleName() : name;
            this.constructor = type.getConstructor();
            postConstructMethods = Arrays.stream(type.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(PostConstruct.class)).toList();
            autowiredBeans = Arrays.stream(type.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Autowired.class)).map(AutoWiredBeanDefinition::build).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public static boolean isAnnotationPresentComponent(Class<?> type) {
        return isAnnotationPresent(type, Component.class);
    }

    @Deprecated
    public static boolean isAnnotationPresent(Class<?> type, Class<? extends Annotation> annotationType) {
        if (Objects.isNull(type)) {
            return false;
        }
        if (type.isAnnotationPresent(annotationType)) {
            return true;
        }
        for (Annotation annotation : type.getDeclaredAnnotations()) {
            if (annotation.annotationType() == Deprecated.class || annotation.annotationType() == Target.class || annotation.annotationType() == Retention.class || annotation.annotationType() == Documented.class) {
                continue;
            }
            if (isAnnotationPresent(annotation.annotationType(), annotationType)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public static Component getComponent(Class<?> beanType) {
        if (Objects.isNull(beanType)) {
            return null;
        }
        Annotation[] annotations = beanType.getDeclaredAnnotations();
        Component component = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Deprecated.class || annotation.annotationType() == Target.class || annotation.annotationType() == Retention.class || annotation.annotationType() == Documented.class) {
                continue;
            }
//            String annotationValue = "";
            if (annotation.annotationType() == Component.class) {
                return (Component) annotation;
            }
//            try {
//                InvocationHandler handler = Proxy.getInvocationHandler(annotation);
//                Field field = handler.getClass().getDeclaredField("memberValues");
//                field.setAccessible(true);
//                Object o = field.get(handler);
//                Method value = annotation.annotationType().getDeclaredMethod("value");
//                String o = (String) value.invoke(annotation);
//                System.out.println(o);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            component = getComponent(annotation.annotationType());
            if (component != null) {
                return component;

            }
        }
        return component;
    }

    @Deprecated
    private String getComponentValue(Class<?> beanType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Annotation annotation : beanType.getDeclaredAnnotations()) {
            if (isAnnotationPresentComponent(annotation.annotationType())) {
                Method value = annotation.annotationType().getDeclaredMethod("value");
                return (String) value.invoke(annotation);
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "name='" + name + '\'' +
                ", beanType=" + beanType +
                '}';
    }
}
