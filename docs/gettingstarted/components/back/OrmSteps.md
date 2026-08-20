# Модуль Orm-Steps

Модуль Orm-Steps содержит в себе шаги для работы с ORM (объектное представление БД) .

### Cостав модуля:

- configuration - работа с доступными проперти и конфигами
- utils - вспомогательные классы
- stepdefs - реализация уникальных относительно других модулей шагов

### Подключение модуля

- Добавляем зависимость:
```
implementation "ru.autotestframework.components:orm-steps:${core_version}"
```

### Заполнение проперти:

```properties
framework.orm.properties.path=путь к проперти файлу hibernate (src/test/resources/hibernate.properties)
framework.orm.package.path=полный путь к пакету с моделями БД (ru.autotestframework.models)
```

### Проперти файл с настройками hibernate:

```properties
hibernate.dialect=диалект БД (org.hibernate.dialect.H2Dialect)
hibernate.connection.driver_class=драйвер БД (org.h2.Driver)
hibernate.connection.url=url подключения (jdbc:h2:~/test)
hibernate.connection.username=логин
hibernate.connection.password=пароль
hibernate.show_sql=вывод логов (true)
```
Есть еще множество других проперти, о них можно почитать в официальной документации hibernate.

### Особенности модуля

**Модуль не умеет сам создавать объекты для создания новых записей и обновления старых в БД!**

**Необходимо со стороны проекта создать объект и подсунуть его в контекст. И в шаг уже отдать наименование переменной в контексте.**