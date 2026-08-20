package ru.autotestframework.web_elements.services;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

public class ParallelExecutionConfig
        implements org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration,
                ParallelExecutionConfigurationStrategy {

    private final int parallelTests = Math.max(
            Integer.getInteger("cucumber.execution.parallel.config.fixed.parallelism", 1),
            Integer.getInteger("junit.jupiter.execution.parallel.config.fixed.parallelism", 1));

    @Override
    public int getParallelism() {
        return parallelTests;
    }

    @Override
    public int getMinimumRunnable() {
        return 0;
    }

    @Override
    public int getMaxPoolSize() {
        return Integer.parseInt(System.getProperty(
                "junit.jupiter.execution.parallel.config.fixed.max-pool-size", String.valueOf(parallelTests)));
    }

    @Override
    public int getCorePoolSize() {
        return getParallelism();
    }

    @Override
    public int getKeepAliveSeconds() {
        return 20;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(ConfigurationParameters configurationParameters) {
        return this;
    }
}
