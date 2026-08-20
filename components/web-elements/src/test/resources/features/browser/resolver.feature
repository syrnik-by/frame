#language:ru
@resolver
@browser
@webElemTesting
Функционал: проверка работы шагов с переменныим контекста
  
  Сценарий: заполнение storage
    Дано открыть ссылку 'https://gitlab-01/'
    И установить переменную 'name' = 'Иван'
    И установить переменные:
      | lastname | Иванов           |
      | age      | 20               |
      | type     | resolvedLastname |
    И добавить записи в localStorage:
      | resolvedName     | ${{name}}     |
      | resolvedLastname | ${{lastname}} |
    И проверить записи в localStorage:
      | resolvedName     | Иван   |
      | resolvedLastname | Иванов |
    И удалить записи в localStorage:
      | ${{type}} |
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И проверить поля таблицы 'Table':
      | name   | age      |
      | Bob    | ${{age}} |
      | George | 42       |