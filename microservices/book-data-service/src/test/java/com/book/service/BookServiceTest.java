package com.book.service;

import com.book.model.Book;
import com.book.repository.BookRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.book.fake.BookTestDataBuilder.aBook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private static final Long A_BOOK_ID = 12314L;

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Nested
    class GetBookById {

        @Test
        void getBookById_callGetBookById() {
            when(bookRepository.getBookById(A_BOOK_ID)).thenReturn(Optional.of(aBook(A_BOOK_ID)));

            bookService.getBookById(A_BOOK_ID);

            verify(bookRepository).getBookById(A_BOOK_ID);
        }

        @Test
        void getBookById_returnBook() {
            when(bookRepository.getBookById(A_BOOK_ID)).thenReturn(Optional.of(aBook(A_BOOK_ID)));

            Book actualBook = bookService.getBookById(A_BOOK_ID);

            assertThat(actualBook.getId()).isEqualTo(A_BOOK_ID);
        }
    }

    @Nested
    class GetBooks {
        @Test
        void getBooks_callGetBooks() {
            bookService.getBooks();

            verify(bookRepository).getBooks();
        }

        @Test
        void getBooks_returnBook() {
            when(bookRepository.getBooks()).thenReturn(List.of(aBook(A_BOOK_ID)));

            List<Book> actualBooks = bookService.getBooks();

            assertThat(actualBooks).extracting(Book::getId).contains(A_BOOK_ID);
        }
    }

}
