package ru.autotestframework.tests;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.Point;
import ru.autotestframework.desktop_elements.actions.TouchActions;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriver;

@Tag("@DesktopElements")
class TouchActionsTest {

    static DesktopDriver desktopDriver = Mockito.mock(DesktopDriver.class);
    static TouchActions touchActions = new TouchActions(desktopDriver);

    @Test
    void tapTest() {
        HashMap<String, Object> parameters = new HashMap<>();
        Point point1 = Mockito.mock(Point.class);
        Point point2 = Mockito.mock(Point.class);
        Point[] points = new Point[] {point1, point2};
        parameters.put("points", points);
        Mockito.doThrow(Error.class).when(desktopDriver).execute("touchActionsTap", parameters);
        Assertions.assertThrows(Error.class, () -> touchActions.tap(points));
    }

    @Test
    void holdTest() {
        HashMap<String, Object> parameters = new HashMap<>();
        Point point1 = Mockito.mock(Point.class);
        Point point2 = Mockito.mock(Point.class);
        Point[] points = new Point[] {point1, point2};
        parameters.put("points", points);
        parameters.put("duration", 777);
        Mockito.doThrow(Error.class).when(desktopDriver).execute("touchActionsHold", parameters);
        Assertions.assertThrows(Error.class, () -> touchActions.hold(777, points));
    }

    @Test
    void pinchTest() {
        HashMap<String, Object> parameters = new HashMap<>();
        Point point1 = Mockito.mock(Point.class);
        parameters.put("center", point1);
        parameters.put("startRadius", 1d);
        parameters.put("endRadius", 2d);
        parameters.put("duration", 3);
        parameters.put("angle", 4d);
        Mockito.doThrow(Error.class).when(desktopDriver).execute("touchActionsPinch", parameters);
        Assertions.assertThrows(Error.class, () -> touchActions.pinch(point1, 1, 2, 3, 4));
    }

    @Test
    void transitionTest() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("duration", 777);
        TouchActions.StartEndPoint startEndPoint1 = Mockito.mock(TouchActions.StartEndPoint.class);
        TouchActions.StartEndPoint startEndPoint2 = Mockito.mock(TouchActions.StartEndPoint.class);
        TouchActions.StartEndPoint[] startEndPoints = new TouchActions.StartEndPoint[] {startEndPoint1, startEndPoint2};
        parameters.put("startEndPoints", startEndPoints);
        Mockito.doThrow(Error.class).when(desktopDriver).execute("touchActionsTransition", parameters);
        Assertions.assertThrows(Error.class, () -> touchActions.transition(777, startEndPoints));
    }

    @Test
    void dragTest() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("duration", 777);
        TouchActions.StartEndPoint startEndPoint1 = Mockito.mock(TouchActions.StartEndPoint.class);
        TouchActions.StartEndPoint startEndPoint2 = Mockito.mock(TouchActions.StartEndPoint.class);
        TouchActions.StartEndPoint[] startEndPoints = new TouchActions.StartEndPoint[] {startEndPoint1, startEndPoint2};
        parameters.put("startEndPoints", startEndPoints);
        parameters.put("durationHold", 1);
        Mockito.doThrow(Error.class).when(desktopDriver).execute("touchActionsDrag", parameters);
        Assertions.assertThrows(Error.class, () -> touchActions.drag(777, startEndPoints, 1));
    }

    @Test
    void rotateTest() {
        HashMap<String, Object> parameters = new HashMap<>();
        Point point1 = Mockito.mock(Point.class);
        parameters.put("center", point1);
        parameters.put("radius", 1d);
        parameters.put("startAngle", 2d);
        parameters.put("endAngle", 3d);
        parameters.put("duration", 777);
        Mockito.doThrow(Error.class).when(desktopDriver).execute("touchActionsRotate", parameters);
        Assertions.assertThrows(Error.class, () -> touchActions.rotate(point1, 1, 2, 3, 777));
    }
}
