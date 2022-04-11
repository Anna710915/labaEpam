package com.epam.esm.exception;

public class CustomNotValidArgumentException extends RuntimeException{

    public CustomNotValidArgumentException() {
    }

    public CustomNotValidArgumentException(String message) {
        super(message);
    }

    public CustomNotValidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomNotValidArgumentException(Throwable cause) {
        super(cause);
    }
}
