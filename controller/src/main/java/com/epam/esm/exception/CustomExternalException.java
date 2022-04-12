package com.epam.esm.exception;

/**
 * The type Custom external exception class extends RuntimeException class.
 * @author Anna Merkul
 */
public class CustomExternalException extends RuntimeException{
    /**
     * Instantiates a new Custom external exception.
     *
     * @param message the message
     */
    public CustomExternalException(String message) {
        super(message);
    }
}
