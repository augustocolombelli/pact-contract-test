package com.book.sales.fake;

import com.book.sales.client.bookdata.dto.BookDataUpdateStockResponse;
import com.book.sales.client.bookdata.dto.UpdateStockStatus;
import com.book.sales.service.dto.BookSalesResult;
import com.book.sales.service.dto.BookSalesStatus;

import static com.book.sales.service.dto.BookSalesStatus.OUT_OF_STOCK;
import static com.book.sales.service.dto.BookSalesStatus.SUCCESS;
import static com.book.sales.service.dto.BookSalesStatus.UPDATE_STOCK_FAILURE;

public class BookSalesResultTestDataBuilder {

    public static BookSalesResult aSuccessBookSalesResult() {
        return BookSalesResult.builder()
                .status(SUCCESS)
                .message("any")
                .build();
    }

    public static BookSalesResult aOutOfStockBookSalesResult() {
        return BookSalesResult.builder()
                .status(OUT_OF_STOCK)
                .message("any")
                .build();
    }

    public static BookSalesResult aUpdateStockFailureBookSalesResult() {
        return BookSalesResult.builder()
                .status(UPDATE_STOCK_FAILURE)
                .message("any")
                .build();
    }
}
