package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * A package-private class representing an image with methods to load, access pixels, and save the image.
 * The image is represented by a 2D array of Color objects.
 * @author Dan Nirel
 */
public class Image {

    private static final String IMAGE_FORMAT = "jpeg";
    private static final String FILE_EXTENSION = ".jpeg";

    private final Color[][] pixelArray;
    private final int width;
    private final int height;
    private final int hashValue;

    /**
     * Constructor to load an image from a file.
     *
     * @param filename The path to the image file to load.
     * @throws IOException if the image cannot be read from the file.
     */
    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();

        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }
        this.hashValue = Arrays.deepHashCode(pixelArray);
    }

    /**
     * Constructor to create an image from a pixel array, width, and height.
     *
     * @param pixelArray The 2D array of Color objects representing the image pixels.
     * @param width The width of the image.
     * @param height The height of the image.
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
        this.hashValue = Arrays.deepHashCode(pixelArray);
    }

    /**
     * Gets the width of the image.
     *
     * @return The width of the image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the image.
     *
     * @return The height of the image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the pixel color at a specified (x, y) coordinate.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @return The Color object representing the pixel color.
     */
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * Saves the image to a file.
     *
     * @param fileName The name of the file (without extension) to save the image as.
     */
    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName + FILE_EXTENSION);
        try {
            ImageIO.write(bufferedImage, IMAGE_FORMAT, outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates the hash code for the image.
     *
     * @return The hash code representing the image.
     */
    @Override
    public int hashCode() {
        return hashValue;
    }

    /**
     * Compares the image with another object for equality.
     *
     * @param object The object to compare with.
     * @return true if the object is equal to this image, false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true; // Check for reference equality

        if (!(object instanceof image.Image)) return false; // Check for class compatibility and null

        // Cast the object to the correct type
        image.Image otherImage = (image.Image) object;

        // Compare width and height using appropriate getters
        if (getWidth() != otherImage.getWidth() || getHeight() != otherImage.getHeight()) {
            return false;
        }

        // Compare the pixel arrays for equality
        return Arrays.deepEquals(pixelArray, otherImage.pixelArray);
    }

}
