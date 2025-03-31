package ru.study.util;

import net.sf.cglib.proxy.Enhancer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.study.model.Person;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnhancerProviderTest {

    EnhancerProvider enhancerProvider;
    ByteArrayOutputStream outputStreamCaptor;
    PrintStream standardOut;

    @BeforeEach
    public void setUp() {
        enhancerProvider = new EnhancerProvider();
        standardOut = System.out;
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testEnhancerCreatesCorrectProxy() {
        Enhancer enhancer = enhancerProvider.getEnhancer(Person.class);
        Person proxy = (Person) enhancer.create();

        proxy.sayAge();

        String outputRes = outputStreamCaptor.toString().trim();
        assertTrue(outputRes.contains("GETAGE") && outputRes.contains("Person"));
    }
}
