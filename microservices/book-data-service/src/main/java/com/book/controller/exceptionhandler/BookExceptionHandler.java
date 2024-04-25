package com.book.controller.exceptionhandler;

import com.book.exception.BookNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class BookExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(value = {BookNotFoundException.class})
    public ErrorResponse handleBookNotFoundException(BookNotFoundException exception) {
        log.warn("A client error occurred on the API request. Status code={}, message={}", NOT_FOUND, exception.getMessage());
        return new ErrorResponse(NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldErrorResponse(err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
                .distinct()
                .collect(Collectors.toList());
        log.warn("A client error occurred on the API request. Status code={}, message={}", NOT_FOUND, exception.getMessage());
        return new ErrorResponse(BAD_REQUEST.value(), errorMessages);
    }

}
