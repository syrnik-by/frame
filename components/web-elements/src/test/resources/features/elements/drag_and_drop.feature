#language:ru
@dragAndDrop
@webElemTesting
Функционал: проверка перемещения элементов
  
  Сценарий: проверка что элемент перемещается html5 (js)
    И открыть ссылку '${{url.dragAndDropHtml5}}'
    И перейти на страницу 'Drag and Drop Examples'
    И проверить заполнение поля 1 из списка 'Drag Elements' значением 'A'
    И переместить элемент 'Drag A' на место элемента 'Drag B'
    И проверить заполнение поля 1 из списка 'Drag Elements' значением 'B'
    
  Сценарий: проверка что элемент перемещается html4 (Actions)
    И открыть ссылку '${{url.dragAndDrop}}'
    И перейти на страницу 'Drag and Drop'
    И проверить заполнение поля 'Drop left' значением 'Drop here'
    И переместить элемент 'Drag left' на место элемента 'Drop left'
    И проверить заполнение поля 'Drop left' значением 'Dropped!'
    И переместить элемент 'Drag right' на место элемента 'Drop left'
    И проверить заполнение поля 'Drop left' значением 'Get Off Me!'