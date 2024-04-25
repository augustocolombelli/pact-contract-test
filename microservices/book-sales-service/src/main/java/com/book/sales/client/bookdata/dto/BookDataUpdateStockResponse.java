package com.book.sales.client.bookdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDataUpdateStockResponse {
    UpdateStockStatus status;
    String message;
}
