package ru.study.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.study.annotation.Bean;
import ru.study.service.AnotherService;
import ru.study.service.UserService;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {

    @Bean
    private UserService userService;

    @Bean
    private AnotherService anotherService;

}
