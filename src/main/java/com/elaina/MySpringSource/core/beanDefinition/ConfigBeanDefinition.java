package com.elaina.MySpringSource.core.beanDefinition;

import java.lang.reflect.Method;

/**
 * ClassName: ConfigBeanDefinition
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/11 15:25
 */
@SuppressWarnings("all")
public class ConfigBeanDefinition extends BeanDefinition {
    private BeanDefinition configDefinition;

    private Method beanMethod;

    public Method getBeanMethod() {
        return beanMethod;
    }

    public BeanDefinition getConfigDefinition() {
        return configDefinition;
    }

    public ConfigBeanDefinition(Class<?> beanType, String name, BeanDefinition configDefinition, Method beanMethod) {
        super(beanType, name);
        this.configDefinition = configDefinition;
        this.beanMethod = beanMethod;
    }
}
