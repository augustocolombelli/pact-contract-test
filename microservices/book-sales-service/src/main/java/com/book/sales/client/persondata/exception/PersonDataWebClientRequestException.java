package com.book.sales.client.persondata.exception;

import org.springframework.web.reactive.function.client.WebClientRequestException;

public class PersonDataWebClientRequestException extends RuntimeException {
    public PersonDataWebClientRequestException(WebClientRequestException exception) {
        super("An error occurred when making request to the person data downstream service.", exception);
    }
}