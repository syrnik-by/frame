package ru.autotestframework.queue_steps;

import static ru.autotestframework.cucumber.type.CucumberTypesDefinition.TABLE_CONVERTER;
import static ru.autotestframework.util.Validator.checkThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.core.PlaceholderResolver;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.core.descriptions.annotation.Parameter;
import ru.autotestframework.cucumber.core.descriptions.annotation.Sample;
import ru.autotestframework.queue_steps.clients.QueueClient;

/**
 * The type Queue steps.
 */
@Slf4j
@RequiredArgsConstructor
@Description("Queue")
public class QueueSteps {

    private final PlaceholderResolver resolver;

    @Autowired
    private List<QueueClient> allClients;

    private QueueClient client;

    /**
     * Connect.
     *
     * @param queueName the queue name
     * @param dataTable the data table
     */
    @When("установить подключение к серверу {resolvable_string}:")
    @Sample("Установить подключение к серверу по параметрам")
    @Parameter(type = "resolvable_string", name = "Выбор брокера сообщений")
    @Parameter(
            type = ":",
            name = "список параметров (параметров подключения может быть большой список,\n"
                    + " требуется уточнять у разработчика весь необходимый список)")
    @Example(
            example = "И установить подключение к серверу RABBITMQ:"
                    + "| host     | rabbitmq.host      |"
                    + "| port     | rabbitmq.port      |"
                    + "| username | rabbitmq.username  |"
                    + "| password | rabbitmq.password  |"
                    + "| queue    | rabbitmq.queueName |")
    public void connect(String queueName, final DataTable dataTable) {
        client = null;
        initClient(queueName, resolveProperties(dataTable));
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    @When("отправить сообщение:")
    @Sample("Отправить сообщение")
    @Parameter(type = ":", name = "сообщение")
    @Example(example = "И отправить сообщение:" + "\"\"\"" + "message" + "\"\"\"")
    public void sendMessage(final String message) {
        client.sendMessage(message);
    }

    /**
     * Find message.
     *
     * @param message the message
     */
    @When("найти сообщение:")
    @Sample("Найти сообщение")
    @Parameter(type = ":", name = "сообщение")
    @Example(example = "И найти сообщение:" + "\"\"\"" + "message" + "\"\"\"")
    public void findMessage(final String message) {
        client.findMessage(message);
    }

    private void initClient(String queueName, Map<String, String> properties) {
        List<String> allClientName = new ArrayList<>();
        allClients.forEach(temp -> {
            String tempName = temp.getName();
            allClientName.add(tempName);
            if (tempName.equals(queueName)) {
                client = client.init(properties);
            }
        });
        if (client == null) {
            throw new AutotestException(
                    "Не найдена очередь с именем '{}' , Доступные очереди : '{}'", queueName, allClientName);
        }
    }

    private Map<String, String> resolveProperties(DataTable dataTable) {
        checkThat(dataTable.height() == 2, "Table should have only 2 rows: Keys and Value");
        Map<String, String> unresolvedProperties = TABLE_CONVERTER.toMap(dataTable, String.class, String.class);
        return resolver.resolve(unresolvedProperties);
    }
}
