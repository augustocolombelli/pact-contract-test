package com.book.sales.client.persondata.exception;

public class PersonDataClientException extends RuntimeException{

    public PersonDataClientException(){
        super("A client error occurred when making request to person data service.");
    }

}
