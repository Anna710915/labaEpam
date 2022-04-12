package com.epam.esm.exception;

/**
 * The type Custom error class for generating message for the client
 * which contains a status code and a text message about problems.
 * @author Anna Merkul
 */
public class CustomError {
    private int code;
    private String message;

    /**
     * Instantiates a new Custom error.
     *
     * @param code    the code
     * @param message the message
     */
    public CustomError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
