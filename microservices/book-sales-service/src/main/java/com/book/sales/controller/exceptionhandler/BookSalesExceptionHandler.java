package com.book.sales.controller.exceptionhandler;

import com.book.sales.client.bookdata.exception.BookDataClientException;
import com.book.sales.client.bookdata.exception.BookDataServerException;
import com.book.sales.client.bookdata.exception.BookDataWebClientRequestException;
import com.book.sales.client.persondata.exception.PersonDataClientException;
import com.book.sales.client.persondata.exception.PersonDataServerException;
import com.book.sales.client.persondata.exception.PersonDataWebClientRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class BookSalesExceptionHandler {

    @ResponseStatus(BAD_GATEWAY)
    @ExceptionHandler(value = {PersonDataServerException.class})
    public ErrorResponse personDataServerException(PersonDataServerException ex) {
        return new ErrorResponse(BAD_GATEWAY.value(), ex.getMessage());
    }

    @ResponseStatus(BAD_GATEWAY)
    @ExceptionHandler(value = {BookDataServerException.class})
    public ErrorResponse bookDataServerException(BookDataServerException ex) {
        return new ErrorResponse(BAD_GATEWAY.value(), ex.getMessage());
    }

    @ResponseStatus(BAD_GATEWAY)
    @ExceptionHandler(value = {BookDataWebClientRequestException.class})
    public ErrorResponse bookDataWebClientRequestException(BookDataWebClientRequestException ex) {
        log.error("An error occurred when making request to book data service.", ex);
        return new ErrorResponse(BAD_GATEWAY.value(), ex.getMessage());
    }

    @ResponseStatus(BAD_GATEWAY)
    @ExceptionHandler(value = {PersonDataWebClientRequestException.class})
    public ErrorResponse personDataWebClientRequestException(PersonDataWebClientRequestException ex) {
        log.error("An error occurred when making request to person data service.", ex);
        return new ErrorResponse(BAD_GATEWAY.value(), ex.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = {PersonDataClientException.class})
    public ErrorResponse personDataClientException(PersonDataClientException ex) {
        return new ErrorResponse(FORBIDDEN.value(), ex.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = {BookDataClientException.class})
    public ErrorResponse bookDataClientException(BookDataClientException ex) {
        return new ErrorResponse(FORBIDDEN.value(), ex.getMessage());
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
