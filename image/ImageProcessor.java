package image;

import java.awt.*;


/**
 * A utility class for processing images, providing methods to manipulate and analyze images.
 * The class includes operations such as adding padding to an image, splitting an image into smaller
 * sub-images, and calculating the average brightness of an image based on the pixel's luminance.
 * It also implements caching to improve performance by storing brightness calculations.
 *
 * @author Yair Ben Yakar, Ofek Levy
 */
public class ImageProcessor {

    // Constants for padding color and brightness calculation weights
    private static final Color DEFAULT_PADDING_COLOR = new Color(255, 255, 255); // White color
    private static final double RED_WEIGHT = 0.2126;
    private static final double GREEN_WEIGHT = 0.7152;
    private static final double BLUE_WEIGHT = 0.0722;
    private static final double MAX_BRIGHTNESS = 255.0;
    private static final int NO_PREV_RESOLUTION = -1;
    private int prevResulotion;

    // Caching maps to store previous brightness calculations
    private double[][] prevBrightness;
    private Image prevImage;


    /**
     * Constructor for the ImageProcessor class.
     * Initializes the current and previous caches for storing image data.
     */
    public ImageProcessor() {
        this.prevBrightness= null;
        this.prevResulotion = NO_PREV_RESOLUTION;
        this.prevImage = null;
    }

    /**
     * Adds padding to the given image to make its dimensions the closest powers of 2.
     *
     * @param image The original image.
     * @return A new padded image with dimensions that are the closest powers of 2.
     */
    public Image addPadding(Image image) {
        int newHeight = calculateClosestPowerOf2(image.getHeight());
        int newWidth = calculateClosestPowerOf2(image.getWidth());
        int heightPadding = (newHeight - image.getHeight()) / 2;
        int widthPadding = (newWidth - image.getWidth()) / 2;

        Color[][] paddedPixelArray = initializePixelArray(newHeight, newWidth, DEFAULT_PADDING_COLOR);
        copyPixelsToPaddedArray(image, paddedPixelArray, heightPadding, widthPadding);

        return new Image(paddedPixelArray, newWidth, newHeight);
    }

    /**
     * Calculates the average brightness for sub-images of a given image, based on the specified resolution.
     * The image is split into smaller sub-images, and for each sub-image, the average brightness is
     * computed.
     * The method utilizes caching to avoid recalculating brightness for the same image and resolution
     * combination.
     * If the image and resolution are the same as the previous calculation, it returns the cached brightness
     * values.
     *
     * @param image The original image whose sub-images' brightness is to be calculated.
     * @param resolution The number of sub-images per row or column in the grid. Determines the resolution of
     *                   the sub-images.
     * @return A 2D array of brightness values, where each element corresponds to the brightness of a
     * sub-image.
     */
    public double[][] calculateSubImageBrightness(Image image, int resolution) {
        if (image.equals(prevImage) && resolution == prevResulotion) {
            return prevBrightness;
        }

        Image[][] subImages = splitIntoSubImages(image, resolution);
        double[][] brightnessValues = new double[subImages.length][subImages[0].length];

        for (int row = 0; row < subImages.length; row++) {
            for (int col = 0; col < subImages[0].length; col++) {
                brightnessValues[row][col] = calculateAverageBrightness(subImages[row][col]);
            }
        }

        prevImage = image;
        prevResulotion = resolution;
        prevBrightness = brightnessValues.clone();
        return brightnessValues;
    }

    /*
     * Splits the image into a grid of sub-images based on the specified number of sub-images per row.
     *
     * @param image               The original image.
     * @param resolution     The number of sub-images per row.
     * @return An array of sub-images.
     */
    private Image[][] splitIntoSubImages(Image image, int resolution) {
        int subImageSize = image.getWidth() / resolution;
        int subImagesPerCol = image.getHeight() / subImageSize;
        Image[][] subImages = new Image[resolution][subImagesPerCol];

        for (int row = 0; row < subImagesPerCol; row++) {
            int startX = row * subImageSize;
            for (int col = 0; col < resolution; col++) {
                int startY = col * subImageSize;
                subImages[row][col] = extractSubImage(startX, startY, subImageSize, image);
            }
        }

        return subImages;
    }


    /*
     * Calculates the average brightness of the given image.
     *
     * @param image The image whose brightness is to be calculated.
     * @return The average brightness as a normalized value between 0 and 1.
     */
    private double calculateAverageBrightness(Image image) {
        double totalBrightness = 0;
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                Color pixel = image.getPixel(row, col);
                totalBrightness += calculateLuminance(pixel);
            }
        }

        int totalPixels = image.getHeight() * image.getWidth();
        return totalBrightness / (MAX_BRIGHTNESS * totalPixels);
    }

    /*
     * Extracts a rectangular sub-image from the original image.
     */
    private Image extractSubImage(int startX, int startY, int subImageSize, Image image) {
        Color[][] subImagePixels = new Color[subImageSize][subImageSize];

        for (int row = 0; row < subImageSize; row++) {
            for (int col = 0; col < subImageSize; col++) {
                subImagePixels[row][col] = image.getPixel(startX + row, startY + col);
            }
        }

        return new Image(subImagePixels, subImageSize, subImageSize);
    }

    /*
     * Calculates the closest power of 2 greater than or equal to the given value.
     */
    private static int calculateClosestPowerOf2(int value) {
        if (value <= 0) {
            return 1; // Handle edge case for non-positive input
        }
        int power = (int) Math.ceil(Math.log(value) / Math.log(2));
        return (int) Math.pow(2, power);
    }

    /*
     * Initializes a 2D pixel array with the specified fill color.
     */
    private static Color[][] initializePixelArray(int height, int width, Color fillColor) {
        Color[][] pixelArray = new Color[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixelArray[row][col] = fillColor;
            }
        }
        return pixelArray;
    }

    /*
     * Copies pixels from the source image into the target padded array with the given offsets.
     */
    private static void copyPixelsToPaddedArray(Image sourceImage, Color[][] targetArray,
                                                int heightOffset, int widthOffset) {
        for (int row = 0; row < sourceImage.getHeight(); row++) {
            for (int col = 0; col < sourceImage.getWidth(); col++) {
                targetArray[row + heightOffset][col + widthOffset] = sourceImage.getPixel(row, col);
            }
        }
    }

    /*
     * Calculates the luminance of a given pixel based on its RGB components.
     */
    private static double calculateLuminance(Color pixel) {
        return pixel.getRed() * RED_WEIGHT +
                pixel.getGreen() * GREEN_WEIGHT +
                pixel.getBlue() * BLUE_WEIGHT;
    }
}
