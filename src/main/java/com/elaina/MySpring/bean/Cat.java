package com.elaina.MySpring.bean;

import com.elaina.MySpringSource.annotation.Autowired;
import com.elaina.MySpringSource.annotation.Component;
import com.elaina.MySpringSource.annotation.PostConstruct;

/**
 * ClassName: Cat
 * Package: com.elaina.MySpring.bean
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/4 22:11
 */
@Component("cat")
public class Cat {
    public Cat() {
        System.out.println("cat已经被创建");
    }

    @Autowired
    private Dog dog;

    @Autowired("hotDogService")
    private DogService dogService;

    @Autowired
    private CatMapper catMapper;

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }


    @PostConstruct
    public void task() {
        System.out.println("cat初始化任务完成");
        dog.wangwang(Cat.class);
        dogService.playBall();
    }

    @PostConstruct
    private void task2() {
        System.out.println("task2执行完成");
    }
}
