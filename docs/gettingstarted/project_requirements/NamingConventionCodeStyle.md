## Naming conventions
### Переменные контекста

Наименование переменной контекста условно разделяется на две части `${{prefixpart.varName}}`:

- prefixpart (аналогично package name):
    - префикс может отсутствовать
    - все символы указываются в нижнем регистре
    - логически неделимые слова пишутся слитно - `${{autotestframework.title}}`, `${{rabbitmq.url}}`
    - логически делимые слова разделяются точкой - `${{service.db.url}}`
    - нельзя использовать любые другие спецсимволы

- varName:
    - имя переменной указывается в lowerCamelCase

### Файлы в проекте

Следующие категории файлов должны быть записаны стилем `snake_case`
- Файлы с расширением `*.feature` (ruble_payment.feature, client_letter.feature и т.д.)
- Все тестовые данные лежащие в папке `src/test/resources/data`
- Вспомогательные файлы/утилиты в директории `tools`

### .feature файл
```gherkin
#language:ru
@SaveContext #тэги в стиле CamelCase
# Функция: Имя "функции" с большой буквы
Функция: Демонстрация возможностей HTTP
#Сценарий: Имя сценария с большой буквы
Сценарий: Проверка получения результата деления 
```

### Файлы-конфигурации (.propreties)

Именование должно быть
аналогично [Spring Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

### Java

Стандартные применимые CodeStyle-ы:
- [CheckStyle](https://checkstyle.org/config.html)
- [Google](https://google.github.io/styleguide/javaguide.html)