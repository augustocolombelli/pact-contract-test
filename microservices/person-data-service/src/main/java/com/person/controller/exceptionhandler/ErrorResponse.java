package com.person.controller.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorResponse {
    Integer statusCode;
    String errorMessage;
}