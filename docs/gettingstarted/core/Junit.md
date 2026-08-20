# JUnit Инструкция

## Настройка/конфигурация

Framework [Аналогично](../core/Settings.md)

UI module [Аналогично](../core/UiCore.md)

Backend - самостоятельно

[Junit документация](https://junit.org/junit5/docs/current/user-guide/)

## Gradle

### dependency

```groovy
implementation "ru.autotestframework.components:web-junit:${version}"
```

### TestTask

```groovy
//Именование таски на ваш Выбор
task junit5e2e(type: Test) {
    doFirst {
        useJUnitPlatform() {
            includeTags("@webElemJunit") //укажите ваши тэги
        }
        //systemProperty("framework.junit", true) опционально для перехода (cucumber и junit совместно 
        //подключены в один модуль (не рекоммендуется)) (в таске для Cucumber без этой строки)
        systemProperty 'spring.profiles.active', 'local'
        systemProperty 'junit.jupiter.extensions.autodetection.enabled', true // опционально для Junit
    }
}
```

Пометка - локально тесты лучше (для производительности) запускать посредством JUnit (Idea / Gradle + Junit)
[ex](https://gitlab-01/automated-testing-technology/core_frameworks/fr-chicory-framework/-/merge_requests/1#note_2008326)

### Example

1. Классы тестов желательно наследовать
   от [BaseSpringJunitTest](../../../core/spring-core/src/main/java/ru/psb/autotestframework/junit/BaseSpringJunitTest.java)

#### seed
Примеры можно наблдать в проекте-шаблоне c нужными зависимостями
[seed-project](https://gitlab-01/automated-testing-technology/core_frameworks/seed-projects/seed-autotest-framework/-/tree/f/ATTDD-12793-tests/junit)

#### Примеры тестов

2. Есть готовый base для ui - BaseUITest (отвечает за поднятие Драйвера (Браузера / Desktop-приложения) с
   использованием junit5 @After /@Before), [BaseUITest](../../../core/ui-core/src/main/java/ru/psb/autotestframework/ui_core/junit/BaseUITest.java)

При необходиости можно написать свои base-классы и использовать их

Примеры:

1. [Web Ui ex](../../../components/web-elements/src/test/java/ru/psb/autotestframework/junit/tests)

Классы наследовать от InjectedPage, например:

```Java
public class TestFormPage extends InjectedPage<TestFormPage> {
}
```

## Backend

Работа с бэкендом (DB / API / sftp / HTTP-requests)
Предполагается самостоятельно.

Требуется подключить spring-core и необходимые зависимости
```groovy
dependencies {
testImplementation "ru.autotestframework.core:spring-core:${VERSION}"
}
```

Note! Для тестирования бэкенда без использования кукумбера - обязательное подключение модулей не предполагается.

### DB

```Java
public class TestClass {
    @Test
    public void someTest() {
        String sqlQuery = "SELECT * FROM TABLE QQ WHERE id = 12";
//...
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        Configuration conf = new DefaultConfiguration()
                .set(conn)
                .set(new DefaultExecuteListenerProvider(new ReportListener())); //подключить необходимый listener
        DSLContext dbContext = DSL.using(conf);
        Result<Record> res = dbContext.fetch(sqlQuery);
        Assertions.assertThat(res.size()).withFailMessage("Неверное число записей" + 3).isEqualTo(3);
        Double sum = res.get(0).getValue("0", Double.class);
        Assertions.assertThat(sum).withFailMessage("Неправильная сумма").isEqualTo(3.14159);
    }
}
```

### http-запросы

```Java
public class TestClass {
    @Test
    public void someTest() {
        String sqlQuery = "SELECT * FROM TABLE QQ WHERE id = 12";
        RequestSpecification spec = configuration.getRequestSpecification().filter(); //add some filter to log (
        var response = given().spec(spec)
                .baseUri("https://gitlab-01/")
                .formParams("form_1", "value 1")
                .post()
                .then();
        resp.statusCode(200)
                .cookie("_gitlab_session")
                .body(text);
    }
}
```

[Пример теста с RestAssured](../../../components/http-steps/src/test/java/ru/psb/autotestframework/http_steps/JUnitExamplems/JunitRestAssured.java)

#### framework.properties

Использовать можно в коде тестов

```Java
public class TestClass {
    // использование готового bean-а (при использовании общего файла пропертей)

    @Autowired
    FrameworkDefaultVariables defaultVariables;

    // использование Spring-аннотаций а) через аннотации в классах. б) через файлы-конфигурации (дополнительные-файлы.)

    @Value("${framework.*variables.url.form:}")
    private String urlPath;

    @Test
    public void someTest() {
        String urlS = defaultVariables.getVariables().get("url.form");
    }
}
```

## TMS и отчетность

Интеграция с ТМС задумана в следующем формате

### I. Allure не нужен

Делать интеграцию по документации ТМС.

### II. Allure нужен

1. Степы в проекте помечаются аннотациями Allure (чтоб не вести паралельно несколько одинаковых для различных систем
   отчетности (TestIT, TestOps, TestRail) и т.д.
2. Тесты пометить с
   использованим [аннотаций адаптеров TMS](https://gitlab-01/automated-testing-technology/libraries/test-it/testit-adapters-java/-/tree/develop/testit-java-commons/src/main/java/ru/psb/testit/annotations)
   ([подробнее](https://gitlab-01/automated-testing-technology/libraries/test-it/testit-adapters-java/-/tree/develop/testit-adapter-junit5))
   @WorkItemIds и т.д.
3. Методы (Степы) пометить аннотациями Allure

Подключить [AllureTestITAdapter](https://gitlab-01/automated-testing-technology/libraries/test-it/testit-adapters-java/-/tree/f/ATTDD-14158-allure_adapter/allure-adapter)

Рекомендуемый подход - необходимо добавить Listener-ы (allure-jooq,
allure-restassured, [подробнее](https://github.com/allure-framework/allure-java), которые по выполнению методов из
библиотек будут создавать дополнительные-шаги (SubSteps при вызове внутри кастомных шагов). Можете писать
самостоятельно, но это трудозатратно.

Пример. Можно подключить и
использовать [Allure-logger](../../../plugins/allure-restassured/src/main/java/ru/psb/allure/restassured/AllureRestAssuredLogger.java)
при необходимости использования Allure, или написать аналогичный логгер для TestIT

а. Добавить плагин

```groovy
implementation "ru.autotestframework.plugins:allure-restassured:${version}"
```

Возможно для удобства, со стороны Core будет предоставлен класс который будет управлять соединениями с БД (заниматься
перераспределением соединений/пулов соединений между тестами для уменьшения нагрузки на БД.

### Gradle Note

Ввиду сложности обновлений извне и использовании Java 17 на Red OS Docker образах, версии Gradle ограничены с 7.3 (17-я Java officially supported) по 7.5.1 (ставится по СЗ). 
