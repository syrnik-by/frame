#language:ru

@SQLDemo
Функция: Тестирование шагов SQL
  
  @SQLSelectDemo
  Сценарий: Тест сценария SELECT на симуляции драйвера БД
    И установить подключение к БД:
      | url                       | user | password | dbms   |
      | data/fake_select_sql.json | user | password | ORACLE |
    И отправить в БД методом SELECT SQL запрос:
    """
    SELECT * FROM TABLE QQ WHERE id = 12
    """
  
  @SQLUpdateDemo
  Сценарий: Тест сценария UPDATE на симуляции драйвера БД
    И установить подключение к БД:
      | url                       | user | password | dbms   |
      | data/fake_update_sql.json | user | password | ORACLE |
    И отправить в БД методом EXECUTE SQL запрос:
    """
    UPDATE qq
    SET t1 = 'test'
    WHERE t2 = 'not test'
    """
    
  @SQLFileDemo
  Сценарий: Тест сценария на отправку запроса из файла
    И установить подключение к БД:
      | url                       | user | password | dbms   |
      | data/fake_update_sql.json | user | password | ORACLE |
    И отправить в БД методом EXECUTE SQL запрос из файла 'data/sql/test.sql'
    
  @SQLConnector
  Сценарий: Тест сценария работы коннектора на симуляции драйвера БД
    И создать коннектор c TestName к БД:
      | url                       | user | password | dbms   |
      | data/fake_update_sql.json | user | password | ORACLE |
    И отправить в БД коннектором TestName методом SELECT SQL запрос:
    """
    SELECT * FROM TABLE QQ WHERE id = 12
    """
    
  @SQLFileDemo
  Сценарий: Тест сценария на отправку запроса из файла c коннектором
    И создать коннектор c 'SQLServer' к БД:
      | url                       | user | password | dbms   |
      | data/fake_update_sql.json | user | password | ORACLE |
    И отправить в БД коннектором 'SQLServer' методом EXECUTE SQL запрос из файла 'data/sql/test.sql'
    
  @SQLConnector
  Сценарий: Тест сценария работы нескольких коннекторов на симуляции драйвера БД
    И создать коннектор c TestName к БД:
      | url                       | user | password | dbms   |
      | data/fake_update_sql.json | user | password | ORACLE |
    И создать коннектор c TestName2 к БД:
      | url                       | user | password | dbms   |
      | data/fake_update_sql.json | user | password | ORACLE |
    И создать коннектор c TestName2 к БД:
      | url                       | user | password | dbms   |
      | data/fake_update_sql.json | user | password | ORACLE |
    И создать коннектор c AnotherDB к БД:
      | url                       | user | password | dbms   |
      | data/fake_select_sql.json | user | password | ORACLE |
  
    И отправить в БД коннектором TestName методом SELECT SQL запрос:
    """
    SELECT * FROM TABLE QQ WHERE id = 12
    """
    И отправить в БД коннектором TestName2 методом SELECT SQL запрос:
    """
    SELECT * FROM TABLE QQ WHERE id = 12
    """
    И отправить в БД коннектором AnotherDB методом SELECT SQL запрос:
    """
    UPDATE qq
    SET t1 = 'test'
    WHERE t2 = 'not test'
    """