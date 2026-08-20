package ru.autotestframework.screen_elements.elements;

import java.awt.Rectangle;
import lombok.Getter;
import lombok.SneakyThrows;
import org.openqa.selenium.Point;
import org.sikuli.basics.Settings;
import org.sikuli.script.Region;
import ru.autotestframework.screen_elements.driver_manager.drivers.DriverScreen;
import ru.autotestframework.screen_elements.elements.typified.TypifiedScreenElement;

/**
 * Text button.
 */
public class TextButton extends TypifiedScreenElement {

    @Getter
    private Region region = null;

    /**
     * Instantiates a new Text button.
     *
     * @param title          the title
     * @param regionLocation the region location
     * @param text           the text
     * @param searchType     the search type
     * @param offsetX        the offset x
     * @param offsetY        the offset y
     */
    public TextButton(
            final String title,
            final String regionLocation,
            final String text,
            final int searchType,
            final int offsetX,
            final int offsetY) {
        super(title, regionLocation.replace("", Settings.OcrLanguage), text, searchType, offsetX, offsetY);
    }

    @Override
    public boolean isDisplayed() {
        return SCREEN.hasText(getSource());
    }

    @Override
    public Point getLocation() {
        return new Point((int) getRectangle().getCenterX(), (int) getRectangle().getCenterY());
    }

    @Override
    public String getTagName() {
        return "findByText";
    }

    /**
     * Gets rectangle.
     *
     * @return the rectangle
     */
    @SneakyThrows
    public Rectangle getRectangle() {
        return getRegion().getRect();
    }

    @Override
    @SneakyThrows
    public Region waitElement() {
        if (region == null) {
            region = DriverScreen.getRegion(getSource(), getSearchType(), getRegionLocation());
        }
        return region;
    }
}
