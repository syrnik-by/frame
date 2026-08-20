# Окружение

Поддержка смены окружения реализована с
помощью [Spring Profiles](https://docs.spring.io/spring-boot/docs/1.2.0.M1/reference/html/boot-features-profiles.html)

По умолчанию окружение = `local`

Если необходимо поменять окружение, нужно изменить переменную среды `spring_profiles_active`  
В зависимости от окружения, подтягиваются разные настройки:

- `framework.properties` - подтягивается всегда

- `framework-local.properties` - подтягивается только для "local" окружения (перезаписывает framework.properties при 
  совпадениях)

- `framework-preprod.properties` - подтягивается только для "preprod" окружения

### Смена окружения

В [Gradle](https://gradle.org/) можно добавить различные таски для окружений:

```groovy
task testInTest(type: Test) {
	//Евсли у вас несколько конутров/окружений повзоляет запустить тесты в соответствующем
    doFirst {
		if (hasProperty('stand')){
            def envProfile = findProperty('stand').toString().toLowerCase()
            environment "SPRING_PROFILES_ACTIVE", envProfile
		}
    }
}

task testInPreProd(type: Test) {
    doFirst {
        environment "spring.profiles.active", "preprod"
    }
}
```

И далее уже запускать через CLI или IDE разные таски `testInTest` или `testInPreProd`.