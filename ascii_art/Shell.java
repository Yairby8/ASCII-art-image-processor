package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.Arrays;

import static ascii_art.ShellConstants.*;

/**
 * The Shell class provides a command-line interface (CLI) for managing commands
 * related to ASCII art generation. It supports various operations such as
 * modifying the charset, adjusting resolution, changing output methods, and running
 * the ASCII art algorithm.
 *
 *
 * @author Yair Ben Yakar, Ofek Levy
 *
 */
public class Shell {

    // Instance Variables
    private int numberOfActiveChars;
    private int resolution;
    private final boolean[] activeCharset;
    private final SubImgCharMatcher charMatcher;
    private String outputMethod;


    /**
     * Constructor to initialize the shell with default settings.
     */
    public Shell() {
        this.activeCharset = new boolean[ASCII_RANGE];
        this.numberOfActiveChars = 0;
        this.charMatcher = initializeDefaultCharset();
        this.resolution = DEFAULT_RESOLUTION;
        this.outputMethod = CONSOLE_OUTPUT_METHOD;
    }

    /**
     * Main loop for handling commands related to ASCII art generation.
     *
     * @param imageName Path to the image file.
     */
    public void run(String imageName) {
        Image image;
        try {
            image = new Image(imageName);
        } catch (IOException e) {
            System.out.println(ERR_UNABLE_TO_OPEN_IMAGE);
            return;
        }

        int minCharsPerRow = Math.max(1, image.getWidth() / image.getHeight());
        int maxCharsPerRow = image.getWidth();
        ConsoleAsciiOutput consoleAsciiOutput = new ConsoleAsciiOutput();
        HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput(imageName, FONT_NAME);
        ImageProcessor imageProcessor = new ImageProcessor();

        while (true) {
            System.out.print(PROMPT_INPUT);
            String request = KeyboardInput.readLine();

            String[] requestParts = request.split(ARG_SEPARATOR);
            String command = requestParts[0];

            try {
                switch (command) {
                    case CMD_EXIT:
                        return;
                    case CMD_PRINT_CHARSET:
                        printActiveCharset();
                        break;
                    case CMD_ADD_CHARSET:
                        handleAddCharset(requestParts);
                        break;
                    case CMD_REMOVE_CHARSET:
                        handleRemoveCharset(requestParts);
                        break;
                    case CMD_RESOLUTION:
                        handleResolution(requestParts, minCharsPerRow, maxCharsPerRow);
                        break;
                    case CMD_ROUND:
                        handelRound(requestParts);
                        break;
                    case CMD_RUN_ALGORITHM:
                        handleRunAlgorithm(image, consoleAsciiOutput, htmlAsciiOutput, imageProcessor);
                        break;
                    case CMD_OUTPUT:
                        handleOutput(requestParts);
                        break;
                    default:
                        throw new UserInputCommandException(ERR_UNKNOWN_COMMAND);
                }
            } catch (UserInputFormatException e) {
                System.out.println(e.getMessage());
            } catch (UserInputCommandException e){
                System.out.println(e.getMessage());
            } catch (IllegalStateException e){
                System.out.println(e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(ERR_UNEXPECTED);
            }
        }
    }

    /*
     * Prints the currently active charset to the console.
     * Characters are separated by a defined separator.
     */
    private void printActiveCharset() {
        boolean first = true;
        for (char i = ASCII_MIN; i <= ASCII_MAX; i++) {
            if (activeCharset[i]) {
                if (!first) {
                    System.out.print(CHARS_PRINT_SEPARATOR);
                }
                System.out.print(i);
                first = false;
            }
        }
        System.out.println();
    }

    /*
     * Adds characters to the active charset based on the specified input.
     *
     * @param args Command arguments containing the characters or ranges to add.
     */
    private void handleAddCharset(String[] args) {
        updateCharset(args[1], true);
    }

    /*
     * Removes characters from the active charset based on the specified input.
     *
     * @param args Command arguments containing the characters or ranges to remove.
     */
    private void handleRemoveCharset(String[] args) {
        updateCharset(args[1], false);
    }

    /*
     * Updates the charset by adding or removing characters based on the specified input.
     *
     * @param input The input defining the characters or ranges to add/remove.
     * @param add   True to add characters; false to remove characters.
     * @throws UserInputFormatException If the input format is invalid.
     */
    private void updateCharset(String input, boolean add) throws UserInputFormatException {
        String errorMessage = ERR_ADD_FORMAT;
        if(!add){
            errorMessage = ERR_REMOVE_FORMAT;
        }
        if (CMD_ADD_ALL.equals(input)) {
            updateRangeCharset((char) ASCII_MIN, (char) ASCII_MAX, add);
        } else if (CMD_ADD_SPACE.equals(input)) {
            updateSingleChar(CHAR_SPACE, add);
        } else if (input.length() == 1) {
            updateSingleChar(input.charAt(0), add);
        } else if (input.contains(CHAR_RANGE_SEPARATOR)) {
            String[] rangeParts = input.split(CHAR_RANGE_SEPARATOR);
            if (rangeParts.length != NUMBER_OF_ADD_REMOVE_ARGS || rangeParts[0].length() != 1 ||
                    rangeParts[1].length() != 1) {
                throw new UserInputFormatException(errorMessage);
            }
            char start = rangeParts[0].charAt(0);
            char end = rangeParts[1].charAt(0);
            if (start > end) {
                char temp = start;
                start = end;
                end = temp;
            }
            updateRangeCharset(start, end, add);
        } else {
            throw new UserInputFormatException(errorMessage);
        }
    }

    /*
     * Updates a single character in the active charset.
     *
     * @param c   The character to update.
     * @param add True to add the character; false to remove it.
     * @throws IllegalArgumentException If the character is invalid.
     */
    private void updateSingleChar(char c, boolean add) throws IllegalArgumentException{
        boolean flag = false;
        if (activeCharset[c] != add) {
            flag = true;
            activeCharset[c] = add;
        }
        if (add) {
            charMatcher.addChar(c);
            if (flag) {
                numberOfActiveChars++;
            }
        } else {
            charMatcher.removeChar(c);
            if (flag){
                numberOfActiveChars--;
            }
        }
    }

    /*
     * Updates a range of characters in the active charset.
     *
     * @param start The starting character of the range.
     * @param end   The ending character of the range.
     * @param add   True to add the characters; false to remove them.
     */
    private void updateRangeCharset(char start, char end, boolean add) {
        for (char c = start; c <= end; c++) {
            updateSingleChar(c, add);
        }
    }



    /*
     * Handles the resolution adjustment commands based on user input.
     * Prints the current resolution or adjusts it up/down within the given bounds.
     *
     * @param args             The array of command-line arguments.
     * @param minCharsPerRow   The minimum allowed characters per row for resolution.
     * @param maxCharsPerRow   The maximum allowed characters per row for resolution.
     * @throws UserInputFormatException If the user provides an invalid command.
     * @throws IllegalStateException    If the resolution adjustment exceeds boundaries.
     */
    private void handleResolution(String[] args, int minCharsPerRow,
                                  int maxCharsPerRow) throws UserInputFormatException, IllegalStateException{
        if (args.length == 1) {
            System.out.println(MSG_RESOLUTION + resolution);
            return;
        }

        String action = args[1];
        if (CMD_UP.equals(action)) {
            if (resolution * RESOLUTION_FACTOR > maxCharsPerRow) {
                throw new IllegalStateException(ERR_RESOLUTION_BOUNDARIES);
            } else {
                resolution *= RESOLUTION_FACTOR;
                System.out.println(MSG_RESOLUTION + resolution);
            }
        } else if (CMD_DOWN.equals(action)) {
            if (resolution / RESOLUTION_FACTOR < minCharsPerRow) {
                throw new IllegalStateException(ERR_RESOLUTION_BOUNDARIES);
            } else {
                resolution /= RESOLUTION_FACTOR;
                System.out.println(MSG_RESOLUTION + resolution);
            }
        } else {
            throw new UserInputFormatException(ERR_RESOLUTION);
        }
    }

    /*
     * Handles changing the rounding method for brightness-to-character mapping.
     *
     * @param args Command arguments.
     * @throws UserInputFormatException If the input format is invalid.
     * @throws IllegalArgumentException If the rounding method is invalid.
     */
    private void handelRound(String[] args) throws UserInputFormatException, IllegalArgumentException{
        String command = args[1];
        switch (command) {
            case CMD_UP:
                charMatcher.changeRoundingMethod(CMD_UP);
                break;
            case CMD_DOWN:
                charMatcher.changeRoundingMethod(CMD_DOWN);
                break;
            case CMD_ABS:
                charMatcher.changeRoundingMethod(CMD_ABS);
                break;
            default:
                throw new UserInputFormatException(ERR_ROUND);
        }
    }

    /*
     * Runs the ASCII art generation algorithm with the current settings.
     *
     * @param image               The input image.
     * @param consoleAsciiOutput  Console output handler.
     * @param htmlAsciiOutput     HTML output handler.
     * @param imageProcessor      The image processor used for image manipulations.
     * @throws IllegalStateException If the minimum character set is not met.
     */
    private void handleRunAlgorithm(Image image, ConsoleAsciiOutput consoleAsciiOutput,
                                    HtmlAsciiOutput htmlAsciiOutput,
                                    ImageProcessor imageProcessor) throws IllegalStateException{
        if (numberOfActiveChars < MIN_CHARS_TO_RUM_ALGORITHM){
            throw new IllegalStateException(ERR_EXECUTION);
        }

        AsciiArtAlgorithm asciiArtAlgorithm =
                new AsciiArtAlgorithm(image, resolution, charMatcher, imageProcessor);
        char[][] pixelArray = asciiArtAlgorithm.run();
        if(outputMethod.equals(CONSOLE_OUTPUT_METHOD)){
            consoleAsciiOutput.out(pixelArray);
        }
        else{
            htmlAsciiOutput.out(pixelArray);
        }
    }


    /*
     * Handles changing the output method (console or HTML).
     *
     * @param requestParts Command arguments.
     * @throws UserInputFormatException If the input format is invalid.
     */
    private void handleOutput(String[] requestParts) throws UserInputFormatException{
        if (requestParts.length != OUTPUT_REQUEST_PARTS_NUMBER) {
            throw new UserInputFormatException(ERR_OUTPUT_CHANGE);
        }
        String newOutputMethod = requestParts[1];
        if (!newOutputMethod.equals(CONSOLE_OUTPUT_METHOD) && !newOutputMethod.equals(HTML_OUTPUT_METHOD)){
            throw new UserInputFormatException(ERR_OUTPUT_CHANGE);
        }
        outputMethod = newOutputMethod;

    }

    /*
     * Initializes the default charset with predefined characters.
     *
     * @return A SubImgCharMatcher initialized with the default charset.
     */
    private SubImgCharMatcher initializeDefaultCharset() {
        Arrays.fill(activeCharset, false);
        char[] initialChars = new char[INITIAL_CHAR_COUNT];
        int count = 0;
        for (char c = START_INITIAL_CHAR; c <= END_INITIAL_CHAR; c++) {
            activeCharset[c] = true;
            initialChars[count] = c;
            count++;
            numberOfActiveChars++;
        }
        return new SubImgCharMatcher(initialChars);
    }

    /**
     * Entry point for the program.
     *
     * @param args Command-line arguments. The first argument should be the image path.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(NO_CLI_ARG);
            return;
        }
        Shell shell = new Shell();
        String imageName = args[CLI_IMAGE_NAME_ARG];
        shell.run(imageName);
    }
}
