#language:ru

@Demo
Функция: Демонстрация возможностей HTTP

  @EndpointConfigure
  Сценарий: Конфигурация эндпоинта
    Дано запустить сервер заглушек
    И адрес сервера 'https://localhost:8484'
    Когда отправить GET запрос
    Тогда проверить в заглушке запрос '/', отправленный методом GET
    Дано эндпоинт '/some/path'
    Когда отправить GET запрос
    Тогда проверить в заглушке запрос '/some/path', отправленный методом GET
    Дано эндпоинт '/another/path'
    Когда отправить GET запрос
    Тогда проверить в заглушке запрос '/another/path', отправленный методом GET
    И очистить в заглушке историю запросов
    И очистить в заглушке маппинги
    И остановить сервер заглушек

  @HeaderConfigure
  Сценарий: Конфигурация заголовков
    Дано запустить сервер заглушек
    И адрес сервера 'https://localhost:8484/some/path'
    И установить header'ы:
      | header1      | header value 1           |
      | Content-Type | application/octet-stream |
    Когда отправить GET запрос
    Тогда проверить в заглушке запрос '/some/path', отправленный методом GET
    И этот запрос содержит заголовки:
      | header1      | header value 1                          |
      | Content-Type | application/octet-stream; charset=UTF-8 |
    Дано установить header'ы:
      | header1      | header value 11 |
      | Content-Type | text/html       |
    Когда отправить GET запрос
    Тогда этот запрос содержит заголовки:
      | header1      | header value 1           |
      | header1      | header value 11          |
      | Content-Type | text/html; charset=UTF-8 |
    Дано это SOAP запрос
    Когда отправить GET запрос
    Тогда этот запрос содержит заголовки:
      | Content-Type | text/xml; charset=UTF-8 |
    И очистить в заглушке историю запросов
    И очистить в заглушке маппинги
    И остановить сервер заглушек.

  @QueryConfigure
  Сценарий: Конфигурация параметров запроса
    Дано запустить сервер заглушек
    И адрес сервера 'https://localhost:8484/some/path'
    И установить query parameters:
      | param1 | value 1 |
      | param2 | value 2 |
    Когда отправить GET запрос
    Тогда проверить в заглушке запрос '/some/path\?param1=value%201&param2=value%202', отправленный методом GET

    И очистить в заглушке историю запросов
    И очистить в заглушке маппинги
    И остановить сервер заглушек.

  @FormConfigure
  Сценарий: Конфигурация формы
    Дано запустить сервер заглушек
    И адрес сервера 'https://localhost:8484/some/path'
    И установить form parameters:
      | form_1 | value 1 |
      | form_2 | value 2 |
    Когда отправить POST запрос
    Тогда проверить в заглушке запрос '/some/path', отправленный методом POST
    И этот запрос содержит в body строку:
    """
    form_1=value%201&form_2=value%202
    """
    И очистить в заглушке историю запросов
    И очистить в заглушке маппинги
    И остановить сервер заглушек.

  @CookieConfigure
  Сценарий: Конфигурация печенек
    Дано запустить сервер заглушек
    И адрес сервера 'https://localhost:8484/some/path'
    И установить cookies:
      | cookie_1 | param_1 |
      | cookie_2 | param_2 |
    Когда отправить GET запрос
    Тогда проверить в заглушке запрос '/some/path', отправленный методом GET

    И этот запрос содержит cookies:
      | cookie_1 | param_1 |
      | cookie_2 | param_2 |
    И очистить в заглушке историю запросов
    И очистить в заглушке маппинги
    И остановить сервер заглушек.

  @GetFile
  Сценарий: Проверка загрузки файла из ответа
    # запуск мока
    Дано запустить сервер заглушек
    И загрузить в заглушку маппинг:
    """
      {
        "request": {
          "method": "GET",
          "url": "/get-file"
        },
        "response": {
          "status": 200,
          "bodyFileName": "file.pdf",
          "headers": {
            "Content-Type": "application/pdf",
            "Content-Disposition": "attachment;filename=\"file.pdf\""
          }
        }
      }
    """
    # тест
    И адрес сервера 'https://localhost:8484/get-file'
    Когда отправить GET запрос
    И статус ответа = 200
    И статус ответа > '170'
    И статус ответа < '201'
    И статус ответа <= '200'
    И статус ответа >= '200'
    И статус ответа inBetween '200, 204'
    И статус ответа inBetween '[200, 204]'
    И статус ответа #custom# '123'
    И статус ответа #notEqual# '323'
    И сохранить из ответа файл с именем 'temp.pdf', записать путь в переменную 'filePath'
    И проверить, что существует файл '${{filePath}}'

     # проверка запроса на стороне сервера-мока
    Тогда проверить в заглушке запрос '/get-file', отправленный методом GET
     # отключение мока

    И очистить в заглушке историю запросов
    И очистить в заглушке маппинги
    И остановить сервер заглушек.

  @SendJsonFile
  Сценарий: Проверка отправки body из текстового файла (xml/json)
    # запуск и конфигурация мока
    Дано запустить сервер заглушек
    И загрузить в заглушку маппинг:
    """
      {
        "request": {
          "method": "POST",
          "url": "/post-json"
        },
        "response": {
          "status": 200,
          "body": "{\"id\": 1}",
          "headers": {
            "Content-Type": "application/pdf",
            "Content-Disposition": "attachment;filename=\"file.pdf\""
          }
        }
      }
    """
    # подготовка контекстных переменных
    Дано установить переменные:
      | contextId     | 12        |
      | contextString | somevalue |
    # тест
    И адрес сервера 'https://localhost:8484/post-json'
    И запрос содержит body 'data/json/example.json'
    Когда отправить POST запрос
    И статус ответа = 200

    # проверка запроса на стороне сервера-мока

    Тогда проверить в заглушке запрос '/post-json', отправленный методом POST
    И этот запрос содержит в body строку:
    """
    ${{file:data/json/example.json}}
    """

    И этот запрос содержит body:
      | id            | ==           | ${{contextId}}     |
      | content.name  | ==           | ${{contextString}} |
      | content.value | ==           | someText           |
      | content.value | notNull      |                    |
      | content.vasya | isNull       |                    |
      | content.value | matchesRegex | .*                 |
      | content.value | contains     | omeTex             |

    # отключение мока
    И очистить в заглушке историю запросов
    И очистить в заглушке маппинги
    И остановить сервер заглушек.
