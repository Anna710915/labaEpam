package com.epam.esm.exception;

/**
 * The type Custom not valid argument exception extends RuntimeException class.
 * @author Anna Merkul
 */
public class CustomNotValidArgumentException extends RuntimeException{

    /**
     * Instantiates a new Custom not valid argument exception.
     *
     * @param message the message
     */
    public CustomNotValidArgumentException(String message) {
        super(message);
    }
}
