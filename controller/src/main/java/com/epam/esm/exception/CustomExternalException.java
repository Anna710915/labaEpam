package com.epam.esm.exception;

public class CustomExternalException extends RuntimeException{
    public CustomExternalException() {
    }

    public CustomExternalException(String message) {
        super(message);
    }

    public CustomExternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomExternalException(Throwable cause) {
        super(cause);
    }
}
