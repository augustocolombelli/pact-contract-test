package com.person.exception;

public class PersonNotFoundException extends RuntimeException{

    public PersonNotFoundException(){
        super("Person was not found.");
    }
}
