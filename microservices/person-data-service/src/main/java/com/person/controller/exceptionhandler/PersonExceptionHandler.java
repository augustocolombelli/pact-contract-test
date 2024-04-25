package com.person.controller.exceptionhandler;

import com.person.exception.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class PersonExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(value = {PersonNotFoundException.class})
    public ErrorResponse personNotFoundException(PersonNotFoundException ex) {
        log.warn("A client error occurred on the API request. Status code={}, message={}", NOT_FOUND, ex.getMessage());
        return new ErrorResponse(NOT_FOUND.value(), ex.getMessage());
    }

}
