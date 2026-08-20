package ru.autotestframework.tests;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.core.DefaultContextVariables;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.cucumber.type.Triple;
import ru.autotestframework.models.TestObject;
import ru.autotestframework.orm_steps.configuration.ORMStepsConfig;
import ru.autotestframework.orm_steps.configuration.ORMStepsProperties;
import ru.autotestframework.orm_steps.step_defs.ORMSteps;

/**
 * Orm Steps test.
 */
@Tag("@OrmSteps")
@TestMethodOrder(OrderAnnotation.class)
class StepsTest {

    private static ORMSteps ormSteps;
    private static TestObject testObject = new TestObject();
    public static final String TEST_CLASS = testObject.getClass().getSimpleName();
    private static Context context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));
    private static long id = Double.valueOf(Math.random() * 1000).longValue();
    private static final String UPDATE_OBJECT = "updateObject";
    private static final String CONTEXT_OBJECT = "contextObject";
    private static final String OBJECT_BY_FIELD = "objectByField";

    /**
     * Before steps test.
     */
    @BeforeAll
    public static void beforeStepsTest() {
        ORMStepsProperties ormStepsProperties = new ORMStepsProperties();
        ormStepsProperties.setPropertiesPath("src/test/resources/hibernate.properties");
        ormStepsProperties.setPackagePath("ru.autotestframework.models");
        ORMStepsConfig ormStepsConfig = new ORMStepsConfig(ormStepsProperties, context);
        ormSteps = ormStepsConfig.getORMSteps();
        testObject.setId(id);
        testObject.setName("testObject");
        context.set(CONTEXT_OBJECT, testObject);
    }

    @AfterAll
    public static void deleteAll() {
        context.clean();
    }

    /**
     * Insert test.
     */
    @Test
    @Order(1)
    void insertTest() {
        ormSteps.insertObject(CONTEXT_OBJECT);
        ormSteps.getObjectById(TEST_CLASS, id, CONTEXT_OBJECT);
        TestObject test = context.getObj(CONTEXT_OBJECT);
        Assertions.assertEquals(id, test.id);
    }

    /**
     * Update test.
     */
    @Test
    @Order(2)
    void updateTest() {
        testObject.setName(UPDATE_OBJECT);
        context.set(CONTEXT_OBJECT, testObject);
        ormSteps.updateObject(CONTEXT_OBJECT);
        ormSteps.getObjectById(TEST_CLASS, id, CONTEXT_OBJECT);
        TestObject test = context.getObj(CONTEXT_OBJECT);
        Assertions.assertEquals(UPDATE_OBJECT, test.name);
    }

    /**
     * Select all test.
     */
    @Test
    @Order(3)
    void selectAllTest() {
        ormSteps.getAllObjects(TEST_CLASS, "selectAllObject");
        Assertions.assertNotNull(context.get("selectAllObject"));
    }

    /**
     * Select by id test.
     */
    @Test
    @Order(4)
    void selectByIdTest() {
        ormSteps.getObjectById(TEST_CLASS, id, CONTEXT_OBJECT);
        TestObject test = context.getObj(CONTEXT_OBJECT);
        Assertions.assertEquals(id, test.id);
    }

    /**
     * Delete test.
     */
    @Test
    @Order(5)
    void deleteTest() {
        ormSteps.delObject(CONTEXT_OBJECT);
        ormSteps.getObjectById(TEST_CLASS, id, CONTEXT_OBJECT);
        Assertions.assertNull(context.getObj(CONTEXT_OBJECT));
    }

    /**
     * Select by field test.
     */
    @Test
    @Order(6)
    void selectByFieldTest() {
        testObject.setName(OBJECT_BY_FIELD);
        context.set(CONTEXT_OBJECT, testObject);
        ormSteps.insertObject(CONTEXT_OBJECT);

        List<Triple> list = new ArrayList<>();
        Triple triple = Triple.of("name", "like", OBJECT_BY_FIELD);
        list.add(triple);
        ormSteps.getObjectByField(TEST_CLASS, CONTEXT_OBJECT, list);
        TestObject test = (TestObject) ((List<Object>) context.getObj(CONTEXT_OBJECT)).get(0);
        Assertions.assertEquals(OBJECT_BY_FIELD, test.name);
    }

    /**
     * Delete by field test.
     */
    @Test
    @Order(7)
    void deleteByFieldTest() {
        List<Triple> list = new ArrayList<>();
        Triple triple = Triple.of("name", "like", OBJECT_BY_FIELD);
        list.add(triple);
        ormSteps.delObjectByField(TEST_CLASS, list);
        ormSteps.getObjectById(TEST_CLASS, id, CONTEXT_OBJECT);
        Assertions.assertNull(context.getObj(CONTEXT_OBJECT));
    }
}
