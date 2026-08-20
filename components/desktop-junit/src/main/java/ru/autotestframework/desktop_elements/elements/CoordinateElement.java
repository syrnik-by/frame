package ru.autotestframework.desktop_elements.elements;

import ru.autotestframework.desktop_elements.actions.MouseActions;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriver;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.desktop_elements.enums.BasePoint;
import ru.autotestframework.ui_core.typified_elements.enums.KeyCombination;
import ru.autotestframework.ui_core.typified_elements.enums.KeyboardLayout;

public class CoordinateElement {

    private TypifiedDesktopElement baseDesktopElement;
    private BasePoint basePointOfBaseDesktopElement;
    private int dx;
    private int dy;
    private int width;
    private int height;
    private MouseActions mouseActions;

    /**
     * Constructor to create Linking Object of DeskTopElement to its on screen location.
     *
     * @param baseDesktopElement            element
     * @param basePointOfBaseDesktopElement
     * @param dx                            left x-axis point
     * @param dy                            bottom y-axis point
     * @param width                         element width
     * @param height                        element height
     */
    public CoordinateElement(
            final TypifiedDesktopElement baseDesktopElement,
            final BasePoint basePointOfBaseDesktopElement,
            final int dx,
            final int dy,
            final int width,
            final int height) {
        this.baseDesktopElement = baseDesktopElement;
        this.basePointOfBaseDesktopElement = basePointOfBaseDesktopElement;
        this.dx = dx;
        this.dy = dy;
        this.width = width;
        this.height = height;
        mouseActions = new MouseActions(baseDesktopElement);
    }

    /**
     * moves mouse to element with offset
     * @param basePoint
     * @param offsetX
     * @param offsetY
     */
    public void moveMouseToElementPoint(final BasePoint basePoint, final int offsetX, final int offsetY) {
        int x = basePoint.getXCoordinate(this.dx, width) + offsetX;
        int y = basePoint.getYCoordinate(this.dy, height) + offsetY;

        mouseActions.mouseMove(basePointOfBaseDesktopElement, x, y);
    }

    /**
     * clicks element with offset
     * @param basePoint
     * @param offsetX
     * @param offsetY
     */
    public void clickToElementPoint(final BasePoint basePoint, final int offsetX, final int offsetY) {
        int x = basePoint.getXCoordinate(this.dx, width) + offsetX;
        int y = basePoint.getYCoordinate(this.dy, height) + offsetY;

        mouseActions.mouseClick(basePointOfBaseDesktopElement, x, y);
    }

    /**
     * rightclicks element with offset
     * @param basePoint
     * @param offsetX
     * @param offsetY
     */
    public void rightClickToElementPoint(final BasePoint basePoint, final int offsetX, final int offsetY) {
        int x = basePoint.getXCoordinate(this.dx, width) + offsetX;
        int y = basePoint.getYCoordinate(this.dy, height) + offsetY;

        mouseActions.mouseRightClick(basePointOfBaseDesktopElement, x, y);
    }

    /**
     * doubleclicks element with offset
     * @param basePoint
     * @param offsetX
     * @param offsetY
     */
    public void doubleClickToElementPoint(final BasePoint basePoint, final int offsetX, final int offsetY) {
        int x = basePoint.getXCoordinate(this.dx, width) + offsetX;
        int y = basePoint.getYCoordinate(this.dy, height) + offsetY;

        mouseActions.mouseDoubleClick(basePointOfBaseDesktopElement, x, y);
    }

    /**
     * clicks and clears element
     */
    public void clickAndClear() {
        clickToElementPoint(BasePoint.CENTER, 0, 0);
        ((DesktopDriver) baseDesktopElement.getWrappedDriver()).performKeyCombination(KeyCombination.CTRL_A_DELETE);
    }

    /**
     * returns text value of CoordinateElement
     * @return
     */
    public String getTextValue() {
        clickToElementPoint(BasePoint.CENTER, 0, 0);
        ((DesktopDriver) baseDesktopElement.getWrappedDriver()).performKeyCombination(KeyCombination.CTRL_A);
        ((DesktopDriver) baseDesktopElement.getWrappedDriver()).performKeyCombination(KeyCombination.CTRL_C);
        clickToElementPoint(BasePoint.CENTER, 0, 0);
        return ((DesktopDriver) baseDesktopElement.getWrappedDriver()).getClipboardText();
    }

    /**
     * sets text value of CoordinateElement
     * @param text
     */
    public void inputTextValue(final String text) {
        clickAndClear();
        if (((DesktopDriver) baseDesktopElement.getWrappedDriver()).getKeyboardLayout() != KeyboardLayout.ENG) {
            ((DesktopDriver) baseDesktopElement.getWrappedDriver()).setKeyboardLayout(KeyboardLayout.ENG);
        }
        ((DesktopDriver) baseDesktopElement.getWrappedDriver()).sendChars(text);
    }
}
