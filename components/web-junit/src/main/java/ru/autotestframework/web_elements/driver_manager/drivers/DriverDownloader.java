package ru.autotestframework.web_elements.driver_manager.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import java.io.File;
import java.net.URL;
import java.util.Objects;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;

public final class DriverDownloader {

    private String driverPath;

    private String repoUrl;

    private String repoUser;

    private String repoPass;

    private String driverVersion;

    private Boolean cacheClear;

    private File cacheFile;

    private ChromeDriverManager chromeDriverManager;

    private static DriverDownloader INSTANCE;

    /**
     * DriverDownloader singleton
     * @return DriverDownloader
     */
    public static DriverDownloader getInstance() {
        if (INSTANCE == null) INSTANCE = new DriverDownloader();
        return INSTANCE;
    }

    /**
     * Returns driver's path or download url
     * @param webDriversProperties
     * @return path or url
     */
    public String setUpDriver(WebDriversProperties webDriversProperties) {
        init(webDriversProperties);
        if (!driverPath.equals("")) {
            System.setProperty("webdriver.chrome.driver", driverPath);
            return driverPath;
        } else {
            return download();
        }
    }

    /**
     * Initialises driver properties
     * @param webDriversProperties
     */
    public void init(WebDriversProperties webDriversProperties) {
        driverPath = webDriversProperties.getPath();
        repoUrl = webDriversProperties.getRepoUrl();
        repoUser = webDriversProperties.getRepoUser();
        repoPass = webDriversProperties.getRepoPass();
        driverVersion = webDriversProperties.getVersion();
        cacheClear = webDriversProperties.getCacheClear();
    }

    /**
     * Downloads driver from Nexus
     * @return download url
     */
    @SneakyThrows
    public String download() {
        setChromeDriverManager();
        chromeDriverManager
                .config()
                .setChromeDownloadUrlPattern(buildArtifactoryRawURL("/drivers/chrome/%s/chromedriver_%s32.zip"))
                .setChromeDriverVersion(driverVersion);
        chromeDriverManager.setup();
        setCacheFile();
        return chromeDriverManager.getDownloadedDriverPath();
    }

    @SneakyThrows
    private void setChromeDriverManager() {
        if (Objects.isNull(chromeDriverManager)) {
            chromeDriverManager = (ChromeDriverManager) WebDriverManager.chromedriver()
                    .driverRepositoryUrl(new URL(buildArtifactoryRawURL("/drivers/chrome/index.xml")));
        }
    }

    private void setCacheFile() {
        if (Objects.isNull(cacheFile)) {
            cacheFile = new File(chromeDriverManager.getDownloadedDriverPath());
        }
    }

    /**
     * Clears cache
     */
    @SneakyThrows
    public void clearCache() {
        if (Objects.nonNull(cacheFile) && cacheClear) {
            while (!(cacheFile.getName().equals(".cache"))) {
                cacheFile = cacheFile.getParentFile();
            }
            FileUtils.forceDeleteOnExit(cacheFile);
        }
    }

    private String buildArtifactoryRawURL(String endpoint) {
        return "https://" + getAuthority() + repoUrl + endpoint;
    }

    private String getAuthority() {
        return repoUser.isEmpty() || repoPass.isEmpty() ? "" : repoUser + ":" + repoPass + "@";
    }
}
