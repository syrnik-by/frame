## Параллелизация тестов на cucumber7 проектах

Для настройки возможности запуска автотестов в несколько потоков необходимо:

#### 1. Gradle Task

Прописать в файле build.gradle в таску, которая у Вас отвечает за запуск автотестов определенные строки
Например:
```groovy
//таска теперь использует Junit Engine для запуска тестов
task executeFeatures(type: Test) {
   doFirst {
      useJUnitPlatform() { // задействовать junit раннер
         excludeTags 'componentTesting' // отключить юнит тесты
      }
      //задайте необходимое число потоков		

      maxParallelForks = 16//Runtime.getRuntime().availableProcessors()
      testLogging.showStandardStreams = true
      testLogging.exceptionFormat = 'full'

      def skippedTags = 'not (@Skip or @Demo)'
      def tags = project.hasProperty('tags') ? findProperty('tags') + ' and ' + skippedTags : skippedTags
      systemProperties["cucumber.execution.parallel.enabled"] = true
      systemProperties["cucumber.execution.parallel.mode.default"] = "concurrent"
      systemProperties["cucumber.execution.parallel.config.strategy"] = "fixed"
      systemProperties["cucumber.execution.parallel.config.fixed.parallelism"] = maxParallelForks
      systemProperties["cucumber.filter.tags"] = tags //новый способ передачи тэгов в движок кукумбера
      //environment "CUCUMBER_FILTER_TAGS", tags //старый способ передачи тэгов в движок кукумбера 

      //передача используемого окружения осталась прежней
      if (project.hasProperty('stand')) {
         environment "SPRING_PROFILES_ACTIVE", findProperty('stand').toString().toLowerCase()
      }
   }
}

```
#### 2. Junit Runner
Использовать новый Runner вместо CucumberRunner-а

```Java
@Suite
@SelectClasspathResource("features")
@ConfigurationParameters({
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = DEFAULT_GLUE),
        @ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true"),
        @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
                value = ALLURE_PLUGIN + "," +
                        "pretty"
        ),        
        @ConfigurationParameter(key = EXECUTION_LIMIT_PROPERTY_NAME, value = "3"), //не работает
        @ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long"),
})
public class Runner {
}
```
Соответствующие Зависимости есть в новой версии Core-FrameWork-а

#### 3. Особенности

Необходимо исключить использование "статики" (какие-то общие ресурсы) для параллелизации:
1. Файлы локальные (если что-то перезаписываете, создаете локально своими Шагами).
2. Данные Системы (надо смотреть каждый случай в отдельности).
3. Реализация со стороны разработки.
   Например, перед созданием нового платежа происходит запрос из sequence нового id док-та, и при параллельных запросах в API/БД возвращает одно и то же значение. При попытке создания документа - один документ создается успешно, второй - возвращает ошибку 400 - что данная операция невозможна.

Варианты решения:
а) Исключить из параллельного запуска.
б) JUnit аннотации связанные с read-write, подробнее в статье https://github.com/cucumber/cucumber-jvm/tree/main/junit-platform-engine.
в) Механизм лока на уровне кода (Синглтон, который регулирует обращения к таким ресурсам) н-р synchronized/lock и т.д.).
г) Перезапуск тестов при падении (?) - не реализовано.

4. @ScenarioScope
   Если есть класс/компонента, в котором вы храните тестовые данные - то необходимо использовать данную аннотацию с использованием другой аннотации Spring - @Component (самое простое).

5. Может возникать ошибка при запуске тестов вида Illegal character in path at index 31: classpath:/features/test/active — копия (10).feature.
   Означает что имя feature-файла содержит недопустимые символы(пробелы) необходимо переименовать.

6. @SaveContext - не будет работать корректно, если будет запущено несколько тестов параллельно с одними и теми же "запоминаемыми" переменными (так как они будут перезаписывать друг друга в общем).

7. Возможно необходимо будет добавить / увеличить ожидания в тестах.

Context теперь для каждого теста свой (изолирован от общего), и есть общий, где находится конфигурация фреймворка (добавлен в контекст отдельного теста).

Можно запускать как в режиме переиспользования браузера  (будет использоваться общий пул, при окончания одного теста Объект Браузера передается в другой), так и в обычном режиме (каждый раз создается новый Драйвер)
```properties
framework.ui.driver.web.reuseBrowserEnabled=true
```