#language:ru
@css
@webElemTesting
Функционал: проверка css свойств елемента
  
  Сценарий: проверка css свойства
    И открыть ссылку '${{url.form}}'
    И перейти на страницу 'Тестовая Форма'
    И установить переменную 'testElement' = 'Отмена'
    И установить переменную 'testProperty' = 'background-color'
    И установить переменную 'testPropertyValue' = 'rgba(0, 128, 0, 1)'
    И проверить CSS-свойства элемента 'Отмена':
      | background-color | rgba(0, 128, 0, 1) |
      | text-align       | center             |
    И проверить CSS-свойства элемента '${{testElement}}':
      | ${{testProperty}} | ${{testPropertyValue}} |
    