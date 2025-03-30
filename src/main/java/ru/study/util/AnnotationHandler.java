package ru.study.util;

import ru.study.annotation.Bean;
import ru.study.annotation.Property;

import java.lang.reflect.Field;
import java.util.Map;

public class AnnotationHandler {

    private final PropertyInjector propertyInjector;
    private final BeanInjector beanInjector;

    public AnnotationHandler() {
        propertyInjector = new PropertyInjector();
        beanInjector = new BeanInjector();
    }

    public <T> void processAnnotations(Class<T> targetClass, Object obj, Map<Class<?>, Object> beanContainer) {
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Bean.class)) beanInjector.injectBean(field, obj, beanContainer);
            if (field.isAnnotationPresent(Property.class)) propertyInjector.injectProperty(field, obj);
        }
    }
}
