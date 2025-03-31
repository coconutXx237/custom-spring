package ru.study.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyLoaderTest {

    private PropertyLoader propertyLoader;

    @BeforeEach
    void setUp() {
        propertyLoader = new PropertyLoader("test.properties");
    }

    @Test
    void testGetExistingProperty() {
        String actualRes = propertyLoader.getProperty("user.name");
        assertEquals("testUserName", actualRes);
    }

    @Test
    void testGetNonExistingProperty() {
        assertNull(propertyLoader.getProperty("non.existing"));
    }

    @Test
    void testNonExistentPropertyFile() {
        Object result = assertThrows(RuntimeException.class, () -> {
            new PropertyLoader("non-existing.properties");
        });
        assertInstanceOf(NullPointerException.class, result);
    }
}
