# Настройки

Вся основная конфигурация организована средствами Spring.
Настройки можно передавать следующими способами (отсортировано по увеличению приоритета):

- Дефолтные настройки спринга `SpringApplication.setDefaultProperties`
- Настройки, находящиеся в `framework.properties`
- Настройки, находящиеся в `framework-{envName}.properties` (зависящие от окружения, оно же spring profile)
- Переменные среды
- Аргументы командной строки

Подробнее можно
прочитать [тут](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config)

### Доступные настройки

##### Proxy

Стандартная настройка [JVM прокси](https://docs.oracle.com/javase/8/docs/technotes/guides/net/proxies.html).  
Обязана применяться ко всем модулям фреймворка
```properties
framework.proxy.enabled
framework.proxy.host
framework.proxy.port
framework.proxy.user
framework.proxy.password
framework.proxy.non-proxy-hosts
```    

##### SSL
```properties
framework.ssl.enabled - включение/выключение ssl валидации, по умолчание выключено
framework.ssl.trust-store - путь до хранилища сертификатов 
``` 

##### WireMock
Настройка мок сервера
```properties
mockservice.port
```

##### RestAssured
```properties
framework.rest-assured.timeout --дефолтный таймаут
framework.rest-assured.logs.enabled --включение отображения http логов
framework.rest-assured.numberReturnType --изменение возвращаемых значений для больших значений (default:FLOAT_AND_DOUBLE) 
```

##### Другое

```properties
framework.deprecated-features.enabled default=false  --включение использования шагов, помеченных как `@Deprecated`
framework.temp.files.cleaning.enabled default=true  --включение очистки папки с временными файлами`
framework.array.string.delimiter default=; --разделитель для списков преобразованных в строку
framework.remove.git.hooks default=false --включение/выключение использования githook-ов
framework.ui.imageComparison.pixelToleranceLevel default=0.1 --уровень допуска по разнице каждого пикселя. По умолчанию разница составляет 0.1 -> 10%. Значение может быть установлено в диапазоне от 0.0 до 0.99
framework.ui.imageComparison.allowingPercentOfDifferentPixels default=0.0 --уровень допуска по общей разнице пикселей на скриншотах. По умолчанию разница составляет 0.0 -> 0%. Значение может быть установлено в диапазоне от 0.0 до 0.99 
```

##### Модуль UI

Доступные настройки UI компонент фреймворка находится в соответствующей [документации](../core/UiCore.md). 

