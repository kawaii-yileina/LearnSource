package com.elaina.MySpring.bean;

import com.elaina.MySpringSource.core.annotation.Mapper;

/**
 * ClassName: CatMapper
 * Package: com.elaina.MySpring.bean
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/10 19:28
 */
@Mapper
public class CatMapper {
    public void miao(Class<? extends Cat> cat) {
        System.out.println(cat.getSimpleName() + "喵喵叫");
    }
}
