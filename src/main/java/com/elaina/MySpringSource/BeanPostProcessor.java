package com.elaina.MySpringSource;

/**
 * ClassName: BeanPostProcessor
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/8 20:43
 */
public interface BeanPostProcessor {
    default Object beforeInitializeBean(Object bean, String beanName) {
        return bean;
    }
    default Object afterInitializeBean(Object bean, String beanName) {
        return bean;
    }
}
