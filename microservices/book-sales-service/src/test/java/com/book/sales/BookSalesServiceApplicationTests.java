package com.book.sales;

import com.book.sales.client.bookdata.BookDataServiceWebClient;
import com.book.sales.client.persondata.PersonDataServiceWebClient;
import com.book.sales.controller.BookSalesController;
import com.book.sales.service.BookSalesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookSalesServiceApplicationTests {

    @Autowired
    private BookSalesService bookSalesService;

    @Autowired
    private BookSalesController bookSalesController;

    @Autowired
    private PersonDataServiceWebClient personDataServiceWebClient;

    @Autowired
    private BookDataServiceWebClient bookDataServiceWebClient;

    @Test
    void contextLoads() {
        assertThat(bookSalesService).isNotNull();
        assertThat(bookSalesController).isNotNull();
        assertThat(personDataServiceWebClient).isNotNull();
        assertThat(bookDataServiceWebClient).isNotNull();
    }

}
