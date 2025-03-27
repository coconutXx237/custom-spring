package ru.study.util;

import ru.study.annotation.Bean;
import ru.study.annotation.Property;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {

    private final Map<Class<?>, Object> beanContainer = new HashMap<>();
    private final PropertyLoader propertyLoader;


    public ObjectFactory() {
        propertyLoader = new PropertyLoader("application.properties");
    }

    public <T> T getBean(Class<T> clazz) {
        try {
            var obj = clazz.getDeclaredConstructor().newInstance();
            var beans = createBeans(clazz);
            beanContainer.putAll(beans);
            handleAnnotations(obj, beanContainer);
            return obj;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

    private void handleAnnotations(Object obj, Map<Class<?>, Object> beanContainer) {
        Field[] fields = obj.getClass().getDeclaredFields();
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


