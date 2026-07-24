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

```
base.url=https://cloud-api.yandex.net
```

Для авторизации необходимо передать OAuth токен через переменную окружения:

```
YANDEX_TOKEN=<your_token>
```

## Run tests

Запуск всех тестов:

```bash
mvn clean test
```

Запуск с переменной окружения:

Windows PowerShell:

```powershell
$env:YANDEX_TOKEN="your_token"
mvn clean test
```

Linux / macOS:

```bash
export YANDEX_TOKEN=your_token
mvn clean test
```

## Allure report

После выполнения тестов сформировать отчет:

```bash
allure serve target/allure-results
```