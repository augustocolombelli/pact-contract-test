package com.book.sales.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookSalesResult {
    BookSalesStatus status;
    String message;
}
