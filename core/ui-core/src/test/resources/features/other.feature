#language:ru
@combineDriversTesting
Функция: Проверка работы десктопа и веба
  
  Сценарий: Интеграционный сценарий
    И перейти на страницу 'Другая'
    И очистить поле 'Text2'
    И установить переменную 'typed' = 'This is how desktop automation works LUL'
    
    И заполнить поле 'Text' значением 'This is how desktop automation works LUL'
    И очистить поле 'Text2'
    И заполнить поле 'Text2' значением 'This is how desktop automation works LUL'
  
    И проверить, что текст '${{typed}}' отображается

    И перейти в браузер по ссылке 'https://gitlab-01'
    Когда добавить cookies :
      | Имя cookie 1 | Значение cookie 1 |
      | Имя cookie 2 | Значение cookie 2 |
    Тогда проверить cookies :
      | Имя cookie 1 | Значение cookie 1 |
    И удалить cookies :
      | Имя cookie 1 |
      | Имя cookie 2 |
    
    И перейти на страницу 'Gitlab Login Page'
    И заполнить поле 'Username' значением '${{fakerRu:Name.first_name}}'
    И заполнить поле 'Password' значением '${{fakerRu:ogrn}}'
    И вернуться к десктоп-приложению
    И перейти на страницу 'Другая'
    И нажать на элемент 'Text'
    И проверить, что текст 'LUL' отображается
    И проверить, что текст 'Web automation works LULT' не отображается
    И проверить многострочный текст:
    """
    ${{typed}}
    """
    И очистить поле 'Text2'
    И нажать на элемент 'Exit'