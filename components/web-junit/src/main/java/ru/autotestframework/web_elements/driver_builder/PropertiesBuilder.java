package ru.autotestframework.web_elements.driver_builder;

import com.codeborne.selenide.Configuration;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import ru.autotestframework.Constants;
import ru.autotestframework.ui_core.driver_builder.CorePropertiesBuilder;

@Slf4j
public class PropertiesBuilder extends CorePropertiesBuilder<PropertiesBuilder> {

    private static final Path BASE_DIR =
            Paths.get("src/test/resources").toAbsolutePath().normalize();
    private String headless;

    /**
     * Set ChromeOptions for Configuration using framework properties.
     *
     * @return self
     */
    public PropertiesBuilder withChromeOptions() {
        var chromeOptions = new ChromeOptions();

        applyProxy(chromeOptions);
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--allow-insecure-localhost");
        chromeOptions.addArguments("-–ignore-ssl-errors=yes");
        chromeOptions.addArguments("--ignore-urlfetcher-cert-requests");
        chromeOptions.addArguments("--test-type");
        applyChromePreferences(chromeOptions);

        configuration.getProperties().forEach((key, value) -> {
            if ("chromeOptions".equals(key)) {
                chromeOptions.addArguments(((String) value).replace(" ", "").split(";"));
                if (((String) value).contains("--headless")) {
                    Configuration.headless = true;
                }
            }
            if ("framework.ui.browser.pageLoadStrategy".equals(key)) {
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.fromString((String) value));
            }
            if ("browser.path".equals(key)) {
                chromeOptions.setBinary((String) value);
            }
            if ("selenide.headless".equals(key)) {
                headless = value.toString();
            }
            if ("headless".equals(key)) {
                headless = value.toString();
            }
        });
        if (headless != null) {
            chromeOptions.setHeadless(Boolean.parseBoolean(headless));
        }
        configuration.setChromeOptions(chromeOptions);
        return this;
    }

    private void applyChromePreferences(final ChromeOptions chromeOptions) {
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        var tempFolder = new File(Constants.TEMP_UI_FOLDER);
        chromePrefs.put("download.default_directory", tempFolder.getAbsolutePath());
        chromePrefs.put("plugins.always_open_pdf_externally", true);
        chromePrefs.put("download.directory_upgrade", true);
        chromePrefs.put("plugins.prompt_for_download", false);

        chromePrefs.put("acceptSslCerts", true);
        if (Optional.ofNullable(configuration.getProperties().getProperty("browser.logging"))
                .orElse("")
                .contains("all")) {
            var logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
        }
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
    }

    @SneakyThrows
    private void applyProxy(final ChromeOptions chromeOptions) {
        String host = System.getProperty(Constants.HTTPS_PROXY_HOST);
        String port = System.getProperty(Constants.HTTP_PROXY_PORT);
        if (StringUtils.isNotBlank(port) && StringUtils.isNotBlank(host)) {
            log.info("Browser proxy enabled on {}:{}", host, port);

            var proxy = new Proxy();
            proxy.setAutodetect(false);
            proxy.setProxyType(Proxy.ProxyType.MANUAL);
            proxy.setHttpProxy(host.concat(":").concat(port));
            proxy.setFtpProxy(host.concat(":").concat(port));
            proxy.setSslProxy(host.concat(":").concat(port));

            chromeOptions.setCapability(CapabilityType.PROXY, proxy);
        }
    }
}
