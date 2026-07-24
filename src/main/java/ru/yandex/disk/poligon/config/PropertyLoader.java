package ru.yandex.disk.poligon.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = PropertyLoader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private PropertyLoader() {}

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
