package ru.autotestframework.web_elements.driver_builder;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_builder.IDriverBuilder;
import ru.autotestframework.web_elements.services.HighlightAspectService;

public class WebDriverBuilder implements IDriverBuilder {

    private final Configuration configuration;
    private WebDriver webDriver;

    public WebDriverBuilder(final Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("UnusedReturnValue")
    private WebDriverBuilder withDecorators() {
        if (Boolean.parseBoolean(configuration.getProperties().getProperty("framework.ui.aspects.highlight.enabled"))) {
            webDriver = new EventFiringDecorator(new HighlightAspectService()).decorate(webDriver);
        }
        return this;
    }

    @Override
    public WebDriver build() {
        // TODO придумать как поправить часть с декоратором
        // withCookies().withLocalStorage().withSessionStorage().withDecorators();
        return null;
    }
}
