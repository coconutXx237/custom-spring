package ru.study.util;

import java.lang.reflect.Field;
import java.util.Map;

public class BeanInjector {
    public void injectBean(Field field, Object obj, Map<Class<?>, Object> beanContainer) {
        field.setAccessible(true);
        var bean = beanContainer.get(field.getType());
        try {
            field.set(obj, bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
