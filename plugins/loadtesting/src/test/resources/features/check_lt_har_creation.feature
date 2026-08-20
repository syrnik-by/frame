#language:ru
@LoadTesting
Функция: Проверка работы записи HAR для плагина АТ НТ
  @workItemIds=1179114
  Сценарий: Проверка работы LoadTesting
    * ✽ Шаг номер '1' ✽
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
    * обновить текущую страницу
    * ✽ Шаг номер '2' ✽
    * открыть ссылку 'https://newton.psbank.ru/'
    * открыть ссылку 'https://gitlab-01'