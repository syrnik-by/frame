package ru.autotestframework.ui_core.services.image_comparison.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import ru.autotestframework.ui_core.services.image_comparison.ImageComparison;
import ru.autotestframework.ui_core.services.image_comparison.models.Rectangle;

/**
 * Draw utils.
 */
@UtilityClass
public class DrawUtils {

    private ImageComparison imageComparison;

    /**
     * Draw the rectangles based on collection of the rectangles and result image.
     *
     * @param rectangles             the collection of the {@link Rectangle} objects.
     * @param currentImageComparison the current image comparison
     * @return result {@link BufferedImage} with drawn rectangles.
     */
    public BufferedImage drawRectangles(
            final List<Rectangle> rectangles, final ImageComparison currentImageComparison) {
        imageComparison = currentImageComparison;
        BufferedImage resultImage = ImageComparisonUtils.deepCopy(imageComparison.getActual());
        var graphics2D = preparedGraphics2D(resultImage);

        drawExcludedRectangles(graphics2D);
        drawRectanglesOfDifference(rectangles, graphics2D);

        return resultImage;
    }

    /**
     * Prepare {@link Graphics2D} based on resultImage and rectangleLineWidth
     *
     * @param image image based on created {@link Graphics2D}.
     * @return prepared {@link Graphics2D} object.
     */
    private Graphics2D preparedGraphics2D(final BufferedImage image) {
        var graphics2D = image.createGraphics();
        graphics2D.setStroke(new BasicStroke(imageComparison.getRectangleLineWidth()));
        return graphics2D;
    }

    /**
     * Draw excluded rectangles.
     *
     * @param graphics2D prepared {@link Graphics2D}object.
     */
    private void drawExcludedRectangles(final Graphics2D graphics2D) {
        if (imageComparison.isDrawExcludedRectangles()) {
            graphics2D.setColor(imageComparison.getExcludedRectangleColor());
            draw(graphics2D, imageComparison.getExcludedAreas().getExcluded());

            if (imageComparison.isFillExcludedRectangles()) {
                fillRectangles(
                        graphics2D,
                        imageComparison.getExcludedAreas().getExcluded(),
                        imageComparison.getPercentOpacityExcludedRectangles());
            }
        }
    }

    /**
     * Draw rectangles with the differences.
     *
     * @param rectangles the collection of the {@link Rectangle} of differences.
     * @param graphics2D   prepared {@link Graphics2D}object.
     */
    private void drawRectanglesOfDifference(final List<Rectangle> rectangles, final Graphics2D graphics2D) {
        List<Rectangle> rectanglesForDraw;
        graphics2D.setColor(imageComparison.getDifferenceRectangleColor());

        if (imageComparison.getMaximalRectangleCount() > 0
                && imageComparison.getMaximalRectangleCount() < rectangles.size()) {
            rectanglesForDraw = rectangles.stream()
                    .sorted(Comparator.comparing(Rectangle::size))
                    .skip(rectangles.size() - imageComparison.getMaximalRectangleCount())
                    .collect(Collectors.toList());
        } else {
            rectanglesForDraw = new ArrayList<>(rectangles);
        }

        draw(graphics2D, rectanglesForDraw);

        if (imageComparison.isFillDifferenceRectangles()) {
            fillRectangles(graphics2D, rectanglesForDraw, imageComparison.getPercentOpacityDifferenceRectangles());
        }
    }

    /**
     * Draw rectangles based on collection of the {@link Rectangle} and {@link Graphics2D}.
     * getWidth/getHeight return real width/height,
     * so need to draw rectangle on one px smaller because minpoint + width/height is point on excluded pixel.
     *
     * @param graphics2D   the {@link Graphics2D} object for drawing.
     * @param rectangles the collection of the {@link Rectangle}.
     */
    private void draw(final Graphics2D graphics2D, final List<Rectangle> rectangles) {
        rectangles.forEach(rectangle -> graphics2D.drawRect(
                rectangle.getMinPoint().x,
                rectangle.getMinPoint().y,
                rectangle.getWidth() - 1,
                rectangle.getHeight() - 1));
    }

    /**
     * Fill rectangles based on collection of the {@link Rectangle} and {@link Graphics2D}.
     * getWidth/getHeight return real width/height,
     * so need to draw rectangle fill two px smaller to fit inside rectangle borders.
     *
     * @param graphics2D     the {@link Graphics2D} object for drawing.
     * @param rectangles     rectangles the collection of the {@link Rectangle}.
     * @param percentOpacity the opacity of the fill.
     */
    private void fillRectangles(
            final Graphics2D graphics2D, final List<Rectangle> rectangles, final double percentOpacity) {
        graphics2D.setColor(new Color(
                graphics2D.getColor().getRed(),
                graphics2D.getColor().getGreen(),
                graphics2D.getColor().getBlue(),
                (int) (percentOpacity / 100 * 0xff)));

        rectangles.forEach(rectangle -> graphics2D.fillRect(
                rectangle.getMinPoint().x - 1,
                rectangle.getMinPoint().y - 1,
                rectangle.getWidth() - 2,
                rectangle.getHeight() - 2));
    }
}
