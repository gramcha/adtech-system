package com.gramcha.queryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.hateoas.VndErrors;

import java.util.Optional;

/**
 * Created by gramcha on 11/07/18.
 */
@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class QSControllerAdvice {
    private ResponseEntity<VndErrors> error(
            final Exception exception, final HttpStatus httpStatus, final String logRef) {
        final String message =
                Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
        return new ResponseEntity<>(new VndErrors(logRef, message), httpStatus);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<VndErrors> assertionException(final IllegalArgumentException e) {
        return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<VndErrors> assertionException(final Exception e) {
        return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
    }
}
