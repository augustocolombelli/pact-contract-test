package com.book.sales.fake;

import com.book.sales.client.bookdata.dto.BookDataResponse;

public class BookDataResponseTestDataBuilder {

    public static BookDataResponse aBookDataResponse(Long bookId) {
        return aBookDataResponse(bookId, 0L);
    }

    public static BookDataResponse aBookDataResponse(Long bookId, long stock) {
        return new BookDataResponse(bookId, "a book", "a isbn", stock);
    }
}
