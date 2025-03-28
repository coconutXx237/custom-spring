package ru.study;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.MethodInterceptor;
import ru.study.model.Person;
import ru.study.model.User;
import ru.study.util.ObjectFactory;

import java.time.Instant;

public class App
{
    public static void main( String[] args ) {
        ObjectFactory objectFactory = new ObjectFactory();
        Person person = objectFactory.getBean(Person.class);
        System.out.println(person);

/*        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Person.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, argss, proxy) -> {
         //   if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
            //    return "Hello Tom!";
          //  } else {
                System.out.printf("Метод %S у объекта %s вызвался во время %s", method.getName(), proxy.hashCode(), Instant.now());
                return proxy.invokeSuper(obj, argss);
            //}
        });

        Person proxyPerson = (Person) enhancer.create();*/

        // System.out.println("Age is: " + proxyPerson.getAge());
        //person.sayAge();

    }
}
