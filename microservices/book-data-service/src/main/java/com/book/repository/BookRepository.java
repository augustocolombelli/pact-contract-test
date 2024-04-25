package com.book.repository;

import com.book.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private static final List<Book> books = buildFakeBooks();

    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

    }

    public List<Book> getBooks() {
        return books;
    }

    private static List<Book> buildFakeBooks() {
        Book aBook = Book.builder()
                .id(201L)
                .name("Domain-Driven Design: Tackling Complexity in the Heart of Software")
                .ISBN("9780321125217")
                .currentStock(0L)
                .build();

        Book anotherBook = Book.builder()
                .id(202L)
                .name("Refactoring: Improving the Design of Existing Code")
                .ISBN("0201485672")
                .currentStock(1L)
                .build();

        Book aThirdBook = Book.builder()
                .id(203L)
                .name("Design Patterns: Elements of Reusable Object-Oriented Software")
                .ISBN("0201633612")
                .currentStock(4L)
                .build();

        return List.of(aBook, anotherBook, aThirdBook);
    }
}
