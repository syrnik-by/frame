package ru.autotestframework.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.it.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.context.annotation.Description;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.models.TestObject;
import ru.autotestframework.orm_steps.step_defs.ORMSteps;

/**
 * Test steps.
 */
@Slf4j
@RequiredArgsConstructor
@Description("Шаги для взаимодействия через api")
public class TestSteps {
    private final ORMSteps ormSteps;
    private final Context context;
    private final FileLoaderImpl fileLoader;

    /**
     * Create test object.
     */
    @Data("создать тестовый объект")
    public void createTestObject() {
        TestObject test = new TestObject();
        test.setId(Double.valueOf(Math.random() * 1000).longValue());
        test.setName("testObject");
        context.set("test", test);
    }

    /**
     * Create database.
     */
    @SneakyThrows
    @When("создать базу")
    public void createDb() {
        try (Session session = ormSteps.getSessionFactory().openSession()) {
            session.beginTransaction();

            String sql = fileLoader.readFileAsString("sql/script.sql");

            if (isSqlValid(sql)) {
                session.createSQLQuery(sql).executeUpdate();
                session.getTransaction().commit();
            } else {
                session.getTransaction().rollback();
                throw new SecurityException("Invalid SQL script detected");
            }
            session.close();
        }
    }

    private boolean isSqlValid(String sql) {
        String upperSql = sql.toUpperCase().trim();
        if (upperSql.contains("DELETE FROM") || upperSql.contains("UPDATE ")) {
            return false;
        }
        return true;
    }
}
