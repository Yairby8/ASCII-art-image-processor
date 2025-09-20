package ascii_art;

/**
 * Custom exception class for handling errors related to user input formats.
 * This exception is thrown when the user provides input in an incorrect format
 * that prevents the execution of a command or action.
 *
 * @author Yair Ben Yakar, Ofek Levy
 */
public class UserInputFormatException extends IllegalArgumentException {

    /**
     *Default error message for input format issues
     */
    private static final String DEFAULT_MESSAGE = "Failed to perform the action due to incorrect format.";

    /**
     * Constructs a UserInputFormatException with the default message.
     * This constructor provides a general error message indicating an incorrect input format.
     */
    public UserInputFormatException() {
        // Calls the second constructor with the default message
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a UserInputFormatException with a custom message.
     * This allows the exception to include a more specific message regarding
     * the issue with the input format.
     *
     * @param customMessage The custom error message that describes the issue with the input format.
     */
    public UserInputFormatException(String customMessage) {
        // Initializes the exception with a formatted message
        super(customMessage);
    }
}
