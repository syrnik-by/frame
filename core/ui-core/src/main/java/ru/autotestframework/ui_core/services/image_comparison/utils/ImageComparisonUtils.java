package ru.autotestframework.ui_core.services.image_comparison.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import lombok.experimental.UtilityClass;
import ru.autotestframework.ui_core.services.image_comparison.exceptions.ImageComparisonException;

/**
 * Image comparison utils.
 */
@UtilityClass
public class ImageComparisonUtils {

    /**
     * The constant SOFTEN_FACTOR.
     */
    public static final float SOFTEN_FACTOR = 0.05f;

    /**
     * Make a copy of the {@link BufferedImage} object.
     *
     * @param image the provided image.
     * @return copy of the provided image.
     */
    public BufferedImage deepCopy(final BufferedImage image) {
        var cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(image.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Read image from the provided path.
     *
     * @param path the path where contains image.
     * @return the {@link BufferedImage} object of this specific image.
     * @throws ImageComparisonException due to read the image from resources.
     */
    public BufferedImage readImageFromResources(final String path) {
        var imageFile = new File(path);
        BufferedImage image;
        if (imageFile.exists()) {
            try {
                image = ImageIO.read(imageFile);
            } catch (IOException e) {
                throw new ImageComparisonException(
                        String.format("Cannot read image from the file, path = %s", path), e);
            }
        } else {
            try (var inputStream = ImageComparisonUtils.class.getClassLoader().getResourceAsStream(path)) {
                image = ImageIO.read(inputStream);
            } catch (IOException e) {
                throw new ImageComparisonException(
                        String.format("Cannot read image from the file, path = %s", path), e);
            }
        }
        return image;
    }

    /**
     * Save image to the provided path.
     *
     * @param pathFile the path to the saving image.
     * @param image    the {@link BufferedImage} object of this specific image.
     * @throws ImageComparisonException due to save image.
     */
    public void saveImage(final File pathFile, final BufferedImage image) {
        var dir = pathFile.getParentFile();
        boolean dirExists = dir == null || dir.isDirectory() || dir.mkdirs();
        if (!dirExists) {
            throw new ImageComparisonException("Unable to create directory " + dir);
        }
        try {
            ImageIO.write(image, "png", pathFile);
        } catch (IOException e) {
            throw new ImageComparisonException(
                    String.format("Cannot save image to path = %s", pathFile.getAbsolutePath()), e);
        }
    }

    /**
     * Resize image to new dimensions and return new image.
     *
     * @param img  the object of the image to be resized.
     * @param newW the new width.
     * @param newH the new height.
     * @return resized {@link BufferedImage} object.
     */
    public BufferedImage resize(final BufferedImage img, final int newW, final int newH) {
        return toBufferedImage(img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH));
    }

    /**
     * Convert image to buffered image.
     *
     * @param img the object of the image to be converted to buffered image.
     * @return the converted buffered image.
     */
    public BufferedImage toBufferedImage(final Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        final var temp = new ImageIcon(img).getImage();
        final var bufferedImage =
                new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
        final Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        final float[] softenArray = {0, SOFTEN_FACTOR, 1 - (SOFTEN_FACTOR * 4), SOFTEN_FACTOR, 0, SOFTEN_FACTOR, 0};
        final var kernel = new Kernel(3, 3, softenArray);
        final var cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        return cOp.filter(bufferedImage, null);
    }

    /**
     * Return the difference in percent between two buffered images.
     *
     * @param img1 the first image.
     * @param img2 the second image.
     * @return difference percent.
     */
    public float getDifferencePercent(final BufferedImage img1, final BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();

        long diff = 0;

        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
            }
        }
        long maxDiff = 3L * 0xff * width * height;

        return (float) (100.0 * diff / maxDiff);
    }

    /**
     * Calculate Pixel RGB differential.
     *
     * @param rgb1 1st pixel
     * @param rgb2 2nd pixel
     * @return calculated difference
     */
    public int pixelDiff(final int rgb1, final int rgb2) {
        int r1 = (rgb1 >> RectangleUtils.RIGTH_SHIFT_FOR_RED) & 0xff;
        int g1 = (rgb1 >> RectangleUtils.RIGHT_SHIFT_FOR_BLUE) & 0xff;
        int b1 = rgb1 & 0xff;

        int r2 = (rgb2 >> RectangleUtils.RIGTH_SHIFT_FOR_RED) & 0xff;
        int g2 = (rgb2 >> RectangleUtils.RIGHT_SHIFT_FOR_BLUE) & 0xff;
        int b2 = rgb2 & 0xff;

        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }
}
