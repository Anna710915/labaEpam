package com.epam.esm.exception;

/**
 * The type Custom not found exception extend RuntimeException class.
 * @author Anna Merkul
 */
public class CustomNotFoundException extends RuntimeException{
    /**
     * Instantiates a new Custom not found exception.
     *
     * @param message the message
     */
    public CustomNotFoundException(String message) {
        super(message);
    }
}
