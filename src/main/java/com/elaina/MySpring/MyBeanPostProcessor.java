package com.elaina.MySpring;

import com.elaina.MySpringSource.BeanPostProcessor;
import com.elaina.MySpringSource.annotation.Component;

/**
 * ClassName: MyBeanPostProcessor
 * Package: com.elaina.MySpring
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/8 21:15
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        System.out.println(beanName + "在初始化完成结束之后执行");
        return BeanPostProcessor.super.afterInitializeBean(bean, beanName);
    }
}
