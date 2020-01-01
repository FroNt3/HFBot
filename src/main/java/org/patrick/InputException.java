package org.patrick;

public class InputException extends Exception {
    
    private static final long serialVersionUID = -6308404507031203906L;

    /**
     * Constructor for an InputException which is thrown in case a user input is invalid.
     *
     * @param message The error message the user is supposed to receive
     */
    public InputException(String message) {
        super(message);
    }
}
