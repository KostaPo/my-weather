package ru.kostapo.myweather.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            File file = new File(PropertiesUtil.class.getClassLoader().getResource("properties").getFile());
            FileReader fileReader = new FileReader(file);
            properties.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException("Can't load 'properties' file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
