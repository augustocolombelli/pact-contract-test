package com.book.sales.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookSalesApiRequest {

    @NotNull(message = "Person Id has to be present")
    Long personId;

    @NotNull(message = "Book Id has to be present")
    Long bookId;
}
