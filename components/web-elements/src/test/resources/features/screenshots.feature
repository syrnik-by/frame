#language:ru
@screenshots
@webElemTesting
Функционал: работа со скриншотами

  Сценарий: Проверка работы со скриншотами
    И открыть ссылку '${{url.dragAndDropHtml5}}'
    И перейти на страницу 'Drag and Drop Examples'
    И записать в файл 'src/test/resources/1.png' скриншот текущего окна
    И переместить элемент 'Drag A' на место элемента 'Drag B'
    И записать в файл 'src/test/resources/2.png' скриншот текущего окна
    И записать в файл 'src/test/resources/result.png' сравнение текущего окна со скриншотом 'src/test/resources/1.png'
    И записать в файл 'src/test/resources/result1.png' сравнение скриншотов 'src/test/resources/1.png' 'src/test/resources/2.png'
    И проверить, что существует файл 'file:result.png'
    И проверить, что существует файл 'file:result1.png'
    И удалить файл по пути 'src/test/resources/1.png'
    И удалить файл по пути 'src/test/resources/2.png'
    И удалить файл по пути 'src/test/resources/result.png'
    И удалить файл по пути 'src/test/resources/result1.png'
