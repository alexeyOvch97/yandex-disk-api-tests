package ru.yandex.disk.poligon.config;

public class Config {

    private Config() {}

    public static final String BASE_URL = PropertyLoader.get("base.url");

    public static final String TOKEN =
            getRequiredEnv("YANDEX_TOKEN");

    private static String getRequiredEnv(String name) {

        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Environment variable '" + name + "' is not set");
        }

        return value;
    }
}
