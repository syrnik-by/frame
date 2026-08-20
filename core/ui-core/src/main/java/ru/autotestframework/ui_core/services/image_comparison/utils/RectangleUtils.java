package ru.autotestframework.ui_core.services.image_comparison.utils;

import java.awt.Point;
import java.util.*;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import ru.autotestframework.ui_core.services.image_comparison.ImageComparison;
import ru.autotestframework.ui_core.services.image_comparison.models.Rectangle;

/**
 * Rectangle utils.
 */
@UtilityClass
public class RectangleUtils {

    /**
     * The constant RIGTH_SHIFT_FOR_RED.
     */
    public static final int RIGTH_SHIFT_FOR_RED = 16;
    /**
     * The constant RIGHT_SHIFT_FOR_BLUE.
     */
    public static final int RIGHT_SHIFT_FOR_BLUE = 8;

    /**
     * Matrix YxX => int[y][x].
     * E.g.:
     * | X - width ----
     * | .....................................
     * Y . (0, 0)                            .
     * | .                                   .
     * | .                                   .
     * h .                                   .
     * e .                                   .
     * i .                                   .
     * g .                                   .
     * h .                                   .
     * t .                             (X, Y).
     * | .....................................
     */
    private int[][] matrix;

    private ImageComparison imageComparison;

    /**
     * Populate rectangles of the differences
     *
     * @param currentsImageComparison the currents image comparison
     * @return the collection of the populated {@link Rectangle} objects.
     */
    public List<Rectangle> populateRectangles(final ImageComparison currentsImageComparison) {
        imageComparison = currentsImageComparison;
        long countOfDifferentPixels = populateTheMatrixOfTheDifference(imageComparison);

        if (countOfDifferentPixels == 0) {
            return Collections.emptyList();
        }

        if (isAllowedPercentOfDifferentPixels(countOfDifferentPixels)) {
            return Collections.emptyList();
        }

        groupRegions();

        Map<Integer, Rectangle> regions = new LinkedHashMap<>();
        for (int i = imageComparison.getCounter(); i < imageComparison.getRegionCount(); i++) {
            regions.put(i, Rectangle.createDefault());
        }
        createRectangles(imageComparison.getCounter(), regions);
        List<Rectangle> rectangles = regions.values().stream()
                .filter(rectangle -> !rectangle.equals(Rectangle.createDefault())
                        && rectangle.size() >= imageComparison.getMinimalRectangleSize())
                .collect(Collectors.toList());

        return mergeRectangles(mergeRectangles(rectangles));
    }

    /**
     * Populate binary matrix with "0" and "1". If the pixels are different set it as "1", otherwise "0".
     *
     * @return the count of different pixels
     */
    private long populateTheMatrixOfTheDifference(final ImageComparison imageComparison) {
        long countOfDifferentPixels = 0;
        matrix = new int[imageComparison.getExpected().getHeight()]
                [imageComparison.getExpected().getWidth()];
        for (var y = 0; y < imageComparison.getExpected().getHeight(); y++) {
            for (var x = 0; x < imageComparison.getExpected().getWidth(); x++) {
                if (!imageComparison.getExcludedAreas().contains(new Point(x, y))) {
                    if (isDifferentPixels(
                            imageComparison.getExpected().getRGB(x, y),
                            imageComparison.getActual().getRGB(x, y))) {
                        matrix[y][x] = 1;
                        countOfDifferentPixels++;
                    }
                }
            }
        }
        return countOfDifferentPixels;
    }

    /**
     * Say if the two pixels equal or not. The rule is the difference between two pixels
     * need to be more than pixelToleranceLevel.
     *
     * @param expectedRgb the RGB value of the Pixel of the Expected image.
     * @param actualRgb   the RGB value of the Pixel of the Actual image.
     * @return {@code true} if they' are difference, {@code false} otherwise.
     */
    private boolean isDifferentPixels(final int expectedRgb, final int actualRgb) {
        if (expectedRgb == actualRgb) {
            return false;
        } else if (imageComparison.getPixelToleranceLevel() == 0.0) {
            return true;
        }

        double r1 = (expectedRgb >> RIGTH_SHIFT_FOR_RED) & 0xff;
        double g1 = (expectedRgb >> RIGHT_SHIFT_FOR_BLUE) & 0xff;
        double b1 = expectedRgb & 0xff;

        double r2 = (actualRgb >> RIGTH_SHIFT_FOR_RED) & 0xff;
        double g2 = (actualRgb >> RIGHT_SHIFT_FOR_BLUE) & 0xff;
        double b2 = actualRgb & 0xff;

        return (Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2))
                > imageComparison.getDifferenceConstant();
    }

    /**
     * Say if provided {@param countOfDifferentPixels} is allowed for ImageComparisonState#MATCH state.
     *
     * @param countOfDifferentPixels the count of the different pixels in comparison.
     * @return true, if percent of different pixels lower or equal allowingPercentOfDifferentPixels,
     * false - otherwise.
     */
    private boolean isAllowedPercentOfDifferentPixels(final long countOfDifferentPixels) {
        long totalPixelCount = ((long) matrix.length) * ((long) matrix[0].length);
        double actualPercentOfDifferentPixels = ((double) countOfDifferentPixels / (double) totalPixelCount) * 100;
        return actualPercentOfDifferentPixels <= imageComparison.getAllowingPercentOfDifferentPixels();
    }

    /**
     * Group rectangle regions in matrix.
     */
    private void groupRegions() {
        for (var y = 0; y < matrix.length; y++) {
            for (var x = 0; x < matrix[y].length; x++) {
                if (matrix[y][x] == 1) {
                    joinToRegion(x, y);
                    imageComparison.setRegionCount(imageComparison.getRegionCount() + 1);
                }
            }
        }
    }

    /**
     * The recursive method which go to all directions and finds difference
     * in binary matrix using {@code threshold} for setting max distance between values which equal "1".
     * and set the {@code groupCount} to matrix.
     *
     * @param x the value of the X-coordinate.
     * @param y the value of the Y-coordinate.
     */
    private void joinToRegion(final int x, final int y) {
        if (isJumpToRejected(x, y)) {
            return;
        }

        matrix[y][x] = imageComparison.getRegionCount();

        for (var i = 0; i < imageComparison.getThreshold(); i++) {
            joinToRegion(x + 1 + i, y);
            joinToRegion(x, y + 1 + i);

            joinToRegion(x + 1 + i, y - 1 - i);
            joinToRegion(x - 1 - i, y + 1 + i);
            joinToRegion(x + 1 + i, y + 1 + i);
        }
    }

    /**
     * Check next step valid or not.
     *
     * @param x X-coordinate of the image.
     * @param y Y-coordinate of the image
     * @return true if jump rejected, otherwise false.
     */
    private boolean isJumpToRejected(final int x, final int y) {
        return y < 0 || y >= matrix.length || x < 0 || x >= matrix[y].length || matrix[y][x] != 1;
    }

    /**
     * Create a {@link Rectangle} object.
     *
     * @return the {@link Rectangle} object.
     */
    private void createRectangles(int counter, Map<Integer, Rectangle> rectangles) {
        var rectangle = Rectangle.createDefault();
        for (var y = 0; y < matrix.length; y++) {
            for (var x = 0; x < matrix[0].length; x++) {
                if (matrix[y][x] >= imageComparison.getCounter()) {
                    updateRectangleCreation(rectangles.get(matrix[y][x]), x, y);
                }
            }
        }
    }

    /**
     * Update {@link Point} of the rectangle based on x and y coordinates.
     */
    private void updateRectangleCreation(final Rectangle rectangle, final int x, final int y) {
        if (x < rectangle.getMinPoint().getX()) {
            rectangle.getMinPoint().x = x;
        }
        if (x > rectangle.getMaxPoint().getX()) {
            rectangle.getMaxPoint().x = x;
        }
        if (y < rectangle.getMinPoint().getY()) {
            rectangle.getMinPoint().y = y;
        }
        if (y > rectangle.getMaxPoint().getY()) {
            rectangle.getMaxPoint().y = y;
        }
    }

    /**
     * Find overlapping rectangles and merge them.
     */
    private List<Rectangle> mergeRectangles(final List<Rectangle> rectangles) {
        var position = 0;
        while (position < rectangles.size()) {
            if (rectangles.get(position).equals(Rectangle.createZero())) {
                position++;
            }
            for (int i = 1 + position; i < rectangles.size(); i++) {
                var r1 = rectangles.get(position);
                var r2 = rectangles.get(i);
                if (r2.equals(Rectangle.createZero())) {
                    continue;
                }
                if (r1.isOverlapping(r2)) {
                    rectangles.set(position, r1.merge(r2));
                    r2.makeZeroRectangle();
                    if (position != 0) {
                        position--;
                    }
                }
            }
            position++;
        }
        return rectangles.stream()
                .filter(it -> !it.equals(Rectangle.createZero()))
                .collect(Collectors.toList());
    }
}
