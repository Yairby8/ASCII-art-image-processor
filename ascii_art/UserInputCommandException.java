package ascii_art;

/**
 * Custom exception class for handling errors related to user input commands.
 * This exception is thrown when a user provides invalid or malformed input that
 * prevents the execution of a command.
 *
 * @author Yair Ben Yakar, Ofek Levy
 */
public class UserInputCommandException extends IllegalArgumentException {

    /**
     *  Default error message for user input command issues
     */
    private static final String DEFAULT_MESSAGE = "Did not execute.";

    /**
     * Constructs a UserInputCommandException with the default message.
     * This constructor uses the default message to indicate that the action could not be performed.
     */
    public UserInputCommandException() {
        // Calls the second constructor with the default action
        super(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a UserInputCommandException with a custom message.
     * This allows the exception to include a more specific message about
     * the action that the program was unable to perform.
     *
     * @param customMessage The custom error message that describes the issue.
     */
    public UserInputCommandException(String customMessage) {
        // Initializes the exception with a formatted message
        super(customMessage);
    }
}
