package ru.study.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.study.annotation.Bean;
import ru.study.annotation.Log;
import ru.study.annotation.Property;
import ru.study.service.AnotherService;
import ru.study.service.UserService;

@NoArgsConstructor
@Data
public class Person {

    @Property("person.age")
    private int age;

    @Bean
    private UserService userService;

    @Bean
    private AnotherService anotherService;

    public void sayAge() {
        System.out.println("This person`s age is: " + getAge());
    }

    @Log
    public int getAge() {
        return age;
    }
}
