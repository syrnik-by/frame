package ru.autotestframework.screen_elements.elements;

import ru.autotestframework.screen_elements.elements.typified.TypifiedScreenElement;

/**
 * Button.
 */
public class Button extends TypifiedScreenElement {

    /**
     * Instantiates a new Button.
     *
     * @param title          the title
     * @param regionLocation the region location
     * @param location       the location
     * @param offsetX        the offset x
     * @param offsetY        the offset y
     */
    public Button(
            final String title,
            final String regionLocation,
            final String location,
            final int offsetX,
            final int offsetY) {
        super(title, regionLocation, location, offsetX, offsetY);
    }

    /**
     * Instantiates a new Button.
     *
     * @param title          the title
     * @param regionLocation the region location
     * @param location       the location
     * @param searchType     the search type
     * @param offsetX        the offset x
     * @param offsetY        the offset y
     */
    public Button(
            final String title,
            final String regionLocation,
            final String location,
            final int searchType,
            final int offsetX,
            final int offsetY) {
        super(title, regionLocation, location, offsetX, offsetY);
    }
}
