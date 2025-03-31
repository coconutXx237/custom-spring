package ru.study.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.study.model.Person;
import ru.study.model.User;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ObjectFactoryTest {

    ObjectFactory objectFactory = new ObjectFactory();

    @BeforeEach
    public void setUp() {
    }

    @Test
    void testGetBeanReturnsCorrectClass() {
        Object resultObjPerson = objectFactory.getBean(Person.class);
        Object resultObjUser = objectFactory.getBean(User.class);

        assertInstanceOf(Person.class, resultObjPerson);
        assertInstanceOf(User.class, resultObjUser);
    }


}
