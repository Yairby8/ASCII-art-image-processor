package ascii_art;

import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

/**
 * The AsciiArtAlgorithm class converts an image into ASCII art representation.
 * It processes an input image, calculates brightness levels for sub-images, and maps them to ASCII characters
 * based on their brightness.
 *
 * Authors: Yair Ben Yakar, Ofek Levy
 */
public class AsciiArtAlgorithm {

    private final Image inputImage; // The input image to be converted
    private final int resolution; // Number of sub-images per row
    private final SubImgCharMatcher subImgCharMatcher;
    private final ImageProcessor imageProcessor;

    /**
     * Constructor for the AsciiArtAlgorithm class.
     * Initializes the algorithm with the given input image, number of sub-images per row,
     * a character matcher, and an image processor.
     *
     * @param inputImage       The input image to be converted to ASCII art.
     * @param resolution  The number of sub-images in each row.
     * @param subImgCharMatcher The matcher to map sub-images to ASCII characters.
     * @param imageProcessor   The image processor used for image manipulations.
     */
    public AsciiArtAlgorithm(Image inputImage, int resolution, SubImgCharMatcher subImgCharMatcher,
                             ImageProcessor imageProcessor) {
        this.inputImage = inputImage;
        this.resolution = resolution;
        // Initialize character matcher using the provided charset
        this.subImgCharMatcher = subImgCharMatcher;
        this.imageProcessor = imageProcessor;
    }

    /**
     * Executes the ASCII art conversion algorithm.
     * This method processes the input image, calculates brightness values for sub-images,
     * and maps those values to corresponding ASCII characters.
     *
     * @return A 2D character array representing the ASCII art.
     */
    public char[][] run() {
        // Add padding to the image to ensure dimensions are a power of 2
        Image paddedImage = imageProcessor.addPadding(inputImage);

        // Divide the padded image into sub-images (tiles) and calculate brightness for each sub-image
        double[][] brightnessValues = imageProcessor.calculateSubImageBrightness(inputImage, resolution);

        // Map brightness values to characters and build the ASCII art
        return mapBrightnessToAsciiArt(brightnessValues, subImgCharMatcher);
    }

    /*
     * Maps brightness values to ASCII characters and builds a 2D character array representing the ASCII art.
     */
    private char[][] mapBrightnessToAsciiArt(double[][] brightnessValues, SubImgCharMatcher charMatcher) {
        char[][] asciiArt = new char[brightnessValues.length][brightnessValues[0].length];

        for (int row = 0; row < brightnessValues.length; row++) {
            for (int col = 0; col < brightnessValues[0].length; col++) {
                asciiArt[row][col] = charMatcher.getCharByImageBrightness(brightnessValues[row][col]);
            }
        }
        return asciiArt;
    }
}
