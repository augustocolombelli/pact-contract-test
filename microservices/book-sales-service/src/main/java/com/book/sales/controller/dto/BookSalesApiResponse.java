package com.book.sales.controller.dto;

import com.book.sales.service.dto.BookSalesStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookSalesApiResponse {
    BookSalesStatus status;
    String message;
}
