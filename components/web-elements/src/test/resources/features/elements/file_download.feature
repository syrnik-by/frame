#language:ru
@fileDownloading
@webElemTesting
Функционал: Проверка скачивания файла
  @Skip
  Сценарий: проверка что скачивается файл по нажатию на элемент с href в DOM
    И открыть ссылку '${{url.fileDownload}}'
    И перейти на страницу 'Форма скачивания'
    И нажать на 'direct-download-window', чтобы скачать файл, записать путь в 'pathToFile'
    И проверить, что существует файл '${{pathToFile}}'