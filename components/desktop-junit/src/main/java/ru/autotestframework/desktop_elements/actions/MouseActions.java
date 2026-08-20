package ru.autotestframework.desktop_elements.actions;

import java.util.HashMap;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.desktop_elements.enums.BasePoint;

public class MouseActions {
    private final TypifiedDesktopElement element;
    private static final String ELEMENT_DRAG_AND_DROP = "elementDragAndDrop";
    private static final String ELEMENT_MOUSE_ACTION = "elementMouseAction";
    private static final String BASE_POINT = "basePoint";
    private static final String ACTION = "action";

    public MouseActions(final TypifiedDesktopElement element) {
        this.element = element;
    }

    /**
     * Drags and drops the mouse from the starting point (Base point of element bounding rectangle + x, y coordinates)
     * with the given distance.
     *
     * @param basePoint {@link BasePoint} of element bounding rectangle.
     * @param x X Coordinate relative to base point of element bounding rectangle.
     * @param y Y Coordinate relative to base point of element bounding rectangle.
     * @param dx The x distance to drag and drop, + for right, - for left.
     * @param dy The y distance to drag and drop, + for down, - for up.
     */
    public void dragAndDrop(final BasePoint basePoint, final int x, final int y, final int dx, final int dy) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("id", element.getId());
        parameters.put("x", x);
        parameters.put("y", y);
        parameters.put("dx", dx);
        parameters.put("dy", dy);
        parameters.put(BASE_POINT, basePoint.toString());

        element.exec(ELEMENT_DRAG_AND_DROP, parameters);
    }

    /**
     * moves mouse from base point to (x,y)
     * @param basePoint
     * @param x
     * @param y
     */
    public void mouseMove(final BasePoint basePoint, final int x, final int y) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("id", element.getId());
        parameters.put("x", x);
        parameters.put("y", y);
        parameters.put(BASE_POINT, basePoint.toString());
        parameters.put(ACTION, "move");

        element.exec(ELEMENT_MOUSE_ACTION, parameters);
    }

    /**
     * clicks mouse on point (x,y)
     * @param basePoint
     * @param x
     * @param y
     */
    public void mouseClick(final BasePoint basePoint, final int x, final int y) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("id", element.getId());
        parameters.put("x", x);
        parameters.put("y", y);
        parameters.put(BASE_POINT, basePoint.toString());
        parameters.put(ACTION, "click");

        element.exec(ELEMENT_MOUSE_ACTION, parameters);
    }

    /**
     * rightclicks mouse on point (x,y)
     * @param basePoint
     * @param x
     * @param y
     */
    public void mouseRightClick(final BasePoint basePoint, final int x, final int y) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("id", element.getId());
        parameters.put("x", x);
        parameters.put("y", y);
        parameters.put(BASE_POINT, basePoint.toString());
        parameters.put(ACTION, "rightClick");

        element.exec(ELEMENT_MOUSE_ACTION, parameters);
    }

    /**
     * doubleclicks mouse on point (x,y)
     * @param basePoint
     * @param x
     * @param y
     */
    public void mouseDoubleClick(final BasePoint basePoint, final int x, final int y) {
        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("id", element.getId());
        parameters.put("x", x);
        parameters.put("y", y);
        parameters.put(BASE_POINT, basePoint.toString());
        parameters.put(ACTION, "doubleClick");

        element.exec(ELEMENT_MOUSE_ACTION, parameters);
    }
}
