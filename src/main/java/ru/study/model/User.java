package ru.study.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.study.annotation.Bean;
import ru.study.annotation.Property;
import ru.study.service.SomeService;
import ru.study.service.UserService;

@NoArgsConstructor
@Data
public class User {

    @Property("user.name")
    private String username;

    @Bean
    private UserService userService;

    @Bean
    private SomeService someService;
}
