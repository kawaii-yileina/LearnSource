package com.elaina.MySpringSource.core.beanDefinition;

/**
 * ClassName: AbstractBeanDefinition
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/11 15:26
 */
public abstract class AbstractBeanDefinition {
    protected String name;
    protected Class<?> beanType;

    public String getName() {
        return name;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
}
