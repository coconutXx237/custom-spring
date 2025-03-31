package ru.study.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ConverterTest {

    Converter converter;

    @BeforeEach
    public void setUp() {
        converter = new Converter();
    }

    @Test
    void testConvertStringAsString() {
        Object actualRes = converter.convertValue("test", String.class);
        assertInstanceOf(String.class, actualRes);
        assertEquals("test", actualRes);
    }

    @Test
    void testConvertStringToInteger() {
        Object actualRes = converter.convertValue("2234", Integer.class);
        assertInstanceOf(Integer.class, actualRes);
        assertEquals(2234, actualRes);
    }

}
