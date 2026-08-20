package ru.autotestframework.web_elements.services;

import static com.codeborne.selenide.FileDownloadMode.FOLDER;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.google.common.net.MediaType;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.ScenarioScope;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ru.autotestframework.Constants;
import ru.autotestframework.core.FileLoaderImpl;
import ru.autotestframework.cucumber.hooks.UIElementsHooks;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainer;
import ru.autotestframework.util.StringUtil;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverDownloader;
import ru.autotestframework.web_elements.helpers.ConsoleLogContainer;
import ru.autotestframework.web_elements.helpers.ExtendedDownloadsFolder;

@Slf4j
@Lazy
@ScenarioScope
@RequiredArgsConstructor
public class WebElementsHooks {
    private static final String FAILURE_LOG_NAME = "browserConsoleFailureLog.txt";
    private static final String HTML_LOG_STRATEGY_OFF = "off";
    private final WebDriversProperties webDriversProperties;
    private final UiProperties uiProperties;

    @Autowired
    private DriverContainer driverContainer;

    private final FileLoaderImpl fileLoader;

    @Autowired
    private final List<IDriverSetter> driverSetters;

    @Autowired
    private ConsoleLogContainer consoleLogContainer;

    @AfterAll
    public static void afterAll() {
        DriverDownloader.getInstance().clearCache();
    }

    /**
     * initialises and configures drivers and working folders and files
     * @param scenario
     */
    @SneakyThrows
    @Before()
    public void initFirstPage(Scenario scenario) {

        if (!(uiProperties.getDriverInit() || scenario.getSourceTagNames().contains("@UI"))) {
            log.error("Driver initialization is disabled");
        } else {
            driverSetters.forEach(IDriverSetter::setDriver);
            Configuration.fileDownload = FOLDER;
            Configuration.downloadsFolder = Constants.TEMP_UI_FOLDER;
            var fileFolder = new File(Constants.TEMP_UI_FOLDER);
            fileFolder.mkdirs();
            WebDriverRunner.getBrowserDownloadsFolder();
            var folder = new ExtendedDownloadsFolder(fileFolder);
        }
    }

    @SneakyThrows
    @Before(order = 10001)
    public void openUrl() {
        Selenide.open(webDriversProperties.getStartingUrl());
    }

    /**
     * Adding console logs to report on failure.
     * Order due {@link UIElementsHooks#tearDown}.
     *
     * @param scenario {@link Scenario}
     */
    @After(order = Integer.MAX_VALUE - 1000)
    public void after(final Scenario scenario) {
        if (scenario.isFailed()
                && uiProperties.getDriverInit()
                && !webDriversProperties.getBrowserLoggingStrategy().equalsIgnoreCase(HTML_LOG_STRATEGY_OFF)) {
            String failureConsoleLog = driverContainer.get().manage().logs().get(LogType.BROWSER).getAll().stream()
                    .map(LogEntry::toString)
                    .collect(Collectors.joining("\n"));
            var description = StringUtil.format("Browser Console log presented further: \n{}", failureConsoleLog);

            if (StringUtils.isNotBlank(failureConsoleLog)) {
                scenario.attach(
                        description, MediaType.PLAIN_TEXT_UTF_8.toString().toLowerCase(Locale.ROOT), FAILURE_LOG_NAME);
            }
            consoleLogContainer
                    .getConsoleLog()
                    .forEach((key, value) -> scenario.attach(
                            value, MediaType.PLAIN_TEXT_UTF_8.toString().toLowerCase(Locale.ROOT), key));
        }
    }
}
