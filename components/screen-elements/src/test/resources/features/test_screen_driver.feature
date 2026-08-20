#language:ru
@screenTesting
@Skip
Функционал: проверка работы screen driver

  Сценарий: проверка работы основных элементов
    И перейти на страницу 'Другая'
    И очистить поле 'Text'
    И нажать на элемент 'OCR element offset'
    И нажать на элемент 'Text' правой кнопкой мыши
    И нажать на элемент 'Select All'
    И нажать на элемент 'File'
    И нажать на элемент 'New from context menu'
    И нажать на элемент 'File'
    И нажать на элемент 'New from context menu'
    И нажать на элемент 'File'
    И нажать на элемент 'New from context menu'
    И нажать на элемент 'File'
    И нажать на элемент 'New from context menu'
    И заполнить поле 'Text' значением 'This is how desktop automation works LUL'
    И очистить поле 'Text'
    И нажать на элемент 'Close all'
    И нажать на элемент 'New'
    И нажать на элемент 'New'
    И нажать на элемент 'New'
    И нажать на элемент 'New'
    И нажать на элемент 'Close all'
    И заполнить поле 'Text' значением 'This is how desktop automation works LUL'
    И очистить поле 'Text'
    И проверить, что элемент 'Exit' активен
    И отображается элемент 'Text'
    И нажать на элемент 'Exit'

  Сценарий: Проверка работы со скриншотами
    И перейти на страницу 'Другая'
    И записать в файл 'src/test/resources/1.png' скриншот текущего окна
    И нажать на элемент 'File'
    И нажать на элемент 'New from context menu'
    И нажать на элемент 'File'
    И нажать на элемент 'New from context menu'
    И записать в файл 'src/test/resources/2.png' скриншот текущего окна
    И записать в файл 'src/test/resources/result.png' сравнение текущего окна со скриншотом 'src/test/resources/1.png'
    И записать в файл 'src/test/resources/result1.png' сравнение скриншотов 'src/test/resources/1.png' 'src/test/resources/2.png'
    И проверить, что существует файл 'file:result.png'
    И проверить, что существует файл 'file:result1.png'
    И удалить файл по пути 'src/test/resources/1.png'
    И удалить файл по пути 'src/test/resources/2.png'
    И удалить файл по пути 'src/test/resources/result.png'
    И удалить файл по пути 'src/test/resources/result1.png'
    И нажать на элемент 'Close all'
    И нажать на элемент 'Exit'