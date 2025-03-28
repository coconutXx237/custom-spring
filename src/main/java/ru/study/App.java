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

        User user = objectFactory.getBean(User.class);
        System.out.println(user);

    }
}
