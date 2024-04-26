package com.book.fake;

import com.book.model.Book;

public class BookTestDataBuilder {

    public static Book aBook(Long bookId) {
        return Book.builder()
                .id(bookId)
                .name("a name")
                .ISBN("a isbn")
                .stock(5L)
                .build();
    }
}
