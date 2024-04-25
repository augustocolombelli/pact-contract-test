package com.book.sales.client.bookdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDataResponse {
    Long id;
    String name;
    String ISBN;
    Long stock;
}
