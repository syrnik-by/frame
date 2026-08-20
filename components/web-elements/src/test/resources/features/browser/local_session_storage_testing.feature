#language:ru
@storage
@browser
@webElemTesting
Функция: Проверка работы с storage

  Сценарий: Проверка работы с localStorage
    Дано открыть ссылку 'https://gitlab-01/'
    И установить переменную 'local 2' = 'Значение local 2'
    И добавить записи в localStorage:
      | Имя local 1 | Значение local 1 |
      | Имя local 2 | ${{local 2}}     |
    Тогда проверить записи в localStorage:
      | Имя local 1 | Значение local 1 |
      | Имя local 2 | ${{local 2}}     |
    И удалить записи в localStorage:
      | Имя local 1 |
      | Имя local 3 |
    Тогда проверить записи в localStorage:
      | Имя local 2 | ${{local 2}} |
    И удалить все записи в localStorage

  Сценарий: Проверка работы с sessionStorage
    Дано открыть ссылку 'https://gitlab-01/'
    И установить переменную 'session 2' = 'Значение session 2'
    И добавить записи в sessionStorage:
      | Имя session 1 | Значение session 1 |
      | Имя session 2 | ${{session 2}}     |
    Когда проверить записи в sessionStorage:
      | Имя session 1 | Значение session 1 |
      | Имя session 2 | ${{session 2}}     |
    Тогда удалить записи в sessionStorage:
      | Имя session 1 |
      | Имя session 2 |
    И удалить все записи в sessionStorage