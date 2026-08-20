#language:ru
@table
@webElemTesting
Функционал: table

  Сценарий: проверка, что таблица не содержит дубли
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И проверить, что таблица 'Table' не содержит повторений

  Сценарий: проверка, что таблица не пустая
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И проверить, что таблица 'Table' не пустая
  
  Сценарий: проверка очистки кэша
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И проверить, что таблица 'Table' не пустая
    И очистить табличный кэш
    И проверить, что таблица 'Table' не пустая
    
  Сценарий: проверка, что таблица пустая
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И нажать на элемент с текстом 'Table Data'
    И заполнить поле 'Text Area' значением '[{"name" : null, "age" : null}]'
    И нажать на элемент с текстом 'Refresh Table'
    И установить ожидание 3 секунды
    И проверить, что таблица 'Table' пустая

  Сценарий: проверка полей таблицы
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И проверить поля таблицы 'Table':
      | name   | age |
      | Bob    | 20  |
      | George | 42  |
    И проверить поля таблицы 'Table':
      | age | name   |
      | 20  | Bob    |
      | 42  | George |

  Сценарий: проверка полей таблицы при отсутствии столбцов в dataTable
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И нажать на элемент с текстом 'Table Data'
    И заполнить поле 'Text Area' значением '[{"name" : "Jack", "age" : 33, "status" : "online"}]'
    И нажать на элемент с текстом 'Refresh Table'
    И проверить поля таблицы 'Table':
      | status | age |
      | online | 33  |

  Сценарий: проверка полей таблицы при поиске строки не в начале таблицы
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И нажать на элемент с текстом 'Table Data'
    И заполнить поле 'Text Area' значением '[{"name" : "Jack", "age" : 33, "status" : "online"}, {"name" : "Oonie", "age" : 11, "status" : "off"}]'
    И нажать на элемент с текстом 'Refresh Table'
    И проверить поля таблицы 'Table':
      | status | age |
      | off    | 11  |

  Сценарий: проверка, что таблица не содержит элементы
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И проверить, что таблица 'Table' не содержит:
      | name | age | status |
      | Jack | 33  | online |
    И проверить, что таблица 'Table' не содержит:
      | name | age |
      | Bob  | 33  |

  Сценарий: проверка счётчика столбцов и строк
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И нажать на элемент с текстом 'Table Data'
    И заполнить поле 'Text Area' значением '[{"name" : "Jack", "age" : 33, "status" : "online"}]'
    И нажать на элемент с текстом 'Refresh Table'
    И установить ожидание 3 секунды
    И проверить, что число столбцов в таблице 'Table' равно 3
    И проверить, что число строк в таблице 'Table' равно 1

  Сценарий: проверка сохранения столбца по ключу
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И сохранить в таблице 'Table' столбец 'name' по ключу 'key'
    И переменные имеют значения:
      | key | == | {cells}name:Bob; name:George |

  Сценарий: проверка сохранения строки по ключу
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И сохранить в таблице 'Table' строку '2' по ключу 'key'
    И переменные имеют значения:
      | key | == | {cells}name:George; age:42 |

  Сценарий: проверка вхождения столбца по ключу
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И сохранить в таблице 'Table' столбец 'name' по ключу 'key'
    И проверить, что в таблице 'Table' содержится столбец из переменной 'key'

  Сценарий: проверка вхождения строки по ключу
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И сохранить в таблице 'Table' строку '2' по ключу 'key'
    И проверить, что в таблице 'Table' содержится строка из переменной 'key'

  Сценарий: проверка сохранения ячейки по ключу
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И сохранить в таблице 'Table' ячейку в строке '2' и столбце 'age' по ключу 'key'
    И переменные имеют значения:
      | key | == | 42 |

  Сценарий: проверка сохранения ячейки в переменную
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И из таблицы 'Table' сохранить значение из столбца 'age' в переменную 'key' из строки с данными:
      | name   |
      | George |
    И переменные имеют значения:
      | key | == | 42 |

  Сценарий: проверка выбора строк и столбцов в таблице
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И выбрать все строки таблицы 'Table'
    И выбрать в таблице 'Table' строку '1'
    И выбрать в таблице 'Table' строку '2'
    И выбрать в таблице 'Table' столбец 'age'
    И в таблице 'Table' выделить строку с данными:
      | name   |
      | George |

  Сценарий: проверка наличия столбца в таблице
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И проверить, что таблица 'Table' содержит столбец 'name'

  Сценарий: проверка кликов по элементу в столбце таблицы
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И в таблице 'Table' кликнуть на элемент в столбце 'name' с данными:
      | name |
      | Bob  |
    И в таблице 'Table' дважды кликнуть на элемент в столбце 'name' с данными:
      | name |
      | Bob  |
    И в таблице 'Table' нажать на элемент в столбце 'name' с данными:
      | name   |
      | George |
    И в таблице 'Table' дважды нажать на элемент в столбце 'name' с данными:
      | name   |
      | George |

  Сценарий: проверка кликов по элементу в строке таблицы
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И в таблице 'Table' кликнуть по ячейке в строке '2' и столбце 'age'
    И в таблице 'Table' дважды кликнуть по ячейке в строке '2' и столбце 'age'
    И в таблице 'Table' нажать по ячейке в строке '2' и столбце 'age'
    И в таблице 'Table' дважды нажать по ячейке в строке '2' и столбце 'age'

  @Skip
  Сценарий: проверка полей таблицы (большая)
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И нажать на элемент 'Table Data'
    И заполнить поле 'Text Area' значением '[{"name" : "Bob", "age" : 20, "lbs" : 145}, {"name": "George", "age" : 42, "lbs" : 145}, {"name": "Vasya", "age" : 12, "lbs" : 145},{"name": "Peter", "age" : 46, "lbs" : 145},{"name": "Norman", "age" : 64, "lbs" : 145},{"name": "Olesya", "age" : 25, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145},{"name": "Akakii", "age" : 145, "lbs" : 145},{"name": "Sergey", "age" : 41, "lbs" : 145},{"name": "Neoklet", "age" : 2565, "lbs" : 145}]'
    И нажать на элемент 'Refresh Table'
    И проверить поля таблицы 'Table':
      | name   | age |
      | Bob    | 20  |
      | George | 42  |
    И проверить поля таблицы 'Table':
      | age | name   |
      | 20  | Bob    |
      | 42  | George |

  Сценарий: проверка, что меняется контекст при переходе со страницы
    И открыть ссылку '${{url.tablePage}}'
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И проверить, что таблица 'Table' не пустая
    И перейти на страницу 'Dynamic HTML TABLE e2e'
    И нажать на элемент с текстом 'Table Data'
    И заполнить поле 'Text Area' значением '[{"name" : null, "age" : null}]'
    И нажать на элемент с текстом 'Refresh Table'
    И установить ожидание 1 секунды
    И проверить, что таблица 'Table' пустая
