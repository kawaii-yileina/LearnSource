package com.elaina.MySpring;

import com.elaina.MySpring.bean.*;
import com.elaina.MySpring.collections.MyLinkedList;
import com.elaina.MySpringSource.core.ApplicationContext;
import com.elaina.MySpringSource.core.annotation.Autowired;
import com.elaina.MySpringSource.core.annotation.Bean;
import com.elaina.MySpringSource.core.annotation.SpringApplication;
import com.elaina.MySpringSource.utils.StringUtil;

import java.util.*;
import java.util.stream.Stream;

/**
 * ClassName: Application
 * Package: com.elaina.MySpring
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.0
 * @create 2026/1/4 21:21
 */
@SpringApplication("com.elaina.MySpring")
public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext.run(Application.class);
        ApplicationContext applicationContext = ApplicationContext.getApplicationContext();
        List<DogService> beans = applicationContext.getBeans(DogService.class);
        System.out.println("beans = " + beans);
        List<Dog> dog = applicationContext.getBeans(Dog.class);
        System.out.println("dogs"+dog);
        System.out.println(applicationContext.getBean(Encoder.class).type);
        System.out.println(applicationContext.getBean("encoder"));
        System.out.println(StringUtil.firstToLowerCase("BabBaa"));
        System.out.println("#################################\n\n\n\n\n\n");
//        new LinkedList<>()
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.add((Integer) 1);
        list.add(2);
        list.add(3);
        list.add(5);
        list.add(3, 4);
//        list.add(2, 4);
//        list.remove(4, true);
//        list.clear();
        list.add(1);
        list.set(-1, 6);
        list.add(3);
        System.out.println("list.get(1) = " + list.get(0));
        System.out.println("list = " + list);
        Iterator<Integer> iterator = list.iterator();
        System.out.println("iterator.next() = " + iterator.next());
        System.out.println(Arrays.toString(list.toArray()));
        list.addAll(List.of(new Integer[]{8, 9}));
        list.addAll(List.of(new Integer[]{8, 9}));
//        list.removeAll(List.of(new Integer[]{8, 9}));
        list.retainAll(List.of(new Integer[]{8, 9}));
        System.out.println(Arrays.toString(list.toArray()));
        System.out.println("list = " + list);
//        System.out.println("list = " + list.stream().map(e -> e + 1).toList());
        list.removeIf(e -> e==8);
//        System.out.println(list.subList(1, -1));
        System.out.println(list);
        System.out.println();



//        System.out.println("dog = " + dog.get(0).getName());
//        System.out.println("dog = " + dog.get(1).getName());
//        String name = AnnotationUtil.getMethodAnnotationValue(Application.class.getDeclaredMethod("getDog"), Bean.class, String.class);
//        System.out.println("name = " + name);
    }

    @Autowired
    private Cat cat;

    @Bean("encoder")
    private Encoder getEncoder() {
        System.out.println("cat.dog-->"+cat.getDog());
        Encoder encoder = new Encoder();
        encoder.type = "baseEncoder";
        return encoder;
    }
}
