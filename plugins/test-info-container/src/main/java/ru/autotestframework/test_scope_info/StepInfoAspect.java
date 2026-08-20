package ru.autotestframework.test_scope_info;

import io.cucumber.spring.ScenarioScope;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Aspect to save step info
 */
@Slf4j
@Order(0)
@Aspect
@ScenarioScope
@Component
@RequiredArgsConstructor
public class StepInfoAspect {

    private final StepInfoProperties properties;
    private final StepInfoContainer stepInfoContainer;

    @Order(0)
    @SneakyThrows
    @Around("execution(@(@io.cucumber.java.StepDefinitionAnnotation *) * *(..))")
    public void addStepInfo(final ProceedingJoinPoint joinPoint) {
        if (properties.isStepMetaInfoEnabled()) {
            var methodSignature = (MethodSignature) joinPoint.getSignature();
            Annotation[] annotations = methodSignature.getMethod().getAnnotations();
            stepInfoContainer.setAnnotations(
                    Arrays.stream(annotations).map(Annotation::toString).collect(Collectors.toList()));
            stepInfoContainer.setStepArgs(joinPoint.getArgs());
        }
        joinPoint.proceed();
    }
}
