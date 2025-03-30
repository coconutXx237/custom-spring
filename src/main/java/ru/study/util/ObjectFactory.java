package ru.study.util;

import net.sf.cglib.proxy.Enhancer;
import ru.study.annotation.Bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {

    private final Map<Class<?>, Object> beanContainer = new HashMap<>();
    private final AnnotationHandler annotationHandler;
    private final EnhancerProvider enhancerProvider;

    public ObjectFactory() {
        annotationHandler = new AnnotationHandler();
        enhancerProvider = new EnhancerProvider();
    }

    public <T> T getBean(Class<T> targetClass) {
        Enhancer enhancer = enhancerProvider.getEnhancer(targetClass);
        T proxyObj = (T) enhancer.create();
        //var obj = clazz.getDeclaredConstructor().newInstance();
        var beans = createBeans(targetClass);
        beanContainer.putAll(beans);
        annotationHandler.processAnnotations(targetClass, proxyObj, beanContainer);
        return proxyObj;
    }

    private <T> Map<Class<?>, Object> createBeans(Class<T> clazz) {
        Map<Class<?>, Object> resultMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> beanType = field.getType();
            if (field.isAnnotationPresent(Bean.class) && !beanContainer.containsKey(beanType)) {
                try {
                    Object beanObject = beanType.getDeclaredConstructor().newInstance();
                    resultMap.put(beanType, beanObject);
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return resultMap;
    }
}


