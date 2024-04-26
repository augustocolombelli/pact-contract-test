package com.book;

import com.book.controller.BookController;
import com.book.repository.BookRepository;
import com.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookDataServiceApplicationTests {

    @Autowired
    private BookController bookController;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void contextLoads() {
        assertThat(bookController).isNotNull();
        assertThat(bookService).isNotNull();
        assertThat(bookRepository).isNotNull();
    }

}
