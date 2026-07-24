package ru.yandex.disk.poligon.config;

public class Config {

    private Config() {
    }

    public static final String BASE_URL = PropertyLoader.get("base.url");

    public static final String TOKEN = getRequiredProperty("YANDEX_TOKEN");

    private static String getRequiredProperty(String name) {

        String value = System.getProperty(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "System property '" + name + "' is not set. Use -D" + name + "=token"
            );
        }

        return value;
    }
}
