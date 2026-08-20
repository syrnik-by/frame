package ru.autotestframework.steps;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.xpath.XPathExpressionException;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.text.StringSubstitutor;
import org.assertj.core.error.AssertJMultipleFailuresError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.autotestframework.configuration.FrameworkDefaultVariables;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.core.DefaultContextVariables;
import ru.autotestframework.core.PlaceholderResolverImpl;
import ru.autotestframework.core.context.Cleanable;
import ru.autotestframework.core.context.Context;
import ru.autotestframework.core.context.ContextImpl;
import ru.autotestframework.core.exception.AutotestException;
import ru.autotestframework.cucumber.step_defs.back.CommonSteps;
import ru.autotestframework.cucumber.step_executor.StepExecutor;
import ru.autotestframework.cucumber.step_executor.StepExecutorImpl;
import ru.autotestframework.cucumber.type.Pair;
import ru.autotestframework.cucumber.type.Triple;

/**
 * Common steps tests.
 */
@Tag("@BackendCore")
class CommonStepsTests {
    private final int countContext = 10;
    private CommonSteps commonSteps;
    private final FrameworkProperties frameworkProperties = new FrameworkProperties();

    /**
     * Before each.
     *
     * @throws IllegalAccessException the illegal access exception
     */
    @BeforeEach
    void beforeEach() throws IllegalAccessException {
        StepExecutor stepExecutor = new StepExecutorImpl();
        List<Cleanable> cleanableBins = new ArrayList<>() {
            {
                add(getTestDataContext());
            }
        };

        commonSteps = new CommonSteps(
                frameworkProperties,
                cleanableBins,
                stepExecutor,
                new PlaceholderResolverImpl(new StringSubstitutor()),
                getTestDataContext());

        FieldUtils.writeField(commonSteps, "cleanableBins", cleanableBins, true);
    }

    /**
     * Sets variable.
     */
    @Test
    void setVariable() {
        int beforeSetSizeContext = getContext().getAll().size();
        commonSteps.setVariable(
                Faker.instance().address().cityName(),
                Faker.instance().address().cityName());
        Assertions.assertEquals(beforeSetSizeContext + 1, getContext().getAll().size());
    }

    /**
     * Sets variables.
     */
    @Test
    void setVariables() {
        int countContext = 10;
        ContextImpl context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));
        while (countContext != 0) {
            context.set(String.valueOf(countContext), "test");
            countContext--;
        }
        var testData = context.getAll().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (String) entry.getValue()));

        var mapBefore = getContext().getAll();
        commonSteps.setVariables(testData);
        var mapAfter = getContext().getAll();
        Assertions.assertEquals(mapBefore.size(), mapAfter.size());

        Map<String, Object> checkMap = new HashMap<>();
        for (String name : mapAfter.keySet()) {
            if (mapBefore.containsKey(name) && mapAfter.get(name).equals(mapAfter.get(name))) {
                checkMap.put(name, mapBefore.get(name));
            }
        }
        Assertions.assertEquals(checkMap.size(), mapAfter.size());
    }

    /**
     * Validate variables.
     */
    @Test
    void validateVariables() {
        Assertions.assertDoesNotThrow(() -> commonSteps.validateVariables(new ArrayList<>() {
            {
                add(Triple.of("1", "contains", getContext().get("1")));
            }
        }));
        Assertions.assertThrows(
                AssertJMultipleFailuresError.class,
                () -> commonSteps.validateVariables(new ArrayList<>() {
                    {
                        add(Triple.of("1", "contains", getContext().get("2")));
                    }
                }));
    }

    /**
     * Sleep.
     */
    @Test
    void sleep() {
        Assertions.assertDoesNotThrow(() -> commonSteps.sleep(1));
    }

    /**
     * Clean context.
     *
     * @throws IllegalAccessException the illegal access exception
     * @throws NoSuchFieldException   the no such field exception
     */
    @Test
    void cleanContext() throws IllegalAccessException, NoSuchFieldException {
        Field cleanableListField = commonSteps.getClass().getDeclaredField("cleanableBins");
        cleanableListField.setAccessible(true);
        List<Cleanable> cleanableList = (List<Cleanable>) cleanableListField.get(commonSteps);
        Assertions.assertNotEquals(0, cleanableList.size());
        commonSteps.cleanContext();
        List<Cleanable> cleanableListAfter = (List<Cleanable>) cleanableListField.get(commonSteps);
        Assertions.assertEquals(
                0, ((ContextImpl) cleanableListAfter.get(0)).getAll().size());
    }

    /**
     * Reset variables to default.
     */
    @Test
    void resetVariablesToDefault() {
        Assertions.assertNotEquals(0, getContext().getAll().size());
        commonSteps.resetVariablesToDefault();
        Assertions.assertEquals(0, getContext().getAll().size());
    }

    /**
     * Variable taken with regex to new variable.
     */
    @Test
    void variableTakenWithRegexToNewVariable() {
        Context beforeSet = getContext();
        commonSteps.variableTakenWithRegexToNewVariable(getTestTriple());
        System.out.println(getContext().getAll().toString());
        Assertions.assertEquals((String) beforeSet.get("1"), getContext().get("1"));
        Assertions.assertEquals(
                String.valueOf(beforeSet.get("1").toString().charAt(0)),
                getContext().get("a1"));
        Assertions.assertEquals((String) beforeSet.get("5"), getContext().get("5"));
        Assertions.assertEquals(
                String.valueOf(beforeSet.get("5").toString().charAt(0)),
                getContext().get("a5"));
        Assertions.assertEquals((String) beforeSet.get("10"), getContext().get("10"));
        Assertions.assertEquals(
                String.valueOf(beforeSet.get("10").toString().charAt(0)),
                getContext().get("a10"));
    }

    /**
     * Put variables from xml response.
     *
     * @throws XPathExpressionException the x path expression exception
     */
    @Test
    void putVariablesFromXMLResponse() throws XPathExpressionException {
        commonSteps.putVariablesFromXMLResponse(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<student>\n"
                        + "    <name>Darla</name>\n"
                        + "    <age>21</age>\n"
                        + "    <subject>Pysics</subject>\n"
                        + "    <gender>Female</gender>\n"
                        + "</student>\n",
                new ArrayList<>() {
                    {
                        add(Pair.of("arrayFemale", "/student[gender='Female']/name"));
                    }
                });
        Assertions.assertEquals("Darla", getContext().get("arrayFemale"));
    }

    /**
     * Steps chain.
     */
    @Test
    void stepsChain() {
        Assertions.assertDoesNotThrow(() -> {
            commonSteps.startRetryStepsChain(5, 100);
            commonSteps.start(1);
            commonSteps.stopStepsChain();
        });
    }

    private Context getContext() {
        try {
            Field contextField = commonSteps.getClass().getDeclaredField("context");
            contextField.setAccessible(true);
            return (Context) contextField.get(commonSteps);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new AutotestException("context not get", e);
        }
    }

    private ContextImpl getTestDataContext() {
        int count = countContext;
        ContextImpl context = new ContextImpl(new DefaultContextVariables(new FrameworkDefaultVariables()));
        while (count != 0) {
            context.set(String.valueOf(count), RandomStringUtils.randomAlphanumeric(10));
            count--;
        }
        return context;
    }

    private List<Triple> getTestTriple() {
        int count = countContext;
        List<Triple> testList = new ArrayList<>();
        while (count != 0) {
            testList.add(Triple.of("a" + count, ".", String.valueOf(count)));
            count--;
        }
        return testList;
    }
}
