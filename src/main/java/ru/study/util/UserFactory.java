package ru.study.util;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.study.annotation.Bean;
import ru.study.model.User;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ru.study.util.StringConstants.SCANNING_PATH;


public class UserFactory {

    private static final Map<Class<?>, Object> beanContainer = new HashMap<>();
    private final Reflections reflections;

    public UserFactory() {
        reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(SCANNING_PATH)
                        .addScanners(Scanners.FieldsAnnotated)
                        .addClassLoaders(ClasspathHelper.contextClassLoader())
        );
    }

    public User createUser() {
        User user = new User();
        createBeans();
        injectBeans(user);
        return user;
    }

    private void injectBeans(User user) {
        Field[] fields = user.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Bean.class)) {
                field.setAccessible(true);
                var bean = beanContainer.get(field.getType());
                try {
                    field.set(user, bean);
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


