# Перезапуск упавших тестов

### Перезапуск Тестов JUnit5 Suite (параллелизация) + Cucumber

Ввиду ограничений использования связки [Gradle/JUnit5/Cucumber](https://github.com/junit-team/junit5/issues/1558)

Возможные решения:

1. Maven Surefire Plugin (Gradle -> Maven) https://maven.apache.org/surefire/maven-surefire-plugin/

2. JunitPlugin 

   I. Добавить зависимость
    ```groovy
        dependencies {
            testImplementation "ru.autotestframework.plugins:junit5-retry:${VERSION}"
        }
    ```

   II. Передавать ConfigurationParameters, через SystemProperties в Gradle Task Пример
    ```groovy
        //стандартные проперти Cucumber Engine
        systemProperties["cucumber.filter.tags"] = "@active"
        systemProperties["cucumber.glue"] = "ru.autotestframework"
        systemProperties["cucumber.plugin"] = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm," +
                " ru.psb.testit.listener.BaseCucumber7Listener"
        //Кастомные проперти Расширения
        systemProperties["junit.retry.allTests"] = true //feafult false пытаемся перезапускать все упавшие тесты 
        systemProperties["junit.retry.attempts"] = 3 // число реранов для всех тестов
        systemProperties["junit.retry.timeout"] = 30 // число секунд для ожидания
    ```
При указании systemProperties["junit.retry.allTests"] = false
будет осуществлен перезапуск тестов с тэгом Retry в числе попыток указанном в системной пеерменной (как выше, или числе попыток указанном в конкретном тесте(имеет больший приоритет в любом случае запуска))

   III. Подключить JunitExtensions
   Добавить в junit-properties.properties строку
```properties
junit.jupiter.extensions.autodetection.enabled=true
```

IV. Пример c Тэгом @RetryN

N - число попыток перезапуска конкретного сценария или группы сценариев
#При падении тест перезапустится с 3 попытками

```gherkin
#language:ru
@Retry3 #cucumber
Сценарий: проверка что различные элементы активны
```

```java
@Tag("Retry3") //for JUnit5
@Test
void testMethod() {}
```

### Плагин для Тестов на основе JUnit4(+Cucumber), JUnit5, TestNG(+Cucumber)

1. Использовать [Gradle Retry Plugin](https://plugins.gradle.org/plugin/org.gradle.test-retry)

   ```groovy
   //I. в самом начале build.gradle файла, указать зависимость
   buildscript {
       dependencies {
           classpath "org.gradle:test-retry-gradle-plugin:1.3.1"
       }
   }
   
   //II. в блоке плагинов
   apply plugin: "test-retry"
   ```

2. Gradle Task

   I. Для определенной Task
   ```groovy
   retry {
       maxRetries = 3 //число попыток перезапуска для одного теста
       maxFailures = 10 //прекратить перезапуск после N-го числа суммы попыток прохождения всех тестов с учетом перезапусков
       failOnPassedAfterRetry = false
   }
   ```

   II. Для всех тестовых Task
   ```groovy
   tasks.withType(Test) {
       retry{
       //...
       }
   }
   ```

