package com.book.sales.service;

import com.book.sales.client.bookdata.BookDataServiceWebClient;
import com.book.sales.client.bookdata.dto.BookDataResponse;
import com.book.sales.client.bookdata.dto.BookDataUpdateStockResponse;
import com.book.sales.client.bookdata.dto.UpdateStockStatus;
import com.book.sales.client.persondata.PersonDataServiceWebClient;
import com.book.sales.client.persondata.dto.PersonDataResponse;
import com.book.sales.service.dto.BookSalesResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.book.sales.client.bookdata.dto.UpdateStockStatus.FAILURE;
import static com.book.sales.service.dto.BookSalesStatus.*;

@Service
@Slf4j
public class BookSalesService {

    @Autowired
    private BookDataServiceWebClient bookDataWebClient;

    @Autowired
    private PersonDataServiceWebClient personDataWebClient;

    public BookSalesResult processBookSales(Long personId, Long bookId) {
        log.info("Making request to book data service to get data of book {}", bookId);
        BookDataResponse bookDataResponse = bookDataWebClient.retrieveBook(bookId);
        log.info("Making request to person data service to get data of person {}", bookId);
        PersonDataResponse personDataResponse = personDataWebClient.retrievePerson(personId);
        if (bookDataResponse.getStock() <= 0) {
            String message = buildOutOfStockMessage(bookDataResponse);
            log.error(message);
            return BookSalesResult.builder()
                    .message(message)
                    .status(OUT_OF_STOCK)
                    .build();
        }
        BookDataUpdateStockResponse bookDataUpdateStockResponse = bookDataWebClient.updateStock(bookId, -1L);
        if (FAILURE.equals(bookDataUpdateStockResponse.getStatus())) {
            log.error("Failure to update the stock of book {}. Response of book data api: {}:", bookId, bookDataUpdateStockResponse.getMessage());
            return BookSalesResult.builder()
                    .message(bookDataUpdateStockResponse.getMessage())
                    .status(UPDATE_STOCK_FAILURE)
                    .build();
        }
        String message = buildSuccessfulMessage(personDataResponse);
        log.info(message);
        return BookSalesResult.builder()
                .message(message)
                .status(SUCCESS)
                .build();
    }

    private String buildSuccessfulMessage(PersonDataResponse personDataResponse) {
        return String.format(
                "The sale to person %s with passport number %s was successful.",
                personDataResponse.getName(),
                personDataResponse.getPassportNumber()
        );
    }

    private String buildOutOfStockMessage(BookDataResponse bookDataResponse) {
        return String.format(
                "The current stock of book %d is %d and is not sufficient to make the sale.",
                bookDataResponse.getId(),
                bookDataResponse.getStock()
        );
    }

}
