package ru.autotestframework.java_elements.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;
import ru.autotestframework.cucumber.page_manager.PageManager;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.ui_core.page_manager.PageEntry;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class JavaElementsAspects {

    public static final long MILLIS_IN_SECOND = 1000L;
    private final DriverContainer driverContainer;
    private final PageManager pageManager;
    private final UiProperties properties;
    private long waitingTime = 0;
    private static final long START_LOADING_TIME = System.currentTimeMillis();

    /**
     * Aspect responsible for processing modal windows of Java Desktop Applications.
     */
    @SneakyThrows
    @AfterReturning("execution(public void ru.autotestframework.cucumber.step_defs.StepsUi.setCurrentPage(..))")
    public void waitWindowAspect(final JoinPoint joinPoint) {

        WebDriver driver = driverContainer.get();
        String title = pageManager
                .getCurrent()
                .getClass()
                .getAnnotation(PageEntry.class)
                .title();
        if (title.equals("Ввод смс кода") || title.equals("Ошибки валидации")) {
            switchToWindow(driver.getWindowHandles()
                    .toArray()[driver.getWindowHandles().toArray().length - 1]
                    .toString());
        } else {
            switchToWindow(title);
        }
    }

    private void switchToWindow(final String title) {
        try {
            driverContainer.get().switchTo().window(title);
        } catch (NoSuchWindowException e) {
            if (waitingTime <= properties.getTimeout() * MILLIS_IN_SECOND) {
                waitingTime = System.currentTimeMillis() - START_LOADING_TIME;
            } else {
                throw e;
            }
            switchToWindow(title);
        }
    }
}
