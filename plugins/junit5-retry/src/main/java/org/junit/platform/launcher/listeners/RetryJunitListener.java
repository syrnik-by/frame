package org.junit.platform.launcher.listeners;

import static java.util.stream.Stream.concat;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.UniqueIdSelector;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;

/**
 * JUnit5 retry extension (integrated with Cucumber). Substantially for use with gradle (have extended logic from maven-surefire plugin)
 * Not visible in logs but can be viewed at extra report Tools - Allure / TMS, use default props for test or from annotations
 *  {@literal @}Retry3 for Cucumber
 *  {@literal @}Tag("Retry3") for JUnit5
 */
@Slf4j
public class RetryJunitListener implements TestExecutionListener {

    private static final String JUNIT_RETRY_ATTEMPTS_PROPERTY = "junit.retry.attempts";
    private static final String JUNIT_RETRY_TIMEOUT_PROPERTY = "junit.retry.timeout";
    private static final String JUNIT_RETRY_ALL_TESTS_PROPERTY = "junit.retry.allTests";
    static final int DEFAULT_RETRIES = Integer.parseInt(System.getProperty(JUNIT_RETRY_ATTEMPTS_PROPERTY, "0"));
    static final int TIMEOUT = Integer.parseInt(System.getProperty(JUNIT_RETRY_TIMEOUT_PROPERTY, "0"));
    static final boolean ALL_TO_RETRY = Boolean.getBoolean(JUNIT_RETRY_ALL_TESTS_PROPERTY);
    private static TestPlan testPlan;
    private static MutableTestExecutionSummary summary;
    private static AtomicInteger retries = new AtomicInteger(0);

    private static List<UniqueIdSelector> getFailures(TestExecutionSummary summary, int iteration) {

        boolean isRetriableOnDefault = ALL_TO_RETRY && DEFAULT_RETRIES > iteration;

        log.info(summary.getFailures().stream()
                .map(x -> x.getException().getMessage())
                .collect(Collectors.joining("\n")));

        return summary.getFailures().stream()
                .map(Failure::getTestIdentifier)
                .filter(TestIdentifier::isTest)
                .filter(x -> x.getTags().stream()
                        .anyMatch(tag -> isRetriableOnDefault && !tag.getName().contains("Retry")
                                || tag.getName().contains("Retry")
                                        && Optional.ofNullable(parseRetryCount(tag.getName()))
                                                        .orElse(DEFAULT_RETRIES)
                                                > iteration))
                .map(TestIdentifier::getUniqueId)
                .map(DiscoverySelectors::selectUniqueId)
                .collect(Collectors.toList());
    }

    private static Integer parseRetryCount(String tagString) {
        Integer parsed = null;
        if (tagString.length() > 5) {
            var value = tagString.substring(5);
            parsed = Integer.parseInt(value);
        }
        return parsed;
    }

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        RetryJunitListener.testPlan = testPlan;
        summary = new MutableTestExecutionSummary(testPlan);
    }

    @SneakyThrows
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        try (LauncherSession session = LauncherFactory.openSession()) {
            var launcher = session.getLauncher();

            summary.timeFinished = System.currentTimeMillis();
            summary.timeFinishedNanos = System.nanoTime();

            log.info(
                    "There are some failed tests: {}, trying to restart",
                    summary.getFailures().size());

            List<UniqueIdSelector> retriableFailures = getFailures(summary, retries.get());
            while (!retriableFailures.isEmpty()) {
                retries.incrementAndGet();
                Thread.sleep(TIMEOUT * 1000L);
                LauncherDiscoveryRequest rerunRequest = LauncherDiscoveryRequestBuilder.request()
                        .selectors(retriableFailures)
                        .build();

                log.info("Test Retry cycle: attempt № {}", retries);
                launcher.execute(rerunRequest);
                retriableFailures = getFailures(summary, retries.get());
            }
        }
    }

    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
        if (testIdentifier.isContainer()) {
            summary.containersFound.incrementAndGet();
        }
        if (testIdentifier.isTest()) {
            summary.testsFound.incrementAndGet();
        }
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        long skippedContainers = concat(Stream.of(testIdentifier), testPlan.getDescendants(testIdentifier).stream())
                .filter(TestIdentifier::isContainer)
                .count();
        long skippedTests = concat(Stream.of(testIdentifier), testPlan.getDescendants(testIdentifier).stream())
                .filter(TestIdentifier::isTest)
                .count();
        summary.containersSkipped.addAndGet(skippedContainers);
        summary.testsSkipped.addAndGet(skippedTests);
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isContainer()) {
            summary.containersStarted.incrementAndGet();
        }
        if (testIdentifier.isTest()) {
            summary.testsStarted.incrementAndGet();
        }
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {

        switch (testExecutionResult.getStatus()) {
            case SUCCESSFUL: {
                if (testIdentifier.isContainer()) {
                    summary.containersSucceeded.incrementAndGet();
                }
                if (testIdentifier.isTest()) {
                    summary.testsSucceeded.incrementAndGet();
                }
                break;
            }

            case ABORTED: {
                if (testIdentifier.isContainer()) {
                    summary.containersAborted.incrementAndGet();
                }
                if (testIdentifier.isTest()) {
                    summary.testsAborted.incrementAndGet();
                }
                break;
            }

            case FAILED: {
                if (testIdentifier.isContainer()) {
                    summary.containersFailed.incrementAndGet();
                }
                if (testIdentifier.isTest()) {
                    summary.testsFailed.incrementAndGet();
                }
                testExecutionResult
                        .getThrowable()
                        .ifPresent(throwable -> summary.addFailure(testIdentifier, throwable));
                break;
            }

            default:
                throw new PreconditionViolationException(
                        "Unsupported execution status:" + testExecutionResult.getStatus());
        }
    }
}
