package ascii_art;

/**
 * Contains all the constant values used throughout the ASCII Art program.
 * These constants define configuration settings for ASCII character ranges,
 * CLI arguments, commands, error messages, and output configurations, ensuring consistency
 * and providing a centralized location for modifying these values across the codebase.
 *
 * @author Yair Ben Yakar, Ofek Levy
 */
public class ShellConstants {

    // ASCII Range and Resolution Settings

    /**
     * The total number of ASCII characters used in the program (0-126)
     */
    public static final int ASCII_RANGE = 127;

    /**
     * The minimum value for printable ASCII characters (32, space)
     */
    public static final int ASCII_MIN = 32;

    /**
     * The maximum value for printable ASCII characters (126, ~)
     */
    public static final int ASCII_MAX = 126;

    /**
     * The character used to represent a space
     */
    public static final char CHAR_SPACE = ' ';

    /**
     * The default resolution for output representation
     */
    public static final int DEFAULT_RESOLUTION = 2;

    /**
     * The starting character for the initial charset (0)
     */
    public static final char START_INITIAL_CHAR = '0';

    /**
     * The ending character for the initial charset (9)
     */
    public static final char END_INITIAL_CHAR = '9';

    /**
     * The total number of initial characters in the charset (0-9)
     */
    public static final int INITIAL_CHAR_COUNT = 10;

    /**
     * The factor used to adjust the resolution
     */
    public static final int RESOLUTION_FACTOR = 2;

    /**
     * The number of parts in the output request
     */
    public static final int OUTPUT_REQUEST_PARTS_NUMBER = 2;

    // Command Line Interface (CLI) Settings

    /**
     * The index of the image name argument in the command line input
     */
    public static final int CLI_IMAGE_NAME_ARG = 0;

    /**
     * Error message when no CLI argument is provided
     */
    public static final String NO_CLI_ARG = "Please provide an image name as the argument.";

    // Command Constants

    /**
     * Command to exit the shell
     */
    public static final String CMD_EXIT = "exit";

    /**
     * Command to print the charset
     */
    public static final String CMD_PRINT_CHARSET = "chars";

    /**
     * Command to add characters to the charset
     */
    public static final String CMD_ADD_CHARSET = "add";

    /**
     * Command to remove characters from the charset
     */
    public static final String CMD_REMOVE_CHARSET = "remove";

    /**
     * Command to change the resolution
     */
    public static final String CMD_RESOLUTION = "res";

    /**
     * Command to increase resolution
     */
    public static final String CMD_UP = "up";

    /**
     * Command to decrease resolution
     */
    public static final String CMD_DOWN = "down";

    /**
     * Command to use absolute values
     */
    public static final String CMD_ABS = "abs";

    /**
     * Command to add all characters to the charset
     */
    public static final String CMD_ADD_ALL = "all";

    /**
     * Command to add the space character to the charset
     */
    public static final String CMD_ADD_SPACE = "space";

    /**
     * Command to round values in the output
     */
    public static final String CMD_ROUND = "round";

    /**
     * Command to run the ASCII Art algorithm
     */
    public static final String CMD_RUN_ALGORITHM = "asciiArt";

    /**
     * Command to specify the output method
     */
    public static final String CMD_OUTPUT = "output";

    /**
     * The minimum number of characters required to run the algorithm
     */
    public static final int MIN_CHARS_TO_RUM_ALGORITHM = 2;

    // Command Argument Numbers

    /**
     * The number of arguments required for the add/remove commands
     */
    public static final int NUMBER_OF_ADD_REMOVE_ARGS = 2;

    // Error Messages

    /**
     * The prompt shown to the user for input
     */
    public static final String PROMPT_INPUT = ">>> ";

    /**
     * Message shown when the resolution is successfully changed
     */
    public static final String MSG_RESOLUTION = "Resolution set to ";

    /**
     * Error message when the format for adding characters is incorrect
     */
    public static final String ERR_ADD_FORMAT = "Did not add due to incorrect format.";

    /**
     * Error message when the format for removing characters is incorrect
     */
    public static final String ERR_REMOVE_FORMAT = "Did not remove due to incorrect format.";

    /**
     * Error message when the resolution exceeds the defined boundaries
     */
    public static final String ERR_RESOLUTION_BOUNDARIES =
            "Did not change resolution due to exceeding boundaries.";

    /**
     * Error message when an unknown command is entered
     */
    public static final String ERR_UNKNOWN_COMMAND = "Did not execute due to incorrect command.";

    /**
     * General error message for unexpected issues
     */
    public static final String ERR_UNEXPECTED = "An unexpected error occurred.";

    /**
     * Error message when an image file cannot be opened
     */
    public static final String ERR_UNABLE_TO_OPEN_IMAGE = "Error: Unable to load image file.";

    /**
     * Error message when the output method format is incorrect
     */
    public static final String ERR_OUTPUT_CHANGE = "Did not change output method due to incorrect format.";

    /**
     * Error message when the charset is too small to execute
     */
    public static final String ERR_EXECUTION = "Did not execute. Charset is too small.";

    /**
     * Error message when the rounding format is incorrect
     */
    public static final String ERR_ROUND = "Did not change rounding method due to incorrect format.";

    /**
     * Error message when the resolution format is incorrect
     */
    public static final String ERR_RESOLUTION = "Did not change resolution due to incorrect format.";

    // Separators

    /**
     * Separator used between arguments in commands
     */
    public static final String ARG_SEPARATOR = " ";

    /**
     * Separator used for character ranges (e.g., A-Z)
     */
    public static final String CHAR_RANGE_SEPARATOR = "-";

    // Print Settings

    /**
     * Separator used when printing characters
     */
    public static final String CHARS_PRINT_SEPARATOR = " ";

    /**
     * Method used for console output
     */
    public static final String CONSOLE_OUTPUT_METHOD = "console";

    /**
     * Method used for HTML output
     */
    public static final String HTML_OUTPUT_METHOD = "html";

    /**
     * Font name used for the output
     */
    public static final String FONT_NAME = "New Courier";
}
