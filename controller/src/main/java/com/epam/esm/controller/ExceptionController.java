package com.epam.esm.controller;

import com.epam.esm.exception.CustomError;
import com.epam.esm.exception.CustomExternalException;
import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.exception.CustomNotValidArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The type Exception controller.
 */
@RestControllerAdvice
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
     * External error custom error. Http status 422.
     *
     * @param e the e
     * @return the custom error
     */
    @ExceptionHandler(CustomNotValidArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public @ResponseBody CustomError unprocessableError(CustomNotValidArgumentException e) {
        return new CustomError(422, e.getMessage());
    }

    /**
     * External error custom error. Http status 500.
     *
     * @param e the e
     * @return the custom error
     */
    @ExceptionHandler(CustomExternalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody CustomError externalError(CustomExternalException e) {
        return new CustomError(500, e.getMessage());
    }

    /**
     * External error custom error. Http status 400. It is a global
     * exception.
     *
     * @param e the e
     * @return the custom error
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody CustomError badRequestError(Exception e) {
        return new CustomError(400, e.getMessage());
    }
}
