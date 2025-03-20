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
import java.util.Set;

import static ru.study.util.StringConstants.SCANNING_PATH;


public class ObjectFactory {

    private static final Map<Class<?>, Object> beanContainer = new HashMap<>();
    private final Reflections reflections;

    public ObjectFactory() {
        reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(SCANNING_PATH)
                        .addScanners(Scanners.FieldsAnnotated)
                        .addClassLoaders(ClasspathHelper.contextClassLoader())
        );
    }

    public <T> T getBean(Class<T> clazz) {
        try {
            var obj = clazz.getDeclaredConstructor().newInstance();
            createBeans();
            injectBeans(obj);
            return obj;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void injectBeans(Object obj) {
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

    private void createBeans() {
        Set<Field> beanFields = reflections.getFieldsAnnotatedWith(Bean.class);
        for (Field field : beanFields) {
            Class<?> beanType = field.getType();
            try {
                Object beanObject = beanType.getDeclaredConstructor().newInstance();
                beanContainer.putIfAbsent(beanType, beanObject);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


