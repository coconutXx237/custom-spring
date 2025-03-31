package ru.study.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import ru.study.annotation.Log;

import java.lang.reflect.Method;
import java.time.Instant;

public class EnhancerProvider {
    public <T> Enhancer getEnhancer(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((MethodInterceptor) (obj, method, arguments, proxy) -> {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method methodd : methods) {
                methodd.setAccessible(true);
                if (methodd.isAnnotationPresent(Log.class) && methodd.equals(method)) {
                    System.out.printf("Метод %S у объекта %s класса %s вызвался во время '%s'\n", method.getName(), proxy.hashCode(), obj.getClass(), Instant.now());
                }
            }
            return proxy.invokeSuper(obj, arguments);
        });
        return enhancer;
    }
}
