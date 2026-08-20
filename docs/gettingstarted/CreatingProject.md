## Создание и запуск проекта на базе фреймворка

### Проект-шаблон

С целью оптимизации трудозатрат (для быстрого подключения) рекомендуется использовать:
- [Шаблонный проект для Linux](https://gitlab-01/automated-testing-technology/core-frameworks/seed-projects/seed-autotest-framework/-/tree/f/PB-52436-seed-linux)
- [Шаблонный проект для Windows](https://gitlab-01/automated-testing-technology/core-frameworks/seed-projects/seed-autotest-framework/-/tree/master)

### Создание с нуля

- Создать новый Gradle Java проект (можно сделать непосредственно в IDEA, необходима Java 11)

- Переопределить properties для Gradle-wrapper-а
```properties
distributionUrl=https\://nexus-external.psbnk.msk.ru/repository/services.gradle.org-distributions/gradle-{ver}-bin.zip
```
где, {ver} - необходимая версия Gradle

- Добавить в созданный build.gradle файл репозитории и зависимости:
```groovy
plugins {
    id 'java'   
}

var VERSION = "3.0"

group 'ru.autotestframework'
version VERSION

compileTestJava.options.encoding = 'UTF-8'

task testInTest(type: Test) {
  //Если у вас несколько конутров/окружений повзоляет запустить тесты в соответствующем
  doFirst {
    if (hasProperty('stand')){
      def envProfile = findProperty('stand').toString().toLowerCase()
      environment "SPRING_PROFILES_ACTIVE", envProfile
    }
  }
}

repositories {
  maven {
    //репозитории для внутренних проектов
    url 'https://nexus-internal.headoffice.psbank.local/repository/at-maven-group/'
  }

  maven {
    //Проксированный репозиторий для MavenCentral
    url 'https://nexus-external.psbnk.msk.ru/repository/maven-public/'
  }
}

dependencies {
  // добавить только тот модуль, шаги которого будут использоваться в проекте
  testImplementation "ru.autotestframework.components:http-steps:${VERSION}"
  testImplementation "ru.autotestframework.components:sql-steps:${VERSION}"
  testImplementation "ru.autotestframework.components:debug-steps:${VERSION}"
  testImplementation "ru.autotestframework.components:queue-steps:${VERSION}"
  testImplementation "ru.autotestframework.components:ftp-steps:${VERSION}"
  //для кодогенерации с использованием Lombok
  testCompileOnly 'org.projectlombok:lombok:1.18.22'
  testAnnotationProcessor 'org.projectlombok:lombok:1.18.22'
}
```        

#### Важно
desktop-elements и html-elements могут неправильно работать в рамках одного build.gradle. Для автоматизации desktop и
web приложений рекомендуется использовать отдельные модули.

Аналогичного принципа следует придерживаться при тестировании Frontend + Backend (н-р, html-elements + http-steps)

Добавить в settings.gradle репозитории (нужно для плагинов):
```groovy
pluginManagement {
    repositories {
        maven {
            url 'https://nexus-internal.headoffice.psbank.local/repository/at-maven-group/'
        }
        maven {
            url 'https://nexus-external.psbnk.msk.ru/repository/maven-public/'
        }
    }
}
```

- В директории `src/test/java` создать пакет `ru.autotestframework.*`
  где `*` - имя вашего проекта

#### Структура
- В `src/test/resources/data` тестовые данные и должны делиться по назначению и типу (data\http\json)
- В `src/test/resources` файлы конфигураций
- В `src/main/java/utils` классы\методы для доп. работы с данными (генерация данных, сравнения, конвертация данных, логины\пароли, перечисления и тд)
- В `src/main/java/dataprovider` классы\методы для работы с БД
  Образец файловой директории:
```textmate
resources
|    data    
|    |    payments
|    |    |    denied         
|    |    |    |    request.xml
|    |    |    |    response.xml
|    framework.properties
|    framework-local.properties
|    framework-preprod.properties
```

#### Запуск
Для запуска тестов необходимо выполнить команду `gradle test` или `gralde testInTestEnv` при необходимости запустить в другом окружении

#### Git hooks
Для обеспечения работы встроенных в ядро гит-хуков, необходимо в build.gradle добавить таску с запуском unit/integration тестов проекта. Эти тесты не имеют отношения к тестируемой системой и должны проверять работоспособность кастомных шагов (если таковые добавляются), а так же должны проверять возможность запуска проекта

```groovy
task componentTesting(type: Test) {
    doFirst {
        // для запуска unit тестов
        useJUnitPlatform {
            includeTags '@UnitTesting'
        }
        // для запуска feature файлов интеграционных тестов
        environment "cucumber.filter.tags", "@FrameworkStartupTest"
    }
}
```

В соответствии с примером выше, для unit тестов нужно добавлять аннотацию
```
@Tag("@UnitTesting")
```

### Cucumber проект

#### Структура

Помимо общей структуры Cucumber-проект дополняется следующими пунктами

- В `src/test/resources/features` feature файлы
- В `src/main/java/hooks` (методы, запускаемые до или после сценария)

resources
...
|    features
|    |    payments
|    |    |    denied.feature
|    |

#### Runner for Cucumber

- В корне пакета ru.autotestframework создать файл Runner с содержимым:

```java
@Suite
@SelectClasspathResource("features")
@ConfigurationParameters({
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = DEFAULT_GLUE),
        @ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true"),
        @ConfigurationParameter(key = JUNIT_PLATFORM_NAMING_STRATEGY_PROPERTY_NAME, value = "long"),
})
public class Runner {
}
```

для интеграционных тестов добавлять тег "@FrameworkStartupTest"

Пример интеграционного теста

```gherkin
#language:ru

@FrameworkStartupTest
Функция: Framework startup test

  Сценарий: Framework startup test
    И установлена переменная 'var' = 'тест запуска фреймворка'
    И переменные имеют значения:
      | var | == | тест запуска фреймворка |
```
