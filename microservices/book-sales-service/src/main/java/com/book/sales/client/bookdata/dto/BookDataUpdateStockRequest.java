package com.book.sales.client.bookdata.dto;

import lombok.*;

@Getter
@Builder
public class BookDataUpdateStockRequest {
    Long id;
    Long quantity;
}
