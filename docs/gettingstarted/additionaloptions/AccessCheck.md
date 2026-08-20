## Утилита проверки доступности Стенда

### Конфигурационный файл и настройки

Для включения функционала проверки при запуске тестов необходимо

1. выставить следующую настройку в true

    ```properties
    framework.access.check.enabled=false
    ``` 

2. Заполнить mse.properties

    ```properties
    [hostTag].[stand]=[host]
    ``` 

    _[Host]_. Принимаются следующие форматы
    
    - Адресс:Порт. Если порт не указан, то автоматичекси подставлеятся 443 порт
      - [URI](https://ru.wikipedia.org/wiki/URI)
    
    _[hostTag]_ - тэг проставляемый в feature-файле и ответственный за фильтрацию
    _[stand]_ - Окружение (контур / environment, в рамках которого проверяется доступ), [подробнее](../additionaloptions/Environment.md)
    , необязательный параметр
    
    Пример mse.properties (должен находится в src/test/resources)
    
    ```properties
    searchengine.test=https://example.ru:443
    searchengine.local=https://www.example.com
    db.test=111.222.3.4:443
    maven=https://maven-repository-example.com/
    ```

3. Указать в автотестах соответствующие тэги

4. Отчет формируется только по окружению, в котором запущены Автотесты ()

### Результат теста при непрохождении проверки

При непрохождении проверки тест будет пропущен (Skipped) c предупреждением (в логе)
_The feature 'Создание платежа' is skipped due AccessCheck failure, violated tags for current Environment = [@clientApi]
. Remove tag or provide appropriate Access. See AccessCheck Report_

### Отдельная задача Gradle [accessCheck]

Генерирует отчет по всем стендам из файла mse.properties, не зависит от параметров фреймворка

- необходима дял встраиваивания в pipeline с настроенным графиком запусков, для того, чтобы не пропустить момент, когда
  тот или иной доступ отвалился

- Для ее добавления необходимо добавить плагин в gradle-build

```groovy
plugins {
  id 'access-checker-plugin' version "<coreVersion>"
}
```

### Отчет

```
==== AccessCheck Report ====
====== Stand : COMMON ======
======== CheckAccess for tag: testIt, host: https://localhost:80
Status: Succeed - Access provided

====== Stand : local ======
======== CheckAccess for tag: clientApi, host: localhost:0
Status: Failed - Access denied. Please make preparations to get Access on this Stand

====== AccessCheck Brief Failures ====== 
Violadted tags : and not (@clientApi)
```

### Cucumber Hooks

```Java
    @Before
    public void checkAccess(final Scenario scenario) throws Exception {
        final ArrayList<String> scenarioTags = new ArrayList<>(scenario.getSourceTagNames());
        List<String> violdatedTags = accessChecker.getViolatedTags().stream()
                .filter(scenarioTags::contains)
                .collect(Collectors.toList());

        if (!violdatedTags.isEmpty()) {
            throw new AssumptionViolatedException(StringUtil.format(
                    "The feature '{}' is skipped due AccessCheck failure,"
                            + " violated tags for current Environment = {}. Remove tag "
                            + "or provide appropriate Access. See AccessCheck Report",
                    scenario.getName(),
                    violdatedTags));
        }
    }
```