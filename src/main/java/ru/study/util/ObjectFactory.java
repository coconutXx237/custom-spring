package ru.study.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import ru.study.annotation.Bean;
import ru.study.annotation.Property;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {

    private final Map<Class<?>, Object> beanContainer = new HashMap<>();
    private final PropertyLoader propertyLoader;


    public ObjectFactory() {
        propertyLoader = new PropertyLoader("application.properties");
    }

    public <T> T getBean(Class<T> targetClass) {
        Enhancer enhancer = getEnhancer(targetClass);
        T proxyObj = (T) enhancer.create();
        //var obj = clazz.getDeclaredConstructor().newInstance();
        var beans = createBeans(targetClass);
        beanContainer.putAll(beans);
        handleAnnotations(targetClass, proxyObj, beanContainer);
        return proxyObj;
    }

    private static <T> Enhancer getEnhancer(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((MethodInterceptor) (obj, method, arguments, proxy) -> {
            System.out.printf("Метод %S у объекта %s класса %s вызвался во время '%s'\n", method.getName(), proxy.hashCode(), obj.getClass(), Instant.now());
            return proxy.invokeSuper(obj, arguments);
        });
        return enhancer;
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

    private <T> void handleAnnotations(Class<T> targetClass, Object obj, Map<Class<?>, Object> beanContainer) {
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Bean.class)) injectBean(field, obj, beanContainer);
            if (field.isAnnotationPresent(Property.class)) injectProperty(field, obj);
        }
    }

    private void injectBean(Field field, Object obj, Map<Class<?>, Object> beanContainer) {
        field.setAccessible(true);
        var bean = beanContainer.get(field.getType());
        try {
            field.set(obj, bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectProperty(Field field, Object obj) {
        field.setAccessible(true);
        String key = field.getAnnotation(Property.class).value();
        String value = propertyLoader.getProperty(key);
        Class<?> targetFieldType = field.getType();
        Object convertedValue = convertValue(value, targetFieldType);
        try {
            field.set(obj, convertedValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object convertValue(String value, Class<?> targetType) {
        Object convertedValue = null;
        if (targetType == Integer.class || targetType == int.class) {
            convertedValue = Integer.parseInt(value);
        } else if (targetType == String.class) {
            convertedValue =  value;
        }
        return convertedValue;
    }
}


