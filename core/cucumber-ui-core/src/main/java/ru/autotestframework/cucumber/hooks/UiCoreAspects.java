package ru.autotestframework.cucumber.hooks;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.configuration.FrameworkProperties;
import ru.autotestframework.ui_core.UiCoreUtils;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;

/**
 * Ui core aspects.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
// @ScenarioScope
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UiCoreAspects {

    private final UiProperties properties;
    private final DriverContainerImpl driverContainer;
    private final FrameworkProperties frameworkProperties;

    /**
     * Aspect responsible for taking screenshots for public UiCore methods.
     *
     * @param joinPoint the join point
     */
    @SneakyThrows
    @After("execution(public * ru.autotestframework.cucumber.step_defs.StepsUi.*(..))"
            + "|| execution(public * ru.autotestframework.cucumber.step_defs.UiTableStepDefs.*(..))")
    public void screenshotUiCore(final JoinPoint joinPoint) {
        if (properties.isScreenShootingOnCoreActionsEnabled()) {
            UiCoreUtils.allureAttach(joinPoint.getSignature().getName(), driverContainer.get());
        }
    }

    /**
     * Aspect responsible for taking screenshots for custom methods.
     *
     * @param joinPoint the join point
     */
    @SneakyThrows
    @After("@annotation(ru.autotestframework.ui_core.services.AddScreenshotOnStep)"
            + "|| @within(ru.autotestframework.ui_core.services.AddScreenshotOnClass)")
    public void screenshotCustomSteps(final JoinPoint joinPoint) {
        if (properties.isScreenShootingOnAnnotationEnabled()) {
            UiCoreUtils.allureAttach(joinPoint.getSignature().getName(), driverContainer.get());
        }
    }
}
