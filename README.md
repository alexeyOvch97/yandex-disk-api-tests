# Yandex Disk API Tests

Автотесты для проверки REST API Яндекс.Диска.

## Stack

- Java 21
- Maven
- JUnit 5
- Rest Assured
- Jackson
- Lombok
- Allure Report

## Configuration

Base URL хранится в `src/main/resources/config.properties`:

```properties
base.url=https://cloud-api.yandex.net
```

Для авторизации необходимо передать OAuth токен при запуске тестов:

```bash
-DYANDEX_TOKEN=<your_token>
```

## Run tests

```bash
mvn clean test -DYANDEX_TOKEN=<your_token>
```

## Allure report

После выполнения тестов открыть отчет:

```bash
allure serve target/allure-results
```