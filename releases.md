@all #release_notes

##### Новая версия [Автотест Фреймворк](https://gitlab-01/automated-testing-technology/core_frameworks/fr-autotest-framework)  :fire:

[v3.3](https://ahcode.headoffice.psbank.local/psb-share/devops/automated-testing-technology/core-frameworks/autotest-framework/-/releases/v3.3)

**Что нового:**
- добавлена проверка и замена неразрывных пробелов при получении текста ячеек таблиц
  https://ahcode.headoffice.psbank.local/psb-share/devops/automated-testing-technology/core-frameworks/autotest-framework/-/merge_requests/86
- в модуль DocumentSteps добавлен функционал по работе с json'ами [ATTDD-1483629](https://alm.headoffice.psbank.local/sd/operator/#uuid:GMtask$125670338)

**Что исправлено:**
- исправлены методы генерации ИНН и ОГРН
  https://alm.headoffice.psbank.local/sd/operator/#uuid:GMtask$154549194
- исправлена ошибка [issue 2](https://ahcode.headoffice.psbank.local/psb-share/devops/automated-testing-technology/core-frameworks/autotest-framework/-/issues/2) работа спецсимволов в параметрах шагов
- исправлены ошибки при логгировании [AF-1027195](https://alm.headoffice.psbank.local/sd/operator/#uuid:GMtask$88682636)

- устранены различные уязвимости, выявленные в ходе аудита со стороны автоматики ДИБ (исключение зависимостей, изменение версий зависимостей) и др. рефакторинг:
- com.fasterxml.jackson.core:jackson-databind -> 2.13.1
- org.apache.commons:commons-compress -> 1.20
- com.github.jknack -> исключено
- org.yaml -> исключено
- ch.qos.logback:logback-core -> 1.2.9
- ch.qos.logback:logback-classic -> 1.2.9


**Что изменено:**
- поднята версия Spring boot до 2.6.3
  https://ahcode.headoffice.psbank.local/psb-share/devops/automated-testing-technology/core-frameworks/autotest-framework/-/merge_requests/81
- изменен js скрипт по получению текста из ячеек таблиц (убираются дубликаты значений в рамках одной ячейки)
  https://ahcode.headoffice.psbank.local/psb-share/devops/automated-testing-technology/core-frameworks/autotest-framework/-/merge_requests/81
- изменен шаг @When("нажать на {string}, чтобы скачать файл, записать путь в {string}") - убрано взаимодействие через selenide
  https://ahcode.headoffice.psbank.local/psb-share/devops/automated-testing-technology/core-frameworks/autotest-framework/-/merge_requests/86
- удален модуль генерации Junit5 тестов из Postman коллекции и вынесен в отдельную [библиотеку](https://ahcode.headoffice.psbank.local/psb-share/devops/automated-testing-technology/libraries/postman-generator) [ATTDD-1489187](https://alm.headoffice.psbank.local/sd/operator/#uuid:GMtask$126952768)
- Junit5 -> возвращении версии [5.8.2](https://alm.headoffice.psbank.local/sd/operator/#uuid:GMtask$153420053)