package ru.autotestframework.ui_core.services.image_comparison.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.autotestframework.ui_core.services.image_comparison.utils.ImageComparisonUtils;

/**
 * Data transfer objects which contains all the needed data for result of the comparison.
 */
@Builder(toBuilder = true)
@Getter
@Setter
public class ImageComparisonResult {

    /**
     * {@link BufferedImage} object of the expected.
     */
    private BufferedImage expected;

    /**
     * {@link BufferedImage} object of the actual.
     */
    private BufferedImage actual;

    /**
     * {@link BufferedImage} object of the comparison result.
     */
    private BufferedImage result;

    /**
     * State of the comparison.
     */
    private ImageComparisonState imageComparisonState;

    /**
     * The difference in percent between two images.
     */
    private float differencePercent;

    /**
     * Rectangles of the differences
     */
    private List<Rectangle> rectangles;

    /**
     * Create default instance of the {@link ImageComparisonResult} with {@link ImageComparisonState#SIZE_MISMATCH}.
     *
     * @param expected          expected {@link BufferedImage} object.
     * @param actual            actual {@link BufferedImage} object.
     * @param differencePercent the percent of the differences between images.
     * @return instance of the {@link ImageComparisonResult} object.
     */
    public static ImageComparisonResult defaultSizeMisMatchResult(
            final BufferedImage expected, final BufferedImage actual, final float differencePercent) {
        return new ImageComparisonResultBuilder()
                .imageComparisonState(ImageComparisonState.SIZE_MISMATCH)
                .differencePercent(differencePercent)
                .expected(expected)
                .actual(actual)
                .result(actual)
                .rectangles(Collections.emptyList())
                .build();
    }

    /**
     * Create default instance of the {@link ImageComparisonResult} with {@link ImageComparisonState#MISMATCH}.
     *
     * @param expected          expected {@link BufferedImage} object.
     * @param actual            actual {@link BufferedImage} object.
     * @param differencePercent the persent of the differences between images.
     * @return instance of the {@link ImageComparisonResult} object.
     */
    public static ImageComparisonResult defaultMisMatchResult(
            final BufferedImage expected, final BufferedImage actual, final float differencePercent) {
        return new ImageComparisonResultBuilder()
                .imageComparisonState(ImageComparisonState.MISMATCH)
                .differencePercent(differencePercent)
                .expected(expected)
                .actual(actual)
                .result(actual)
                .build();
    }

    /**
     * Create default instance of the {@link ImageComparisonResult} with {@link ImageComparisonState#MATCH}.
     *
     * @param expected expected {@link BufferedImage} object.
     * @param actual   actual {@link BufferedImage} object.
     * @return instance of the {@link ImageComparisonResult} object.
     */
    public static ImageComparisonResult defaultMatchResult(final BufferedImage expected, final BufferedImage actual) {
        return new ImageComparisonResultBuilder()
                .imageComparisonState(ImageComparisonState.MATCH)
                .expected(expected)
                .actual(actual)
                .result(actual)
                .rectangles(Collections.emptyList())
                .build();
    }

    /**
     * Write comparison result to File.
     *
     * @param file file to write in
     * @return self. image comparison result
     */
    public ImageComparisonResult writeResultTo(final File file) {
        ImageComparisonUtils.saveImage(file, result);
        return this;
    }
}
