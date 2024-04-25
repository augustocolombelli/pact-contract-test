package com.book.sales.controller;

import com.book.sales.controller.dto.BookSalesApiRequest;
import com.book.sales.controller.dto.BookSalesApiResponse;
import com.book.sales.service.dto.BookSalesResult;
import com.book.sales.service.BookSalesService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.book.sales.service.dto.BookSalesStatus.OUT_OF_STOCK;
import static com.book.sales.service.dto.BookSalesStatus.UPDATE_STOCK_FAILURE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
@RestController
@RequestMapping("/book-sales")
public class BookSalesController {

    @Autowired
    private BookSalesService bookSalesService;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookSalesApiResponse> saleBook(@Valid @RequestBody BookSalesApiRequest requestBody) {
        log.info("Sales book API requested with request body {}.", requestBody);
        BookSalesResult bookSalesResult =
                bookSalesService.processBookSales(requestBody.getPersonId(), requestBody.getBookId());

        if(OUT_OF_STOCK.equals(bookSalesResult.getStatus()) ||
                UPDATE_STOCK_FAILURE.equals(bookSalesResult.getStatus()) ){
            return new ResponseEntity<>(buildBookSalesApiResponse(bookSalesResult), UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(buildBookSalesApiResponse(bookSalesResult), OK);
    }

    private BookSalesApiResponse buildBookSalesApiResponse(BookSalesResult bookSalesResult) {
        return BookSalesApiResponse.builder()
                .message(bookSalesResult.getMessage())
                .status(bookSalesResult.getStatus())
                .build();
    }
}
