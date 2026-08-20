package ru.autotestframework.ui_core.driver_manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Driver container impl boot.
 */
@Component
@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class DriverContainerImplBoot extends DriverContainerImpl {}
