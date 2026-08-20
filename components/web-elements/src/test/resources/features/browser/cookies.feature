#language:ru
@cookies
@webElemTesting
@browser
Функция: Проверка работы с печеньем
  #реальная страница так как иначе куки не проставляются
  Сценарий: проверка работы с печеньем
    И открыть ссылку 'https://gitlab-01/'
    Когда добавить cookies :
      | Имя cookie 1 | Значение cookie 1 |
      | Имя cookie 2 | Значение cookie 2 |
    И открыть ссылку 'https://gitlab-01/'
    Тогда проверить cookies :
      | Имя cookie 1 | Значение cookie 1 |
      | Имя cookie 2 | Значение cookie 2 |
    И открыть ссылку 'https://gitlab-01/'
    И удалить cookies :
      | Имя cookie 1 |
      | Имя cookie 2 |
    И открыть ссылку 'https://gitlab-01/'
	И обновить текущую страницу
    И удалить все cookies

  Сценарий: проверка заголовков cookies
    И открыть ссылку 'https://gitlab-01/'
    И добавить cookies :
      | cookie_name | cookie_value |
    И проверить заголовок cookies :
      | _gitlab_session |
      | cookie_name     |