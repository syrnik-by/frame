package ru.autotestframework.junit.tests;

import com.codeborne.selenide.Configuration;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import ru.autotestframework.Constants;
import ru.autotestframework.ui_core.configuration.IDriverSetter;
import ru.autotestframework.ui_core.configuration.UiProperties;
import ru.autotestframework.ui_core.driver_manager.DriverContainerImpl;
import ru.autotestframework.ui_core.junit.BaseUITest;

public abstract class JUnitUIBaseExampleTest extends BaseUITest {

    @Autowired
    DriverContainerImpl driverContainer;

    @Autowired
    protected UiProperties uiProperties;

    @Autowired
    protected List<IDriverSetter> driverSetters;

    @BeforeEach
    public void startUp() {

        // конфигурация общая для проекта
        setupSelenide();

        // интегрирует framework.properties с selenide Configuration
        // необходимо для разбиения по стендам
        driverSetters.forEach(IDriverSetter::setDriver);

        // не нужно (ответственность за старт передана Selenide) // если не используете листенеры (WebDriverProvider)
        // WebDriverRunner.setWebDriver(driverContainer.get());

        // если один стенд можно захардкодить, можно выбрать строку из стенда из пропертей спринговых
        // Selenide.open();
    }

    private void setupSelenide() {
        ChromeOptions chromeOptions = new ChromeOptions()
                .addArguments("--disable-gpu")
                .addArguments("--start-maximized")
                .addArguments("--disable-blink-features")
                .addArguments("--disable-blink-features=AutomationControlled")
                .addArguments("--incognito");

        chromeOptions.setAcceptInsecureCerts(true);
        Configuration.browserCapabilities = chromeOptions.merge(Configuration.browserCapabilities);

        Configuration.baseUrl = "https://localhost";
        Configuration.downloadsFolder = Constants.TEMP_UI_FOLDER;
        Configuration.headless = false;
        Configuration.timeout = uiProperties.getTimeout() * 1000L;
        // осторожно с этой опцией
        Configuration.holdBrowserOpen = true;
    }

    @AfterEach
    public void teardown() {
        // отдано на откуп селенида, решает переиспользовать браузер или нет
        driverContainer.release();
    }
}
