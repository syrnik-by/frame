package ru.autotestframework.orm_steps.step_defs;

import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import jdk.jfr.Description;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.SessionFactory;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.core.descriptions.annotation.Example;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.orm_steps.utils.ORMUtils;

/**
 * Orm step definitions for cucumber.
 */
@Data
@RequiredArgsConstructor
@Description("ORM")
public class ORMSteps {
    private final SessionFactory sessionFactory;
    private final String packagePath;
    private final Context context;

    private List<Object> resultObjects = new ArrayList<>();

    /**
     * Gets all objects.
     *
     * @param clazz   the clazz
     * @param varName the var name
     */
    @SneakyThrows
    @When("получить все объекты {resolvable_string} из базы данных в переменную {resolvable_string}")
    @Example(example = "И получить все объекты 'Test' из базы данных в переменную 'test'")
    public void getAllObjects(String clazz, String varName) {
        context.set(varName, ORMUtils.getAllObjects(sessionFactory, Class.forName(packagePath + "." + clazz)));
    }

    /**
     * Gets object by id.
     *
     * @param clazz   the clazz
     * @param id      the id
     * @param varName the var name
     */
    @SneakyThrows
    @When("получить объект {resolvable_string} по идентификатору {long} в переменную {resolvable_string}")
    @Example(example = "И получить объект 'Test' по идентификатору 2 в переменную 'test'")
    public void getObjectById(String clazz, long id, String varName) {
        context.set(varName, ORMUtils.getById(sessionFactory, Class.forName(packagePath + "." + clazz), id));
    }

    /**
     * Gets object by field.
     *
     * @param clazz   the clazz
     * @param varName the var name
     * @param list    the list
     */
    @SneakyThrows
    @When("получить объект/объекты {resolvable_string} в переменную {resolvable_string} где:")
    @Example(example = "И получить объекты 'Test' в переменную 'test' где:" + "| name | like | Hello |")
    public void getObjectByField(String clazz, String varName, List<Triple> list) {
        context.set(varName, ORMUtils.getByField(sessionFactory, Class.forName(packagePath + "." + clazz), list));
    }

    /**
     * Check object by field.
     *
     * @param clazz the clazz
     * @param list  the list
     */
    @SneakyThrows
    @When("проверить, что в базе данных отсутствует объект {resolvable_string} с параметрами:")
    @Example(
            example =
                    "проверить, что в базе данных отсутствует объект 'Test' с параметрами:" + "| name | like | Hello |")
    public void checkObjectByField(String clazz, List<Triple> list) {
        if (!ORMUtils.getByField(sessionFactory, Class.forName(packagePath + "." + clazz), list)
                .isEmpty()) {
            throw new AutotestException("Объект {} присутсвует в базе!", clazz);
        }
    }

    /**
     * Insert object.
     *
     * @param objectName the object name
     */
    @SneakyThrows
    @When("добавить в базу данных объект {resolvable_string}")
    @Example(example = "И добавить в базу данных объект 'test'")
    public void insertObject(String objectName) {
        ORMUtils.insertObject(sessionFactory, context.getObj(objectName));
    }

    /**
     * Update object.
     *
     * @param objectName the object name
     */
    @SneakyThrows
    @When("обновить в базе данных объект {resolvable_string}")
    @Example(example = "И обновить в базе данных объект 'test'")
    public void updateObject(String objectName) {
        ORMUtils.updateObject(sessionFactory, context.getObj(objectName));
    }

    /**
     * Del object by field.
     *
     * @param clazz the clazz
     * @param list  the list
     */
    @SneakyThrows
    @When("удалить из базы данных объект {resolvable_string} с параметрами:")
    @Example(example = "И удалить из базы данных объект 'Test' с параметрами:" + "| id | = | 1 |")
    public void delObjectByField(String clazz, List<Triple> list) {
        ORMUtils.deleteObject(sessionFactory, Class.forName(packagePath + "." + clazz), list);
    }

    /**
     * Del object.
     *
     * @param objectName the object name
     */
    @SneakyThrows
    @When("удалить из базы данных объект {resolvable_string}")
    @Example(example = "И удалить из базы данных объект 'test'")
    public void delObject(String objectName) {
        ORMUtils.deleteObject(sessionFactory, context.getObj(objectName), List.of());
    }
}
