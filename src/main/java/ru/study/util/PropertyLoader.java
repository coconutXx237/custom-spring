package ru.study.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    private final Properties prop;
    private final InputStream inputStream;

    public PropertyLoader(String propertiesFileName) {
        prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        inputStream = loader.getResourceAsStream(propertiesFileName);
        loadProperties();

    }

    private void loadProperties() {
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return prop.getProperty(key);
    }
}
