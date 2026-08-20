package ru.autotestframework.screen_elements.elements;

import org.sikuli.script.Key;
import ru.autotestframework.screen_elements.elements.typified.TypifiedScreenElement;
import ru.autotestframework.ui_core.exceptions.ElementInteractionException;
import ru.autotestframework.ui_core.typified_elements.ifaces.ICleanable;
import ru.autotestframework.ui_core.typified_elements.ifaces.IWritable;

/**
 * Text box.
 */
public class TextBox extends TypifiedScreenElement implements ICleanable, IWritable {

    /**
     * Instantiates a new Text box.
     *
     * @param title          the title
     * @param regionLocation the region location
     * @param location       the location
     * @param offsetX        the offset x
     * @param offsetY        the offset y
     */
    public TextBox(
            final String title,
            final String regionLocation,
            final String location,
            final int offsetX,
            final int offsetY) {
        super(title, regionLocation, location, offsetX, offsetY);
    }

    /**
     * Instantiates a new Text box.
     *
     * @param title          the title
     * @param regionLocation the region location
     * @param location       the location
     * @param searchType     the search type
     * @param offsetX        the offset x
     * @param offsetY        the offset y
     */
    public TextBox(
            final String title,
            final String regionLocation,
            final String location,
            final int searchType,
            final int offsetX,
            final int offsetY) {
        super(title, regionLocation, location, offsetX, offsetY);
    }

    public void clean() {
        clear();
    }

    @Override
    public void write(final String value) throws ElementInteractionException {
        clean();
        copyToClipboard(value);
        SCREEN.type("v", Key.CTRL);
    }

    @Override
    public void append(final String value) throws ElementInteractionException {
        copyToClipboard(value);
        click();
        SCREEN.type(Key.END);
        SCREEN.type("v", Key.CTRL);
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean isFixStateValue() {
        return false;
    }
}
