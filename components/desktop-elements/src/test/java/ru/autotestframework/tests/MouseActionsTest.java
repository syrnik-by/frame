package ru.autotestframework.tests;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.autotestframework.desktop_elements.actions.MouseActions;
import ru.autotestframework.desktop_elements.elements.typified.TypifiedDesktopElement;
import ru.autotestframework.desktop_elements.enums.BasePoint;

@Tag("@DesktopElements")
class MouseActionsTest {

    static TypifiedDesktopElement typifiedDesktopElement = Mockito.mock(TypifiedDesktopElement.class);
    static MouseActions mouseActions = new MouseActions(typifiedDesktopElement);

    @Test
    void dragAndDropTest() {
        Mockito.when(typifiedDesktopElement.getId()).thenReturn("777");
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", "777");
        parameters.put("x", 1);
        parameters.put("y", 2);
        parameters.put("dx", 3);
        parameters.put("dy", 4);
        parameters.put("basePoint", BasePoint.TOP_LEFT.toString());
        Mockito.doThrow(Error.class).when(typifiedDesktopElement).exec("elementDragAndDrop", parameters);
        Assertions.assertThrows(Error.class, () -> mouseActions.dragAndDrop(BasePoint.TOP_LEFT, 1, 2, 3, 4));
    }

    @Test
    void mouseMoveTest() {
        Mockito.when(typifiedDesktopElement.getId()).thenReturn("777");
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", "777");
        parameters.put("x", 1);
        parameters.put("y", 2);
        parameters.put("basePoint", BasePoint.TOP_LEFT.toString());
        parameters.put("action", "move");
        Mockito.doThrow(Error.class).when(typifiedDesktopElement).exec("elementMouseAction", parameters);
        Assertions.assertThrows(Error.class, () -> mouseActions.mouseMove(BasePoint.TOP_LEFT, 1, 2));
    }

    @Test
    void mouseClickTest() {
        Mockito.when(typifiedDesktopElement.getId()).thenReturn("777");
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", "777");
        parameters.put("x", 1);
        parameters.put("y", 2);
        parameters.put("basePoint", BasePoint.TOP_LEFT.toString());
        parameters.put("action", "click");
        Mockito.doThrow(Error.class).when(typifiedDesktopElement).exec("elementMouseAction", parameters);
        Assertions.assertThrows(Error.class, () -> mouseActions.mouseClick(BasePoint.TOP_LEFT, 1, 2));
    }

    @Test
    void mouseRightClickTest() {
        Mockito.when(typifiedDesktopElement.getId()).thenReturn("777");
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", "777");
        parameters.put("x", 1);
        parameters.put("y", 2);
        parameters.put("basePoint", BasePoint.TOP_LEFT.toString());
        parameters.put("action", "rightClick");
        Mockito.doThrow(Error.class).when(typifiedDesktopElement).exec("elementMouseAction", parameters);
        Assertions.assertThrows(Error.class, () -> mouseActions.mouseRightClick(BasePoint.TOP_LEFT, 1, 2));
    }
}
