package ru.autotestframework.cucumber.driver_manager;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;

/**
 * Driver container impl cucumber.
 */
@ScenarioScope
@Component
@Slf4j
@Primary
@ConditionalOnProperty(value = "framework.junit", havingValue = "false", matchIfMissing = true)
public class DriverContainerImplCucumber extends DriverContainerImpl {}
