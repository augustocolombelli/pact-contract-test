package com.book.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("Book was not found");
    }
}
