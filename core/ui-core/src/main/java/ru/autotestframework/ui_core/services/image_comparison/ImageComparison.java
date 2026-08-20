package ru.autotestframework.ui_core.services.image_comparison;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import ru.autotestframework.ui_core.services.image_comparison.models.ExcludedAreas;
import ru.autotestframework.ui_core.services.image_comparison.models.ImageComparisonResult;
import ru.autotestframework.ui_core.services.image_comparison.models.ImageComparisonState;
import ru.autotestframework.ui_core.services.image_comparison.models.Rectangle;
import ru.autotestframework.ui_core.services.image_comparison.utils.DrawUtils;
import ru.autotestframework.ui_core.services.image_comparison.utils.ImageComparisonUtils;
import ru.autotestframework.ui_core.services.image_comparison.utils.RectangleUtils;

/**
 * Image comparison.
 */
@Data
public class ImageComparison {

    /**
     * Expected image for comparison.
     */
    private final BufferedImage expected;

    /**
     * Actual image for comparison.
     */
    private final BufferedImage actual;

    /**
     * The threshold which means the max distance between non-equal pixels.
     * Could be changed according to the size and requirements of the image.
     */
    private int threshold = 5;

    /**
     * Width of the line that is drawn the rectangle.
     */
    private int rectangleLineWidth = 1;

    /**
     * File of the result destination.
     */
    private File destination;

    /**
     * The number which marks how many rectangles. Beginning from 2.
     */
    private int counter = 2;

    /**
     * The number of the marking specific rectangle.
     */
    private int regionCount = counter;

    /**
     * The number of the minimal rectangle size. Count as (width x height).
     */
    private Integer minimalRectangleSize = 1;

    /**
     * Maximal count of the Rectangles.
     * It means that would get the first x biggest rectangles.
     * Default value is -1, that means that all the rectangles would be drawn.
     */
    private Long maximalRectangleCount = -1L;

    /**
     * Level of the pixel tolerance. By default, it's 0.1 -> 10% difference.
     * The value can be set from 0.0 to 0.99.
     */
    private double pixelToleranceLevel;

    /**
     * Constant using for counting the level of the difference.
     */
    private double differenceConstant;

    /**
     * ExcludedAreas contains a List of Rectangles to be ignored when comparing images
     */
    private ExcludedAreas excludedAreas = new ExcludedAreas();

    /**
     * Flag which says draw excluded rectangles or not.
     */
    private boolean drawExcludedRectangles = false;

    /**
     * Flag for filling comparison difference rectangles.
     */
    private boolean fillDifferenceRectangles = false;

    /**
     * Sets the opacity percentage of the fill of comparison difference rectangles. 0.0 means completely transparent and 100.0 means completely opaque.
     */
    private double percentOpacityDifferenceRectangles = 20.0;

    /**
     * Flag for filling excluded rectangles.
     */
    private boolean fillExcludedRectangles = false;

    /**
     * Sets the opacity percentage of the fill of excluded rectangles. 0.0 means completely transparent and 100.0 means completely opaque.
     */
    private double percentOpacityExcludedRectangles = 20.0;

    /**
     * The percent of the allowing pixels to be different to stay {@link ImageComparisonState#MATCH} for comparison.
     * E.g. percent of the pixels, which would ignore in comparison.
     */
    private double allowingPercentOfDifferentPixels;

    /**
     * Sets rectangle color of image difference. By default, it's red.
     */
    private Color differenceRectangleColor = Color.RED;

    /**
     * Sets rectangle color of excluded part. By default, it's green.
     */
    private Color excludedRectangleColor = Color.GREEN;

    /**
     * Create a new instance of {@link ImageComparison} that can compare the given images.
     *
     * @param expected expected image to be compared
     * @param actual   actual image to be compared
     */
    public ImageComparison(final String expected, final String actual) {
        this(
                ImageComparisonUtils.readImageFromResources(expected),
                ImageComparisonUtils.readImageFromResources(actual),
                null);
    }

    /**
     * Create a new instance of {@link ImageComparison} that can compare the given images.
     *
     * @param expected expected image to be compared
     * @param actual   actual image to be compared
     */
    public ImageComparison(final BufferedImage expected, final BufferedImage actual) {
        this(expected, actual, null);
    }

    /**
     * Create a new instance of {@link ImageComparison} that can compare the given images.
     *
     * @param expected    expected image to be compared
     * @param actual      actual image to be compared
     * @param destination destination to save the result. If null, the result is shown in the UI.
     */
    public ImageComparison(final String expected, final String actual, final File destination) {
        this(
                ImageComparisonUtils.readImageFromResources(expected),
                ImageComparisonUtils.readImageFromResources(actual),
                destination);
    }

    /**
     * Create a new instance of {@link ImageComparison} that can compare the given images.
     *
     * @param expected    expected image to be compared
     * @param actual      actual image to be compared
     * @param destination destination to save the result. If null, the result is shown in the UI.
     */
    public ImageComparison(final BufferedImage expected, final BufferedImage actual, final File destination) {
        this.expected = expected;
        this.actual = actual;
        this.destination = destination;
        differenceConstant = calculateDifferenceConstant();
    }

    /**
     * Draw rectangles which cover the regions of the difference pixels.
     *
     * @return the result of the drawing.
     */
    public ImageComparisonResult compareImages() {
        if (isImageSizesNotEquals(expected, actual)) {
            BufferedImage actualResized =
                    ImageComparisonUtils.resize(actual, expected.getWidth(), expected.getHeight());
            return ImageComparisonResult.defaultSizeMisMatchResult(
                    expected, actual, ImageComparisonUtils.getDifferencePercent(actualResized, expected));
        }

        List<Rectangle> rectangleList = RectangleUtils.populateRectangles(this);

        if (rectangleList.isEmpty()) {
            var matchResult = ImageComparisonResult.defaultMatchResult(expected, actual);
            if (drawExcludedRectangles) {
                matchResult.setResult(DrawUtils.drawRectangles(rectangleList, this));
                saveImageForDestination(matchResult.getResult());
            }
            return matchResult;
        }

        BufferedImage resultImage = DrawUtils.drawRectangles(rectangleList, this);
        saveImageForDestination(resultImage);
        return ImageComparisonResult.defaultMisMatchResult(
                        expected, actual, ImageComparisonUtils.getDifferencePercent(actual, expected))
                .toBuilder()
                .result(resultImage)
                .rectangles(rectangleList)
                .build();
    }

    /**
     * Check images for equals their widths and heights.
     *
     * @param expected {@link BufferedImage} object of the expected image.
     * @param actual   {@link BufferedImage} object of the actual image.
     * @return true if image size are not equal, false otherwise.
     */
    private boolean isImageSizesNotEquals(final BufferedImage expected, final BufferedImage actual) {
        return expected.getHeight() != actual.getHeight() || expected.getWidth() != actual.getWidth();
    }

    /**
     * Save image to destination object if exists.
     *
     * @param image {@link BufferedImage} to be saved.
     */
    private void saveImageForDestination(final BufferedImage image) {
        if (Objects.nonNull(destination)) {
            ImageComparisonUtils.saveImage(destination, image);
        }
    }

    private double calculateDifferenceConstant() {
        return Math.pow(pixelToleranceLevel * Math.sqrt(Math.pow(0xff, 2) * 3), 2);
    }

    /**
     * Sets pixel tolerance level.
     *
     * @param pixelToleranceLevel the pixel tolerance level
     * @return the pixel tolerance level
     */
    public ImageComparison setPixelToleranceLevel(final double pixelToleranceLevel) {
        this.pixelToleranceLevel = pixelToleranceLevel;
        return this;
    }

    /**
     * Sets allowing percent of different pixels.
     *
     * @param allowingPercentOfDifferentPixels the allowing percent of different pixels
     * @return the allowing percent of different pixels
     */
    public ImageComparison setAllowingPercentOfDifferentPixels(final double allowingPercentOfDifferentPixels) {
        this.allowingPercentOfDifferentPixels = allowingPercentOfDifferentPixels;
        return this;
    }
}
