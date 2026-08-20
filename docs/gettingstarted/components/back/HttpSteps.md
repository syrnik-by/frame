## HTTP модуль

Данный под-модуль позволяет отправлять запросы по протоколу [HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP)
Реализован с использованием библиотеки [Rest-Assured](https://rest-assured.io)

### Пример выполнения запроса на Cucumber-шагах

```gherkin
#language:ru
Когда адрес сервера '${{clientUrl}}'
И эндпоинт '/api/path/${{docId}}'
И установлены header'ы:
| accept        | application/json |
| Authorization | ${{userToken}}   |
| Content-Type  | application/xml  |
И установить query parameters:
| id | ${{id}} |
И установить cookies:
| someCookie | someCookieValue |
И запрос содержит body 'data/json/request.xml'
И отправлен POST запрос
Тогда ответ не пустой
И статус ответа = 200
```

### Извлечение переменных

Извлечь переменные из тела JSON можно с использованием шага
```gherkin
#language:ru
И получить переменные из body:
| itemId | items[0].status.id |
И сохранить из ответа файл с именем 'filename.pdf', записать путь в переменную 'temp/http'
```
### Валидация ответа

HTTP-ответ можно валидировать следующими шагами:
```gherkin
#language:ru
Тогда ответ не пустой
И статус ответа = 200

# ответ JSON
И ответ содержит JSON:
"""
{"status": 1}
"""
# путь к файлу содержащему тело которое ожидается
И ответ содержит JSON 'data/json/awaitedLetterResponse.json'
# ожидаемая json схема (есть аналогичный шаг для XSD)
И ответ соответствует JSON схеме 'data/json/schemas/item_status_scheme.json'
  
# валидация полей XML/JSON ответа .. ** подробнее ниже
И ответ содержит body:
| Envelope.Body.DivideResponse.DivideResult.text() | !=             | "1"                 |
| Envelope.Body.DivideResponse.DivideResult.text() | containsString | "${{divideResult}}" |

```
** Описание полей таблицы шага валидации JSON/XML
- первая колонка - [GPath](https://groovy-lang.org/processing-xml.html#_gpath) локатор, позволяет обращаться к элементу тела(XML, JSON)

- вторая колонка - оператор сравнения (все операторы см. в пакете *.cucumber.parser)

- третья колонка - операнд(значение). Тип операнда определяется по формату записи:

```gherkin
| "1"        | String    |
| 1          | Integer   |
| 1.0        | Float     |
| ["1", ""]  | String[]  |
| [1, 0]     | Integer[] |
| [1.0, 2.0] | Float[]   |
```

### Список шагов Http (Rest-Assured)

Полный список представлен в классе [Steps](../../../../components/http-steps/src/main/java/ru/psb/autotestframework/http_steps/HttpSteps.java)

### Конфигурирования дефолтных значений для Specification

Спецификацию Rest-Assured можно сконфигурировать реализуя интерфейс SpecificationConfigurer, установить дефолтные заголовки, Адрес сервера,
необходимость url-энкодирования и т.д.

```Java 
@RequiredArgsConstructor
@Component
public class RestAssuredConfigurator implements SpecificationConfigurer {

    public RequestSpecification configure(RequestSpecification specification) {
        specification.baseUri("https://someurl.ru");
        specification.header(new Header("accept", "application/json"));
        return specification.urlEncodingEnabled(false);
    }
}
```

## Заглушки - MockService

Под-модуль позволяет разворачивать заглушки в виде отдельного сервера. 
Применим когда есть возможность: 
- Сконфигурировать окружение;
- Перенаправить запросы в заглушку:
  - Используя средства DevTools селениума:
  - Используя возможность внедрения своего кода в код разработчиков фронтэнда.


Основан на библиотеке [WireMock](https://wiremock.org/docs/stubbing)

Полный список шагов представлен в
классе [WireMockSteps](../../../../components/http-steps/src/main/java/ru/psb/autotestframework/http_steps/WireMockSteps.java)