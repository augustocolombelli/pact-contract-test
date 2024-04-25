package com.book.service;


import com.book.exception.BookNotFoundException;
import com.book.model.Book;
import com.book.repository.BookRepository;
import com.book.service.dto.UpdateStockResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.book.service.dto.UpdateStockStatus.FAILURE;
import static com.book.service.dto.UpdateStockStatus.SUCCESS;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book getBookById(Long id) {
        return bookRepository
                .getBookById(id)
                .orElseThrow(BookNotFoundException::new);
    }

    public List<Book> getBooks() {
        return bookRepository.getBooks();
    }

    public UpdateStockResult updateStock(Long id, Long quantity) {
        Optional<Book> optionalBook = bookRepository.getBookById(id);
        if (optionalBook.isEmpty()) {
            String message = String.format("Failed to update stock of book %d because the book was not found.", id);
            log.warn(message);
            return UpdateStockResult.builder()
                    .status(FAILURE)
                    .message(message)
                    .build();
        }
        Book book = optionalBook.get();
        long newStock = book.getStock() + quantity;
        if (newStock < 0) {
            String message = String.format("Failed to update stock of book %d because the new stock will be less than 0.", id);
            log.warn(message);
            return UpdateStockResult.builder()
                    .status(FAILURE)
                    .message(message)
                    .build();
        }
        book.updateStock(newStock);
        String message = String.format("The stock of the book %d was update to %d.", id, newStock);
        log.info(message);
        return UpdateStockResult.builder()
                .status(SUCCESS)
                .message(message)
                .build();
    }
}
