import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.datafaker.Faker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.configuration.FrameworkConfig;
import ru.autotestframework.test_scope_info.StepInfoAspect;
import ru.autotestframework.test_scope_info.StepInfoContainer;
import ru.autotestframework.test_scope_info.StepInfoProperties;

@Tag("@TestInfo")
class StepInfoAspectTest {

    @Test
    void aspectTest() throws NoSuchMethodException {
        ProceedingJoinPoint jp = Mockito.mock(ProceedingJoinPoint.class);

        StepInfoContainer stepInfoContainer2 = new StepInfoContainer();
        StepInfoProperties properties = new StepInfoProperties();
        StepInfoContainer stepInfoContainer = Mockito.spy(stepInfoContainer2);
        StepInfoAspect aspect = new StepInfoAspect(properties, stepInfoContainer);

        properties.setStepMetaInfoEnabled(true);
        ReflectionTestUtils.setField(aspect, "properties", properties);
        ReflectionTestUtils.setField(aspect, "stepInfoContainer", stepInfoContainer);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Method method = FrameworkConfig.class.getDeclaredMethod("configureOnStartUp");
        Mockito.when(signature.getMethod()).thenReturn(method);
        Mockito.when(jp.getSignature()).thenReturn(signature);
        Object[] obj = new Object[12];
        Arrays.fill(obj, Faker.instance().ancient());
        Mockito.when(jp.getArgs()).thenReturn(obj);
        aspect.addStepInfo(jp);

        Assertions.assertEquals(jp.getArgs(), stepInfoContainer.getStepArgs());
        Assertions.assertNotEquals(0, jp.getArgs().length);
        Assertions.assertEquals(
                Arrays.stream(method.getAnnotations()).map(Annotation::toString).collect(Collectors.toList()),
                stepInfoContainer.getAnnotations());
        Assertions.assertNotEquals(0, stepInfoContainer.getAnnotations().size());
    }

    @Test
    void aspectDisabledTest() {

        ProceedingJoinPoint jp = Mockito.mock(ProceedingJoinPoint.class);

        StepInfoContainer stepInfoContainer2 = new StepInfoContainer();
        StepInfoProperties properties = new StepInfoProperties();
        StepInfoContainer stepInfoContainer = Mockito.spy(stepInfoContainer2);
        properties.setStepMetaInfoEnabled(false);
        StepInfoAspect aspect = new StepInfoAspect(properties, stepInfoContainer);
        ReflectionTestUtils.setField(aspect, "properties", properties);
        ReflectionTestUtils.setField(aspect, "stepInfoContainer", stepInfoContainer);
        MethodSignature signature = Mockito.mock(MethodSignature.class);

        aspect.addStepInfo(jp);
        Mockito.verifyNoInteractions(stepInfoContainer, signature);
    }

    @Test
    void aspectConstructorTest() {

        StepInfoContainer stepInfoContainer2 = new StepInfoContainer();
        StepInfoProperties properties = new StepInfoProperties();
        Assertions.assertDoesNotThrow(() -> new StepInfoAspect(properties, stepInfoContainer2));
    }
}
