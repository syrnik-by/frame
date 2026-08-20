package ru.autotestframework.sql.db;

import static ru.autotestframework.sql.db.DslContextHolder.getDslContext;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.autotestframework.core.FileLoaderImpl;

@Slf4j
@Component
public class DBQueryExecutor {

    private DSLContext dslContext;

    @Autowired
    private FileLoaderImpl fileLoader;

    public DBQueryExecutor connect(ConnectionSupplier supplier) {
        this.dslContext = getDslContext(supplier);
        return this;
    }

    /**
     * Выполняет SQL запрос типа SELECT в виде строки
     * и возвращает первую строку результата
     *
     * @param query sql запрос в виде строки
     * @return первую строку результата
     */
    @Step("Выполнить SQL запрос '{query}' и получить первую запись из ответа")
    public Map<String, Object> executeQuerySelectFirstRow(String query) {
        return executeQuerySelectAllRows(query).get(0);
    }

    /**
     * Выполняет SQL запрос типа SELECT из sql файла
     * и возвращает первую строку результата
     *
     * @param sqlFilePath путь к файлу с запросом
     * @return первую строку результата
     */
    @Step("Выполнить SQL запрос из файла '{sqlFilePath}' и получить первую запись из ответа")
    public Map<String, Object> executeQuerySelectFirstRow(Path sqlFilePath) {
        return executeQuerySelectAllRows(sqlFilePath).get(0);
    }

    /**
     * Выполняет SQL запрос типа SELECT в виде строки
     *
     * @param query sql запрос в виде строки
     * @return результат выполнения SQL-запроса в виде списка мап
     */
    @Step("Выполнить SQL запрос '{query}' и получить список записей из ответа")
    public List<Map<String, Object>> executeQuerySelectAllRows(String query) {
        Allure.addAttachment("SQL request", query);
        log.info("SQL request:\n{}", query);

        var result = dslContext.fetch(query);

        Allure.addAttachment("SQL response", result.toString());
        log.info("SQL response:\n{}", result);
        return convertResultToMap(result);
    }

    /**
     * Выполняет SQL запрос типа SELECT из sql файла
     *
     * @param sqlFilePath путь к запросу
     * @return результат выполнения SQL-запроса в виде списка мап
     */
    @Step("Выполнить SQL запрос из файла '{sqlFilePath}' и получить список записей из ответа")
    public List<Map<String, Object>> executeQuerySelectAllRows(Path sqlFilePath) {
        String query = fileLoader.readFileAsString(String.valueOf(sqlFilePath));
        return executeQuerySelectAllRows(query);
    }

    /**
     * Конвертирует полученный результат из SELECT запроса в список мап, чтобы извлекать значение по имени столбца
     *
     * @param result полученный после запроса результат
     */
    private List<Map<String, Object>> convertResultToMap(Result<Record> result) {
        List<Map<String, Object>> rowsList = new ArrayList<>();
        for (Record currentRecord : result) {
            Map<String, Object> rowMap = new HashMap<>();
            for (Field<?> field : currentRecord.fields()) {
                rowMap.put(field.getName(), currentRecord.getValue(field));
            }
            rowsList.add(rowMap);
        }
        return rowsList;
    }
}
