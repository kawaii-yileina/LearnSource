package com.elaina.MySpring.bean;

import com.elaina.MySpringSource.annotation.Autowired;
import com.elaina.MySpringSource.annotation.Component;
import com.elaina.MySpringSource.annotation.Mapper;
import com.elaina.MySpringSource.annotation.Service;

/**
 * ClassName: Dog
 * Package: com.elaina.MySpring.bean
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/6 22:50
 */
@Component("myDog")
public class Dog {
    @Autowired(value = "dogServiceImpl")
    private DogService dogService;

    public DogService getDogService() {
        return dogService;
    }

    public void setDogService(DogService dogService) {
        this.dogService = dogService;
    }

    private String name;

    public String getName() {
        return name;
    }

    public Dog() {
        this.name = "namelessDog";
        System.out.println("dog创建完成");
    }

    public Dog(String name) {
        System.out.println("dog:" + name + "创建完成");
        this.name = name;
    }

    public void wangwang(Class<?> type) {
        System.out.println(name + "在"+type.getSimpleName()+"对象中狗叫");
        dogService.playBall();
        System.out.println("dogService:" + dogService);
        System.out.println("--------");
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
