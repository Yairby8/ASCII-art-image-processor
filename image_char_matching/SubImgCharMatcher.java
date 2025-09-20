package image_char_matching;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;

import static image_char_matching.CharConverter.convertToBoolArray;


/**
 * A class that matches sub-image brightness levels to ASCII characters based on their brightness.
 * It maps brightness values (calculated from characters) to the characters themselves, allowing efficient
 * retrieval of the closest match for a given brightness.
 *
 * Authors: Yair Ben Yakar, Ofek Levy
 */
public class SubImgCharMatcher {

    // Constants
    private static final double INVALID_BRIGHTNESS = -1.0;
    private static final double MIN_VALID_BRIGHTNESS = 0.0;
    private static final double MAX_VALID_BRIGHTNESS = 1.0;
    private static final String INVALID_BRIGHTNESS_ARGUMENT = "Brightness must be in the range [0, 1].";
    private static final int UPPER_LIMIT_OF_ASCII = 126;
    private static final int LOWER_LIMIT_OF_ASCII = 32;
    private static final String OUT_OF_ASCII_RANGE_MESSAGE = "Character must be in the range [32, 126].";
    private static final String EMPTY_LINKED_LIST_ERROR_MESSAGE =
            "The LinkedList of characters cannot be null or empty.";
    private static final String ABS = "abs";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String CHANGING_ROUND_METHOD_ERROR =
            "Invalid rounding method. Valid options are: 'abs', 'up', 'down'.";


    // Data structures for managing brightness mappings
    private final TreeMap<Double, LinkedList<Character>> brightnessToCharMap;
    private final double[] brightnessCache;
    private Double maxBrightness;
    private Double minBrightness;
    private String roundType;

    /**
     * Constructor for the SubImgCharMatcher class.
     * Initializes the mapping between character brightness and the character itself,
     * then normalizes the brightness values.
     *
     * @param charset The set of ASCII characters to be used for brightness mapping.
     */
    public SubImgCharMatcher(char[] charset) {
        this.brightnessCache = new double[UPPER_LIMIT_OF_ASCII + 1];
        this.roundType = ABS;
        Arrays.fill(brightnessCache, INVALID_BRIGHTNESS);

        this.brightnessToCharMap = new TreeMap<>();
        for (char character : charset) {
            double brightness = calculateCharBrightness(character);
            if(!brightnessToCharMap.containsKey(brightness)) {
                LinkedList<Character> chars = new LinkedList<>();
                chars.add(character);
                this.brightnessToCharMap.put(brightness, chars);
            }
            else {
                this.brightnessToCharMap.get(brightness).add(character);
            }

            this.brightnessCache[character] = brightness;
        }
        this.minBrightness = brightnessToCharMap.firstKey();
        this.maxBrightness = brightnessToCharMap.lastKey();
    }

    /**
     * Retrieves the character whose brightness is closest to the given brightness value.
     * If multiple characters have the same brightness, returns the one with the smallest ASCII value.
     *
     * @param brightness The brightness value (must be in the range [0, 1]).
     * @return The ASCII character with the closest brightness value.
     * @throws IllegalArgumentException If the brightness is not in the range [0, 1].
     */
    public char getCharByImageBrightness(double brightness) throws IllegalArgumentException {
        if (brightness < MIN_VALID_BRIGHTNESS || brightness > MAX_VALID_BRIGHTNESS) {
            throw new IllegalArgumentException(INVALID_BRIGHTNESS_ARGUMENT);
        }

        double denormalizedBrightness = denormalizeBrightness(brightness);

        // Find the closest brightness values
        Double lowerKey = brightnessToCharMap.floorKey(denormalizedBrightness);
        Double higherKey = brightnessToCharMap.ceilingKey(denormalizedBrightness);

        // Handle edge cases where only one key exists
        if (lowerKey == null) {
            return findSmallestChar(brightnessToCharMap.get(higherKey));
        }
        if (higherKey == null) {
            return findSmallestChar(brightnessToCharMap.get(lowerKey));
        }

        // Calculate distances to the lower and higher brightness values
        double lowerDistance = Math.abs(denormalizedBrightness - lowerKey);
        double higherDistance = Math.abs(denormalizedBrightness - higherKey);

        // Use the rounding type to determine which character to return
        switch (this.roundType) {
            case ABS:
                if (lowerDistance < higherDistance) {
                    return findSmallestChar(brightnessToCharMap.get(lowerKey));
                }
                return findSmallestChar(brightnessToCharMap.get(higherKey));

            case UP:
                return findSmallestChar(brightnessToCharMap.get(higherKey));

            default: // DOWN
                return findSmallestChar(brightnessToCharMap.get(lowerKey));
        }
    }

    /**
     * Changes the rounding method used by the algorithm.
     * Valid rounding methods are:
     *
     * "abs" - Rounds using absolute value rules.
     * "up" - Always rounds up.
     * "down" - Always rounds down.
     *
     *
     * @param roundingMethod A string representing the rounding method. Must be one of "abs", "up", or "down".
     * @throws IllegalArgumentException If the rounding method is invalid.
     */
    public void changeRoundingMethod(String roundingMethod) throws IllegalArgumentException{
        switch (roundingMethod.toLowerCase()) {
            case ABS:
                this.roundType = ABS;
                break;
            case UP:
                this.roundType = UP;
                break;
            case DOWN:
                this.roundType = DOWN;
                break;
            default:
                throw new IllegalArgumentException(CHANGING_ROUND_METHOD_ERROR);
        }
    }

    /**
     * Adds a character to the brightness map.
     *
     * @param character The character to add.
     * @throws IllegalArgumentException If the character is not in the range [32, 126].
     */
    public void addChar(char character) throws IllegalArgumentException{
        // Validate that the character is within the ASCII range
        if (character < LOWER_LIMIT_OF_ASCII || character > UPPER_LIMIT_OF_ASCII) {
            throw new IllegalArgumentException(OUT_OF_ASCII_RANGE_MESSAGE);
        }

        double brightness = INVALID_BRIGHTNESS;

        if (brightnessCache[character] != INVALID_BRIGHTNESS) {
            brightness = brightnessCache[character];
        } else {
            brightness = calculateCharBrightness(character);
            brightnessCache[character] = brightness;
        }

        if(!brightnessToCharMap.containsKey(brightness)) {
            LinkedList<Character> chars = new LinkedList<>();
            chars.add(character);
            this.brightnessToCharMap.put(brightness, chars);
        }
        else {
            this.brightnessToCharMap.get(brightness).add(character);
        }

        maxBrightness = Math.max(brightness, maxBrightness);
        minBrightness = Math.min(brightness, minBrightness);
    }

    /**
     * Removes a character from the brightness map.
     *
     * @param character The character to remove.
     * @throws IllegalArgumentException If the character is not in the range [32, 126].
     */
    public void removeChar(char character) throws IllegalArgumentException{
        // Validate that the character is within the ASCII range
        if (character < LOWER_LIMIT_OF_ASCII || character > UPPER_LIMIT_OF_ASCII) {
            throw new IllegalArgumentException(OUT_OF_ASCII_RANGE_MESSAGE);
        }

        if (brightnessCache[character] != INVALID_BRIGHTNESS) {
            double brightness = brightnessCache[character];
            int charIndex = brightnessToCharMap.get(brightness).indexOf(character);
            if (brightnessToCharMap.get(brightness).contains(character)) {
                brightnessToCharMap.get(brightness).remove(charIndex);
            }

            // Update min and max brightness if necessary
            if (brightness == maxBrightness) {
                maxBrightness = brightnessToCharMap.isEmpty() ?
                        Double.MIN_VALUE : brightnessToCharMap.lastKey();
            }
            if (brightness == minBrightness) {
                minBrightness = brightnessToCharMap.isEmpty() ?
                        Double.MAX_VALUE : brightnessToCharMap.firstKey();
            }
        }
    }

    /*
     * Finds and returns the character with the smallest ASCII value from a LinkedList.
     *
     * @param characters A LinkedList of characters to search.
     * @return The character with the smallest ASCII value.
     * @throws IllegalArgumentException If the LinkedList is empty.
     */
    private char findSmallestChar(LinkedList<Character> characters) throws  IllegalArgumentException{
        // Validate the input
        if (characters == null || characters.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_LINKED_LIST_ERROR_MESSAGE);
        }

        char smallestChar = characters.getFirst(); // Initialize with the first character

        // Iterate through the list to find the smallest character
        for (char c : characters) {
            if (c < smallestChar) {
                smallestChar = c;
            }
        }

        return smallestChar;
    }



    /*
     * Calculates the brightness of a given character based on its boolean pixel representation.
     */
    private double calculateCharBrightness(char character) {
        boolean[][] charPixelArray = convertToBoolArray(character);
        int whitePixelCount = 0;

        for (boolean[] row : charPixelArray) {
            for (boolean pixel : row) {
                if (pixel) {
                    whitePixelCount++;
                }
            }
        }

        int totalPixels = charPixelArray.length * charPixelArray[0].length;
        return ((double) whitePixelCount) / totalPixels;
    }

    /*
     * Normalizes a brightness value to a range between 0 and 1.
     */
    private double normalizeBrightness(double brightness) {
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    /*
     * Denormalizes a brightness value from a range of 0 to 1 back to its original scale.
     */
    private double denormalizeBrightness(double brightness) {
        return brightness * (maxBrightness - minBrightness) + minBrightness;
    }
}
