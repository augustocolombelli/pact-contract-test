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
        void whenGetBookById_thenCallsRepository() {
            // given
            when(bookRepository.getBookById(A_BOOK_ID)).thenReturn(Optional.of(aBook(A_BOOK_ID)));

            // when
            bookService.getBookById(A_BOOK_ID);

            // then
            verify(bookRepository).getBookById(A_BOOK_ID);
        }

        @Test
        void whenGetBookById_thenReturnsBook() {
            // given
            when(bookRepository.getBookById(A_BOOK_ID)).thenReturn(Optional.of(aBook(A_BOOK_ID)));

            // when
            Book actualBook = bookService.getBookById(A_BOOK_ID);

            // then
            assertThat(actualBook.getId()).isEqualTo(A_BOOK_ID);
        }

    }

    @Nested
    class GetBooks {

        @Test
        void whenGetBooks_thenCallRepository() {
            bookService.getBooks();

            verify(bookRepository).getBooks();
        }

        @Test
        void whenGetBooks_thenReturnBooks() {
            // given
            when(bookRepository.getBooks()).thenReturn(List.of(aBook(A_BOOK_ID)));

            // when
            List<Book> actualBooks = bookService.getBooks();

            // then
            assertThat(actualBooks).extracting(Book::getId).contains(A_BOOK_ID);
        }
        
    }

}
