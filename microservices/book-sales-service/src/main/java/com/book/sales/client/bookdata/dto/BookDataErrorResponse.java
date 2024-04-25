package com.book.sales.client.bookdata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookDataErrorResponse {
    Integer statusCode;
    String errorMessage;
}
