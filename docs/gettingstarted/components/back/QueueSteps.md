## "Очереди" модуль

Данный под-модуль предоставляет готовые реализации клиентов для взаимодействия с очередями [KAFKA](https://kafka.apache.org) и [RABBITMQ](https://www.rabbitmq.com/client-libraries/java-client) 
и возможностью добавлять свои реализации клиентов


### Инициализация

<span style="color:red">
!!!ВАЖНО!!! весь необходимый список параметров подключения требуется уточнять у разработчика 
</span>

#### В рамках работы с RABBITMQ можно задавать данные для подключения 2-мя способами:

1) c помощью properties 

RABBITMQ (classpath:rabbitmq.properties)
```properties
framework.queues.rabbitmq.host
framework.queues.rabbitmq.port
framework.queues.rabbitmq.username:
framework.queues.rabbitmq.password:
framework.queues.rabbitmq.queue.name:
```

2) с помощью DataTable cucumber шага установки подключения
```gherkin
#language:ru
И установить подключение к серверу RABBITMQ:
  | host       | rabbitmq.host      |
  | port       | rabbitmq.port      |
  | username   | rabbitmq.username  |
  | password   | rabbitmq.password  |
  | queue.name | rabbitmq.queueName |

И установить подключение к серверу QUEUE:
  | property.key      | property.value      |
  | property.key1     | property.value1     |
```

Так-же доступна возможность часть данных подключения хранить в properties а часть задавать в шаге 

#### В рамках работы с KAFKA можно задавать данные для подключения 1 способом:

KAFKA (classpath:kafka.properties)
```properties
spring.kafka.bootstrap-servers:
spring.kafka.consumer.group-id:
spring.kafka.consumer.key-deserializer:
spring.kafka.consumer.value-deserializer:
spring.kafka.consumer.auto-offset-reset:earliest:
spring.kafka.listener.poll-timeout:
spring.kafka.producer.key-serializer:
spring.kafka.producer.value-serializer:

spring.kafka.security.protocol:
spring.kafka.ssl.key-store-type:
spring.kafka.ssl.key-store-location:
spring.kafka.ssl.key-store-password:
spring.kafka.ssl.trust-store-type:
spring.kafka.ssl.trust-store-location:
spring.kafka.ssl.trust-store-password:
```

### Добавление клиента в модуль 

Для добавления своего клиента для очереди требуется задекларировать:

1) Класс клиента (ru.autotestframework.queue)

CustomQueueClient - это тестовое имя (можно использовать любое) , кукумбер шаг инициализации проверять метод getName()
```java
public class CustomQueueClient implements QueueClient {
    @Override
    public String getName() {
        return "CustomQueueClient";
    }

    @Override
    public QueueClient init(Map<String, String> map) {
        return this;
    }

    @Override
    public void sendMessage(String s) {
//Код отправки сообщения используя CustomQueueClient
    }

    @Override
    public void findMessage(String s) {
//Код вычитывания сообщения используя CustomQueueClient
    }
}
```

2) Создание конфигурации (ru.autotestframework.queue)

В данной конфигурации вы можете под свои потребности наполнять List<QueueClient> в queue-steps

Примечание: 
- @Primary у бина для того что бы использовался ваш бин
- @Autowired если вы хотите использовать дефолтные properties для KAFKA и RABBITMQ 
- По необходимости можно добавлять свои кастомные properties как для кастомных клиентов так и для KAFKA и RABBITMQ

ПРИМЕР: добавление своего класс клиента 
```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class QueueConfig{
    
    @Autowired
    private RabbitMqProperties rabbitMqProperties;

    @Bean
    @Primary
    public List<QueueClient> queueClients() {
        return List.of(new RabbitMqClient(rabbitMqProperties), new CustomQueueClient());
    }
}
```
### Пример Cucumber-шагов

```gherkin

Сценарий: Отправка сообщения в RabbitMq
Когда установить подключение к серверу 'RABBITMQ':
| host     | host     |
| port     | port     |
| username | username |
| password | password |
И отправить сообщение:
"""
Тестовое сообщение
"""
  
Сценарий: Отправка собщения с помощью клиента
Когда установить подключение к серверу 'CustomQueueClient':
| property.key  | property.value  |
| property.key1 | property.value1 |
И отправить сообщение:
"""
Тестовое сообщение
"""
```

Все шаги представлены в классах:
[QueueSteps](../../../../components/queue-steps/src/main/java/ru/psb/autotestframework/queue_steps/QueueSteps.java)
[KafkaSteps](../../../../components/queue-steps/src/main/java/ru/psb/autotestframework/queue_steps/KafkaSteps.java)
[KafkaRecordSteps](../../../../components/queue-steps/src/main/java/ru/psb/autotestframework/queue_steps/KafkaRecordSteps.java)