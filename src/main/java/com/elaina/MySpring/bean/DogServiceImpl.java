package com.elaina.MySpring.bean;

import com.elaina.MySpringSource.core.annotation.Autowired;
import com.elaina.MySpringSource.core.annotation.Service;

/**
 * ClassName: DogServiceImpl
 * Package: com.elaina.MySpring.bean
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/7 23:34
 */
@Service("dogServiceImpl")
public class DogServiceImpl implements DogService{

    @Autowired
    //todo 没注入成功
    private Dog dog;

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public void playBall() {
        System.out.println(dog + " playing ball");
    }
}
