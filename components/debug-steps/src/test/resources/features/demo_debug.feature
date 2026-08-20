#language:ru

@DebugDemo
Функция: Выполнение debug шагов

  Сценарий: Выполнение шагов c контекстом
    И установить переменные:
      | param1 | ${{fakerRu:Name.first_name}} |
      | param2 | ${{fakerRu:ogrn}}  |
      | param3 | ${{secret:${{param1}}}} |
    И вывести в лог переменные из контекста
    И вывести в лог строку '${{secret:${{param1}}}}'
    И вывести в лог строку '${{param1}}'
    И тестим таблицу:
      | param 1 | ${{secret:${{param1}}}} |
      | param 2 | ${{param2}} |
      | param 1 | ${{param1}} |

  Сценарий: Выполнение debug шагов
    И вывести в лог переменные из контекста
    И вывести в лог строку 'Выполнение debug шагов'
    И тестим таблицу:
      | param 1 | value 1 |
      | param 2 | value 2 |

