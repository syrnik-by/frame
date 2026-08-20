package ru.autotestframework.web_elements.services;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.util.StringUtil;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.helpers.ConsoleLogContainer;

@Slf4j
@Aspect
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@RequiredArgsConstructor
public class ConsoleLogService {

    private final WebDriversProperties properties;
    private final DriverContainerImpl driverContainer;
    private final ConsoleLogContainer consoleLogContainer;
    private static final String HTML_LOG_STRATEGY_PAGE = "page";
    /**
     * collects logs from webdriver to file
     * @param joinPoint
     */
    @SneakyThrows
    @Before("execution(public * ru.autotestframework.cucumber.step_defs.StepsUi.setCurrentPage(..))")
    public void provideLogs(final JoinPoint joinPoint) {
        if (properties.getBrowserLoggingStrategy().equalsIgnoreCase(HTML_LOG_STRATEGY_PAGE)) {
            String pageName = (String) ((MethodInvocationProceedingJoinPoint) joinPoint).getArgs()[0];

            String consoleLog = driverContainer.get().manage().logs().get(LogType.BROWSER).getAll().stream()
                    .map(LogEntry::toString)
                    .collect(Collectors.joining("\n"));

            var description =
                    StringUtil.format("'{}' page loaded. Console log presented further: \n{}", pageName, consoleLog);

            if (StringUtils.isNotBlank(consoleLog)) {
                consoleLogContainer.put(pageName, description);
            }
        }
    }
}
