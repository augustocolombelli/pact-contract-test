package com.book.sales.fake;

import com.book.sales.client.bookdata.dto.BookDataUpdateStockResponse;
import com.book.sales.client.bookdata.dto.UpdateStockStatus;

public class BookDataUpdateStockResponseTestDataBuilder {

    public static BookDataUpdateStockResponse aBookDataUpdateStockResponse(UpdateStockStatus status){
        return new BookDataUpdateStockResponse(status, "a message");
    }
}
