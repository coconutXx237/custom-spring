package ru.study.util;

import ru.study.annotation.Property;

import java.lang.reflect.Field;

public class PropertyInjector {

    private final PropertyLoader propertyLoader;
    private final Converter converter;

    public PropertyInjector() {
        propertyLoader = new PropertyLoader("application.properties");
        converter = new Converter();
    }

    public void injectProperty(Field field, Object obj) {
        field.setAccessible(true);
        String key = field.getAnnotation(Property.class).value();
        String value = propertyLoader.getProperty(key);
        Class<?> targetFieldType = field.getType();
        Object convertedValue = converter.convertValue(value, targetFieldType);
        try {
            field.set(obj, convertedValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
