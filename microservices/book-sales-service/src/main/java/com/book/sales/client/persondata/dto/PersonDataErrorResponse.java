package com.book.sales.client.persondata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PersonDataErrorResponse {
    Integer statusCode;
    String errorMessage;
}
