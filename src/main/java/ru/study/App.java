package ru.study;

import ru.study.model.Person;
import ru.study.model.User;
import ru.study.util.ObjectFactory;

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
