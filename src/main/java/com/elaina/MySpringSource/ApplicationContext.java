package com.elaina.MySpringSource;

import com.elaina.MySpringSource.annotation.Bean;
import com.elaina.MySpringSource.annotation.Configuration;
import com.elaina.MySpringSource.annotation.SpringApplication;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: ApplicationContext
 * Package: com.elaina.MySpringSource
 * Description:
 *
 * @author 灰之魔女-伊蕾娜
 * @version 1.1
 * @create 2026/1/4 21:09
 */
@SuppressWarnings("unchecked")
public class ApplicationContext {
    private final String CLASS_EXT = ".class";

    private final Map<String, Object> ioc = new LinkedHashMap<>();

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private final Map<String, Object> loadingIoc = new HashMap<>();

    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    private static ApplicationContext applicationContext;

    private ApplicationContext(String packageName) throws URISyntaxException, IOException {
        initContext(packageName);
    }

    private ApplicationContext(String[] packageNames) throws URISyntaxException, IOException {
        initContext(packageNames);
    }

    public Object getBean(String name) {
        if ("haidaogou".equals(name)) {
            System.out.println("ioc.containsKey(name) = " + ioc.containsKey(name));
        }
        if (ioc.containsKey(name)) {
            return ioc.get(name);
        } else if (beanDefinitionMap.containsKey(name)) {
            return createBean(beanDefinitionMap.get(name));
        }
        return ioc.get(name);

    }

    public <T> T getBean(Class<T> beanType) {
        BeanDefinition bd = beanDefinitionMap.values().stream()
                .filter(beanDefinition -> beanType.isAssignableFrom(beanDefinition.getBeanType()))
                .findFirst().orElse(null);
        if (bd != null) {
            return (T) getBean(bd.getName());
        }
        return null;
    }

    public <T> T getBean(Class<T> beanType, String name) {
        BeanDefinition bd = beanDefinitionMap.values().stream()
                .filter(beanDefinition -> beanType.isAssignableFrom(beanDefinition.getBeanType()) && beanDefinition.getName().equals(name))
                .findFirst().orElse(null);
        if (bd != null) {
            return (T) getBean(bd.getName());
        }
        return null;
    }

    public <T> List<T> getBeans(Class<T> beanType) {
        return beanDefinitionMap.values().stream()
                .filter(beanDefinition -> beanType.isAssignableFrom(beanDefinition.getBeanType()))
                .map(beanDefinition -> (T) getBean(beanDefinition.getName()))
                .toList();
    }


    private void initContext(String packageName) throws URISyntaxException, IOException {
        scanPackage(packageName).stream()
                .filter(this::canCreate).forEach(this::wrapper);
        initBeanPostProcessor();
        beanDefinitionMap.values().forEach(this::createBean);
    }

    private void  initContext(String[] packageNames) throws URISyntaxException, IOException {
        scanPackage(packageNames).stream()
                .filter(this::canCreate)
                .forEach(this::wrapper);
        initBeanPostProcessor();
        beanDefinitionMap.values().forEach(this::createBean);
    }

    private void initBeanPostProcessor() {
        beanPostProcessorList.addAll(beanDefinitionMap.values().stream()
                .filter(beanDefinition ->BeanPostProcessor.class.isAssignableFrom(beanDefinition.getBeanType()))
                .map(this::createBean).map(bean -> (BeanPostProcessor) bean).toList());
    }

    private Object createBean(BeanDefinition type) {
        if (ioc.containsKey(type.getName())) {
            return ioc.get(type.getName());
        }
        if (loadingIoc.containsKey(type.getName())) {
            return loadingIoc.get(type.getName());
        }
        return doCreateBean(type);
    }

    private Object doCreateBean(BeanDefinition type) {
        Constructor<?> constructor = type.getConstructor();
        try {
            Object bean;
            if (type instanceof ConfigBeanDefinition configBeanDefinition) {
                Object config = getBean(configBeanDefinition.getConfigDefinition().getName());
                Method beanMethod = configBeanDefinition.getBeanMethod();
                if (beanMethod.getParameterTypes().length > 0) {
                    bean = beanMethod.invoke(config, Arrays.stream(beanMethod.getParameterTypes()).map(this::getBean).toArray());
                } else {
                    bean = beanMethod.invoke(config);
                }
                bean = initializeBean(bean, type);
                ioc.put(type.getName(), bean);
                return bean;

            }else {
                bean = constructor.newInstance();
            }
            loadingIoc.put(type.getName(), bean);
            autoWiredBean(bean, type);
            bean = initializeBean(bean, type);
            loadingIoc.remove(type.getName());
            ioc.put(type.getName(), bean);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object initializeBean(Object bean, BeanDefinition beanDefinition){
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            bean = beanPostProcessor.beforeInitializeBean(bean, beanDefinition.getName());
        }
        if (!beanDefinition.getPostConstructMethods().isEmpty()) {
            for (Method method : beanDefinition.getPostConstructMethods()) {
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException(String.format("初始化bean(%s)失败", beanDefinition.getName()) + e);
                }
            }
        }
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            bean = beanPostProcessor.afterInitializeBean(bean, beanDefinition.getName());
        }
        return bean;
    }

    private void autoWiredBean(Object bean, BeanDefinition beanDefinition) throws IllegalAccessException{
        if (beanDefinition.getAutowiredBeans().isEmpty()) {
            return;
        }
        for (AutoWiredBeanDefinition autoWiredBeanDefinition : beanDefinition.getAutowiredBeans()) {
            Object obj;
            if (autoWiredBeanDefinition.getName().isEmpty()) {
                obj = getBean(autoWiredBeanDefinition.getBeanType());
            } else {
                obj = getBean(autoWiredBeanDefinition.getName());
            }
            if (obj == null && autoWiredBeanDefinition.isRequired()) {
                throw new RuntimeException("类型：" + autoWiredBeanDefinition.getBeanType().getName() + "自动注入属性失败");
            }
            autoWiredBeanDefinition.getField().set(bean, obj);
        }
    }

    private void wrapper(Class<?> type) {
        BeanDefinition beanDefinition = new BeanDefinition(type);
        if (beanDefinitionMap.containsKey(beanDefinition.getName())) {
            return;
        }
        beanDefinitionMap.put(beanDefinition.getName(), beanDefinition);
        if (AnnotationUtil.isAnnotationPresent(type, Configuration.class)) {
            Arrays.stream(type.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Bean.class)).forEach(method -> {
                try {
                    method.setAccessible(true);
                    String name = AnnotationUtil.getMethodAnnotationValue(method, Bean.class, String.class);
                    ConfigBeanDefinition bean = new ConfigBeanDefinition(method.getReturnType(), name, beanDefinition, method);
                    beanDefinitionMap.put(name, bean);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    protected boolean canCreate(Class<?> type) {
        return AnnotationUtil.isAnnotationPresentComponent(type) || AnnotationUtil.isAnnotationPresent(type, Configuration.class);
    }

    private List<Class<?>> scanPackage(String packageName) throws URISyntaxException, IOException {
        List<Class<?>> classList = new ArrayList<>();
        URL url = this.getClass().getClassLoader().getResource(packageName.replace(".", File.separator));
        assert url != null;
        Path path = Path.of(url.toURI());
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                Path absolutePath = file.toAbsolutePath();
                if (absolutePath.toString().endsWith(CLASS_EXT)) {
                    String classPath = absolutePath.toString().replace(File.separator, ".");
                    int classIndex = classPath.indexOf(packageName);
                    String className = classPath.substring(classIndex, absolutePath.toString().length() - CLASS_EXT.length());
                    try {
                        classList.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println(classList);
        return classList;
    }

    private List<Class<?>> scanPackage(String[] packageNames) throws URISyntaxException, IOException {
        ArrayList<Class<?>> classList = new ArrayList<>();
        for (String packageName : packageNames) {
            classList.addAll(scanPackage(packageName));
        }
        return classList;
    }

    @Deprecated
    public static void run(String packageName) {
        try {
            applicationContext = new ApplicationContext(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void run(Class<?> bootClass) {
        try {
            SpringApplication annotation = bootClass.getDeclaredAnnotation(SpringApplication.class);
            if (annotation == null) {
                throw new RuntimeException("未找到启动类");
            } else if (annotation.value().length == 0) {
                applicationContext = new ApplicationContext(getPackageName(bootClass));
            } else {
                String[] values = annotation.value();
                applicationContext = new ApplicationContext(Arrays.stream(values).map(value -> "".equals(value) ? getPackageName(bootClass) : value).collect(Collectors.toList()).toArray(String[]::new));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    private static String getPackageName(Class<?> bootClass) {
        return bootClass.getPackage().getName();
    }

    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new RuntimeException("还没有启动的应用");
        }
        return applicationContext;
    }

}
