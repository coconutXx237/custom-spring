package ru.study.util;

public class Converter {
    public Object convertValue(String value, Class<?> targetType) {
        Object convertedValue = null;
        if (targetType == Integer.class || targetType == int.class) {
            convertedValue = Integer.parseInt(value);
        } else if (targetType == String.class) {
            convertedValue =  value;
        }
        return convertedValue;
    }
}
