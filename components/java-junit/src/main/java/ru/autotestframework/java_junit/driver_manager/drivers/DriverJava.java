package ru.autotestframework.java_junit.driver_manager.drivers;

import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import ru.autotestframework.java_junit.driver_builder.JavaDriverBuilder;
import ru.autotestframework.java_junit.driver_builder.PropertiesBuilder;
import ru.autotestframework.java_junit.elements.typified.TypifiedJavaElement;
import ru.autotestframework.ui_core.driver_builder.Configuration;
import ru.autotestframework.ui_core.driver_manager.Driver;

public class DriverJava extends Driver {
    @Override
    public String getTypifiedElementClassName() {
        return TypifiedJavaElement.class.getName();
    }

    public DriverJava(final String path, final String propertyPath) {
        super(path, propertyPath);
    }

    @Override
    public WebDriver build() {
        System.setProperty("webdriver.java.driver", getPath());
        if (getPropertyPath().isEmpty()) {
            return new JavaDriverBuilder(new PropertiesBuilder().build()).build();
        }
        PropertiesBuilder propertiesBuilder = new PropertiesBuilder().withProperties(getPropertyPath());
        Configuration javaDriverConfiguration =
                propertiesBuilder.withDesiredCapabilities().build();
        return new JavaDriverBuilder(javaDriverConfiguration).build();
    }

    @SneakyThrows
    @Override
    public File takeScreenshot() {
        File screen = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        WebDriver.Window window = getDriver().manage().window();
        Rectangle rect =
                new Rectangle(window.getSize().getWidth(), window.getSize().getHeight());
        org.openqa.selenium.Point p = window.getPosition();
        ImageIO.write(ImageIO.read(screen).getSubimage(p.getX(), p.getY(), rect.width, rect.height), "png", screen);
        return screen;
    }
}
