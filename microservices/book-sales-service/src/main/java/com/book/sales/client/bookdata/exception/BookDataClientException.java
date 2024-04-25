package com.book.sales.client.bookdata.exception;

public class BookDataClientException extends RuntimeException{

    public BookDataClientException(){
        super("A client error occurred when making request to book data service.");
    }
}
