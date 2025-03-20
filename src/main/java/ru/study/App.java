package ru.study;

import ru.study.model.User;
import ru.study.util.UserFactory;

public class App
{
    public static void main( String[] args ) {

        UserFactory userFactory = new UserFactory();
        User user = userFactory.createUser();
        System.out.println(user);
    }
}
