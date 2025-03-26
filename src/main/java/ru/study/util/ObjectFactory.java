package ru.study.util;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.study.annotation.Bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {

    private final Map<Class<?>, Object> beanContainer = new HashMap<>();

    public ObjectFactory() {
    }

    public <T> T getBean(Class<T> clazz) {
        try {
            var obj = clazz.getDeclaredConstructor().newInstance();
            var beans = createBeans(clazz);
            beanContainer.putAll(beans);
            injectBeans(obj, beanContainer);
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

    private void injectBeans(Object obj, Map<Class<?>, Object> beanContainer) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Bean.class)) {
                field.setAccessible(true);
                var bean = beanContainer.get(field.getType());
                try {
                    field.set(obj, bean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}


