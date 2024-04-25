package com.book.controller;

import com.book.controller.dto.UpdateStockApiRequest;
import com.book.controller.dto.UpdateStockApiResponse;
import com.book.model.Book;
import com.book.service.BookService;
import com.book.service.dto.UpdateStockResult;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.book.service.dto.UpdateStockStatus.FAILURE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Book> getBooks() {
        log.info("Get books API requested.");
        return bookService.getBooks();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Book getBookById(@PathVariable Long id) {
        log.info("Get book by id {} API requested.", id);
        return bookService.getBookById(id);
    }

    @PostMapping(value = "/updateStock", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
    public ResponseEntity<UpdateStockApiResponse> updateStock(@Valid @RequestBody UpdateStockApiRequest requestBody) {
        log.info("Update stock API requested with request body {}.", requestBody);
        UpdateStockResult updateStockResult = bookService.updateStock(requestBody.getId(), requestBody.getQuantityToUpdate());
        if (FAILURE.equals(updateStockResult.getStatus())) {
            return new ResponseEntity<>(buildUpdateStockApiResponse(updateStockResult), UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(buildUpdateStockApiResponse(updateStockResult), OK);
    }

    private UpdateStockApiResponse buildUpdateStockApiResponse(UpdateStockResult updateStockResult) {
        return UpdateStockApiResponse.builder()
                .message(updateStockResult.getMessage())
                .status(updateStockResult.getStatus())
                .build();
    }

}
