package com.epam.esm.controller;

import com.epam.esm.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Exception controller.
 */
@RestControllerAdvice
@CrossOrigin(maxAge = 3600)
public class ExceptionController {

    /**
     * Not found custom error. Http status 404.
     *
     * @param e the e
     * @return the custom error
     */
    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    CustomError notFound(CustomNotFoundException e) {
        return new CustomError(404, e.getMessage());
    }

    /**
     * External error custom error. Http status 500.
     *
     * @param e the e
     * @return the custom error
     */
    @ExceptionHandler({CustomExternalException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody CustomError externalError(CustomExternalException e) {
        return new CustomError(500, e.getMessage());
    }

    /**
     * External error. Http status 400. It is a global
     * exception.
     *
     * @param e the e
     * @return the custom error
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody CustomError badRequestError(Exception e) {
        return new CustomError(500, e.getMessage());
    }

    /**
     * Handle validation exceptions custom error.
     *
     * @param ex the ex
     * @return the custom error
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody CustomError handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new CustomError(422, errors.toString());
    }

    /**
     * Forbidden error.
     *
     * @param e the e
     * @return the custom error
     */
    @ExceptionHandler(CustomForbiddenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomError forbiddenError(CustomForbiddenException e) {
        return new CustomError(403, e.getMessage());
    }
}
