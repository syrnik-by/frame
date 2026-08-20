# Модуль web-elements

Модуль web-elements содержит в себе элементы и необходимый функционал для автоматизации тестирования web-приложений (
frontend). В данный момент для используется Selenide/Selenium представляющий из себя драйвер взаимодействия с Браузером

### Компоненты Модуля:

- _configuration_ - работа с доступными проперти и конфигами
- _elements_ - реализованные элементы
- _services / helpers_ - вспомогательные классы
- _stepdefs_ - реализация уникальных относительно других модулей шагов
  [Реализация](../../../../components/web-elements/src/main/java/ru/psb/autotestframework/web_elements)

### Конфигурация модуля:

Для возможности перехода на страницу напрямую необходимо задать поле url для соотв класса страницы

```properties
framework.ui.driver.web.starting.url - ссылка на базовую страницу тестируемой системы
framework.ui.driver.web.path - путь до web драйвера
framework.ui.driver.web.version = 114 - версия необходимого драйвера (при загрузке драйверов с внешнего источника)
framework.ui.driver.web.repo.url - url адрес внешнего источника без https:// (адрес источника в artifactory можно уточнить в отделе развития)
framework.ui.driver.web.repo.user - логин для авторизации на внешнем источнике при необходимости
framework.ui.driver.web.repo.pass - пароль для авторизации на внешнем источнике при необходимости
framework.ui.driver.web.cache.clear - необходимость очистки кэша после прогона тестов (при загрузке драйверов с внешнего источника)
framework.ui.driver.web.properties.path - путь до файла с пропертями для кокретного модуля
browser.path - путь до исполняемого файла браузера (требуется для запуска Yandex-драйвера)
chromeOptions - список [аргументов](https://peter.sh/experiments/chromium-command-line-switches/) с которым необходимо запускать Браузер (--incognito;--headless и т.д.)
framework.ui.driver.web.reuseBrowserEnabled - default false - позволяет переиспользовать Объект браузера между тестами
#reuseBrowserEnable и параллелизация конфликтует с настройкой framework.ui.closeOnFail- не использовать совместно (только для локального прогона 1-го теста)
framework.ui.driver.web.browser.logging.strategy - default off - добавление к отчету Логов из Консоли Браузера [off, page, on]
framework.ui.browser.pageLoadStrategy - ожидание загрузки страницы после событий навигации (normal/eager/none)*
```
* Подробнее [pageLoadStrategy](https://www.selenium.dev/selenium/docs/api/javascript/module/selenium-webdriver/lib/capabilities_exports_PageLoadStrategy.html)

### Особенности Модуля

- При совершении действий с элементами (на данный момент только нажатие) выполняется подсветка элемента (необходимо для
  ускорения разбора причин падения автотестов)
#### Cucumber
- Присутствуют шаги взаимодействия с функционалом браузера - Cookies/Storage
- Присутствуют шаги прямого перехода на страницы Web-приложения (с/без использования Basic Authentication)

#### Особенности конфигурирования Page

Для возможности перехода на страницу напрямую необходимо задать поле url для соотв класса страницы

```
@URL(url = "/Personal/Loans")
public String url;
```

### Troubleshooting

Для тех кто будет переходить на новую версию хрома (114)
Там при запуске возникает ошибка:

```
ChromeDriver was started successfully.
10:48:53.421  INFO  'o.o.s.remote.ProtocolHandshake' - Detected dialect: W3C
10:48:53.476  WARN  'o.o.selenium.remote.http.WebSocket' - Invalid Status code=403 text=Forbidden
java.io.IOException: Invalid Status code=403 text=Forbidden
```

Лечится добавлением хром опции 
```
--remote-allow-origins=*
```