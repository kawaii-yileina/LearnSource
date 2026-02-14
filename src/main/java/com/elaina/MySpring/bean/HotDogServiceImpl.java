package com.elaina.MySpring.bean;

import com.elaina.MySpringSource.annotation.Component;

/**
 * ClassName: HotDogServiceImpl
 * Package: com.elaina.MySpring.bean
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/8 22:05
 */
@Component("hotDogService")
public class HotDogServiceImpl implements DogService{

    @Override
    public void playBall() {
        System.out.println("hotDog");
    }

    @Override
    public void setDog(Dog dog) {

    }

}
