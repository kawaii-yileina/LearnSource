package com.elaina.MySpringSource;

import com.elaina.MySpringSource.annotation.Component;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * ClassName: AnnotationUtil
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/11 15:50
 */
@SuppressWarnings("all")
public class AnnotationUtil {
    public static boolean isAnnotationPresentComponent(Class<?> type) {
        return isAnnotationPresent(type, Component.class);
    }

    public static boolean isAnnotationPresent(Class<?> type, Class<? extends Annotation> annotationType) {
        if (Objects.isNull(type)) {
            return false;
        }
        if (type.isAnnotationPresent(annotationType) || type == annotationType) {
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

    public static boolean isAnnotationPresent(Field field, Class<? extends Annotation> annotationType) {
        if (Objects.isNull(field)) {
            return false;
        }
        if (field.isAnnotationPresent(annotationType)) {
            return true;
        }
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (annotation.annotationType() == Deprecated.class || annotation.annotationType() == Target.class || annotation.annotationType() == Retention.class || annotation.annotationType() == Documented.class) {
                continue;
            }
            if (isAnnotationPresent(annotation.annotationType(), annotationType)) {
                return true;
            }
        }
        return false;
    }

    public static Component getComponent(Class<?> beanType) {
        return getAnnotation(beanType, Component.class);
    }

    public static <T> T getAnnotation(Class<?> beanType, Class<? extends Annotation> annotationType) {
        if (Objects.isNull(beanType) || Objects.isNull(annotationType)) {
            return null;
        }
        Annotation[] annotations = beanType.getDeclaredAnnotations();
        T result = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Deprecated.class || annotation.annotationType() == Target.class || annotation.annotationType() == Retention.class || annotation.annotationType() == Documented.class) {
                continue;
            }
//            String annotationValue = "";
            if (annotation.annotationType() == annotationType) {
                return (T) annotation;
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
            result = getAnnotation(annotation.annotationType(), annotationType);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static String getComponentValue(Class<?> beanType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getAnnotationValue(beanType, Component.class, String.class);
    }

    public static Object getAnnotationValue(Class<?> beanType, Class<? extends Annotation> annotationType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Annotation annotation : beanType.getDeclaredAnnotations()) {
            if (isAnnotationPresent(annotation.annotationType(), annotationType)) {
                Method value = annotation.annotationType().getDeclaredMethod("value");
                return value.invoke(annotation);
            }
        }
        return null;
    }

    public static <T> T getAnnotationValue(Class<?> beanType, Class<? extends Annotation> annotationType, Class<T> returnType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (T) getAnnotationValue(beanType, annotationType);
    }


    public static <T> T getMethodAnnotationValue(Method method, Class<? extends Annotation> annotationType, Class<T> returnType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotationType == annotation.annotationType()) {
                Method value = annotation.annotationType().getDeclaredMethod("value");
                return (T) value.invoke(annotation);
            }
        }
        return null;
    }

    public static Object getAnnotationValue(Field field, Class<? extends Annotation> annotationType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        field.setAccessible(true);
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (isAnnotationPresent(annotation.annotationType(), annotationType)) {
                Method value = annotation.annotationType().getDeclaredMethod("value");
                return value.invoke(annotation);
            }
        }
        return null;
    }

    public static Object getAnnotationTargetValue(Annotation annotation, String targetName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (annotation == null) {
            throw new RuntimeException("annotation为空");
        }
        Method method = annotation.annotationType().getDeclaredMethod(targetName);
        method.setAccessible(true);
        return method.invoke(annotation);
    }

    public static <T> T getAnnotationTargetValue(Annotation annotation, String targetName, Class<T> returnType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (annotation == null) {
            throw new RuntimeException("annotation为空");
        }
        Method method = annotation.annotationType().getDeclaredMethod(targetName);
        method.setAccessible(true);
        return (T) method.invoke(annotation);
    }

    public static Annotation getAnnotationPresent(Field field, Class<? extends Annotation> annotationType) {
        field.setAccessible(true);
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (isAnnotationPresent(annotation.annotationType(), annotationType)) {
                return annotation;
            }
        }
        return null;
    }
}
