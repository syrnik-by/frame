package ru.autotestframework.tests.drivers;

import io.github.bonigarcia.wdm.config.Config;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import java.io.File;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.autotestframework.web_elements.configuration.WebDriversProperties;
import ru.autotestframework.web_elements.driver_manager.drivers.DriverDownloader;

@Disabled
@Tag("@webElemElements")
class DriverDownloaderTest {

    @Test
    void setUpDriverWithoutDownloadTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();

        String driverPath = DriverDownloader.getInstance().setUpDriver(webDriversProperties);
        Assertions.assertEquals(driverPath, webDriversProperties.getPath());
    }

    @Test
    void setUpDriverWithDownloadTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();

        DriverDownloader driverDownloader = Mockito.mock(DriverDownloader.class);
        Mockito.when(driverDownloader.download()).thenReturn("download");
        Mockito.doCallRealMethod().when(driverDownloader).init(webDriversProperties);
        Mockito.doCallRealMethod().when(driverDownloader).setUpDriver(webDriversProperties);
        webDriversProperties.setPath("");
        ReflectionTestUtils.setField(driverDownloader, "INSTANCE", driverDownloader);
        String driverPath = DriverDownloader.getInstance().setUpDriver(webDriversProperties);
        Assertions.assertEquals("download", driverPath);
    }

    @Test
    void initTest() {
        WebDriversProperties webDriversProperties = new WebDriversProperties();
        DriverDownloader.getInstance().init(webDriversProperties);
        Assertions.assertEquals(
                DriverDownloader.getInstance().setUpDriver(webDriversProperties), webDriversProperties.getPath());
    }

    @Test
    void downloadTest() {
        DriverDownloader driverDownloader = Mockito.mock(DriverDownloader.class);
        ReflectionTestUtils.setField(driverDownloader, "INSTANCE", driverDownloader);
        Mockito.doCallRealMethod().when(driverDownloader).download();
        ChromeDriverManager chromeDriverManager = Mockito.mock(ChromeDriverManager.class);
        ReflectionTestUtils.setField(driverDownloader, "chromeDriverManager", chromeDriverManager);
        Config config = Mockito.mock(Config.class);
        Mockito.when(chromeDriverManager.config()).thenReturn(config);
        Mockito.when(config.setChromeDownloadUrlPattern(Mockito.anyString())).thenReturn(config);
        Mockito.when(config.setChromeDriverVersion(Mockito.anyString())).thenReturn(config);
        Mockito.doNothing().when(chromeDriverManager).setup();
        ReflectionTestUtils.setField(driverDownloader, "repoUrl", "repoUrl");
        ReflectionTestUtils.setField(driverDownloader, "repoUser", "repoUser");
        ReflectionTestUtils.setField(driverDownloader, "repoPass", "repoPass");
        ReflectionTestUtils.setField(driverDownloader, "driverVersion", "driverVersion");
        File file = Mockito.mock(File.class);
        ReflectionTestUtils.setField(driverDownloader, "cacheFile", file);
        Mockito.when(chromeDriverManager.getDownloadedDriverPath()).thenReturn("downloadedDriverPath");
        String download = DriverDownloader.getInstance().download();
        Assertions.assertEquals("downloadedDriverPath", download);
        Mockito.verify(config, Mockito.times(1)).setChromeDownloadUrlPattern(Mockito.anyString());
        Mockito.verify(config, Mockito.times(1)).setChromeDriverVersion(Mockito.anyString());
        Mockito.verify(chromeDriverManager, Mockito.times(1)).setup();
    }

    @Test
    void clearCacheTest() {
        DriverDownloader driverDownloader = Mockito.mock(DriverDownloader.class);
        ReflectionTestUtils.setField(driverDownloader, "INSTANCE", driverDownloader);
        Mockito.doCallRealMethod().when(driverDownloader).clearCache();
        File file = Mockito.mock(File.class);
        ReflectionTestUtils.setField(driverDownloader, "cacheFile", file);
        ReflectionTestUtils.setField(driverDownloader, "cacheClear", true);
        Mockito.doThrow(Error.class).when(file).deleteOnExit();
        Mockito.when(file.getName()).thenReturn(".cache");
        Assertions.assertThrows(
                Error.class, () -> DriverDownloader.getInstance().clearCache());
    }

    @AfterEach
    void removeInstance() {
        ReflectionTestUtils.setField(DriverDownloader.getInstance(), "INSTANCE", null);
    }
}
