package ru.autotestframework.desktop_elements.actions;

import java.util.HashMap;
import org.openqa.selenium.Point;
import ru.autotestframework.desktop_elements.desktop_driver.DesktopDriver;

public class TouchActions {

    private DesktopDriver driver;

    private static final String DURATION = "duration";

    public TouchActions(final DesktopDriver driver) {
        this.driver = driver;
    }

    /**
     * Performs a tap on the given point or points.
     *
     * @param points The points that should be hold down.
     */
    public void tap(final Point[] points) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("points", points);

        driver.execute("touchActionsTap", parameters);
    }

    /**
     * Holds the touch on the given points for the given duration.
     *
     * @param duration The duration of the hold (in milliseconds).
     * @param points   The points that should be hold down.
     */
    public void hold(final Integer duration, final Point[] points) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("points", points);
        parameters.put(DURATION, duration);

        driver.execute("touchActionsHold", parameters);
    }

    /**
     * Performs a pinch with two fingers.
     *
     * @param center      The center point of the pinch.
     * @param startRadius The starting radius.
     * @param endRadius   The end radius.
     * @param duration    The duration of the action (in milliseconds).
     * @param angle       The angle of the two points, relative to the x-axis.
     */
    public void pinch(
            final Point center,
            final double startRadius,
            final double endRadius,
            final Integer duration,
            final double angle) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("center", center);
        parameters.put("startRadius", startRadius);
        parameters.put("endRadius", endRadius);
        parameters.put(DURATION, duration);
        parameters.put("angle", angle);

        driver.execute("touchActionsPinch", parameters);
    }

    /**
     * Transitions all the points from the start point to the end points.
     *
     * @param duration       The duration for the action (in milliseconds).
     * @param startEndPoints The list of start/end point tuples.
     */
    public void transition(final Integer duration, final StartEndPoint[] startEndPoints) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(DURATION, duration);
        parameters.put("startEndPoints", startEndPoints);

        driver.execute("touchActionsTransition", parameters);
    }

    /**
     * Performs a touch-drag from the start point to the end point.
     *
     * @param duration   The duration of the action (in milliseconds).
     * @param startEndPoints The list of start/end point tuples.
     * @param durationHold   The duration of the hold on start points (in milliseconds).
     */
    public void drag(final Integer duration, final StartEndPoint[] startEndPoints, final Integer durationHold) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(DURATION, duration);
        parameters.put("startEndPoints", startEndPoints);
        parameters.put("durationHold", durationHold);

        driver.execute("touchActionsDrag", parameters);
    }

    /**
     * Performs a 2-finger rotation around the given point where the first finger is at the center and
     * the second is rotated around.
     *
     * @param center     The center point of the rotation.
     * @param radius     The radius of the rotation.
     * @param startAngle The starting angle (in rad).
     * @param endAngle   The ending angle (in rad).
     * @param duration   The total duration for the transition (in milliseconds).
     */
    public void rotate(
            final Point center,
            final double radius,
            final double startAngle,
            final double endAngle,
            final Integer duration) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("center", center);
        parameters.put("radius", radius);
        parameters.put("startAngle", startAngle);
        parameters.put("endAngle", endAngle);
        parameters.put(DURATION, duration);

        driver.execute("touchActionsRotate", parameters);
    }

    public static class StartEndPoint {
        private Integer x1;
        private Integer y1;
        private Integer x2;
        private Integer y2;

        private Point startPoint;
        private Point endPoint;

        /**
         * Tuple Object for points.
         * @param startPoint first point
         * @param endPoint   second point
         */
        public StartEndPoint(final Point startPoint, final Point endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;

            this.x1 = startPoint.getX();
            this.y1 = startPoint.getY();
            this.x2 = endPoint.getX();
            this.y2 = endPoint.getY();
        }

        public Integer getX1() {
            return x1;
        }

        public Integer getY1() {
            return y1;
        }

        public Integer getX2() {
            return x2;
        }

        public Integer getY2() {
            return y2;
        }

        public Point getStartPoint() {
            return startPoint;
        }

        public Point getEndPoint() {
            return endPoint;
        }
    }
}
