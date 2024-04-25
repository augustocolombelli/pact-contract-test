package com.book.sales.client.bookdata.exception;

import org.springframework.web.reactive.function.client.WebClientRequestException;

public class BookDataWebClientRequestException extends RuntimeException {
    public BookDataWebClientRequestException(WebClientRequestException exception) {
        super("An error occurred when making request to the book data downstream service.", exception);
    }
}