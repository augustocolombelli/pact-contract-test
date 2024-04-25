package com.book.sales.service;

import com.book.sales.client.bookdata.BookDataServiceWebClient;
import com.book.sales.client.persondata.PersonDataServiceWebClient;
import com.book.sales.service.dto.BookSalesResult;
import com.book.sales.service.dto.BookSalesStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.book.sales.client.bookdata.dto.UpdateStockStatus.FAILURE;
import static com.book.sales.client.bookdata.dto.UpdateStockStatus.SUCCESS;
import static com.book.sales.fake.BookDataResponseTestDataBuilder.aBookDataResponse;
import static com.book.sales.fake.BookDataUpdateStockResponseTestDataBuilder.aBookDataUpdateStockResponse;
import static com.book.sales.fake.PersonDataResponseTestDataBuilder.aPersonDataResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookSalesServiceTest {

    private static final Long A_PERSON_ID = 12314L;
    private static final Long A_BOOK_ID = 48585L;


    @InjectMocks
    private BookSalesService bookSalesService;

    @Mock
    private BookDataServiceWebClient bookDataWebClient;

    @Mock
    private PersonDataServiceWebClient personDataWebClient;

    @Nested
    class ProcessBookSales {

        @Test
        void whenProvidedPersonAndBookId_thenCallPersonDataWebClient() {
            // given
            when(bookDataWebClient.retrieveBook(A_BOOK_ID)).thenReturn(aBookDataResponse(A_BOOK_ID));

            // when
            bookSalesService.processBookSales(A_PERSON_ID, A_BOOK_ID);

            // then
            verify(personDataWebClient).retrievePerson(A_PERSON_ID);
        }

        @Test
        void whenProvidedPersonAndBookId_thenCallBookDataWebClient() {
            // given
            when(bookDataWebClient.retrieveBook(A_BOOK_ID)).thenReturn(aBookDataResponse(A_BOOK_ID));

            // when
            bookSalesService.processBookSales(A_PERSON_ID, A_BOOK_ID);

            // then
            verify(bookDataWebClient).retrieveBook(A_BOOK_ID);
        }

        @Test
        void whenBookDoesNotHaveStock_thenReturnOutOfStockStatus() {
            // given
            when(bookDataWebClient.retrieveBook(A_BOOK_ID)).thenReturn(aBookDataResponse(A_BOOK_ID));

            // when
            BookSalesResult actualResult = bookSalesService.processBookSales(A_PERSON_ID, A_BOOK_ID);

            // then
            assertThat(actualResult.getStatus()).isEqualTo(BookSalesStatus.OUT_OF_STOCK);
        }

        @Test
        void whenFailureTheStockUpdate_thenReturnUpdateStockFailure() {
            // given
            when(bookDataWebClient.retrieveBook(A_BOOK_ID)).thenReturn(aBookDataResponse(A_BOOK_ID, 5L));
            when(bookDataWebClient.updateStock(eq(A_BOOK_ID), any())).thenReturn(aBookDataUpdateStockResponse(FAILURE));

            // when
            BookSalesResult actualResult = bookSalesService.processBookSales(A_PERSON_ID, A_BOOK_ID);

            // then
            assertThat(actualResult.getStatus()).isEqualTo(BookSalesStatus.UPDATE_STOCK_FAILURE);
        }

        @Test
        void whenProvidedPersonAndBookId_thenReturnsSuccess() {
            // given
            when(bookDataWebClient.retrieveBook(A_BOOK_ID)).thenReturn(aBookDataResponse(A_BOOK_ID, 5L));
            when(bookDataWebClient.updateStock(eq(A_BOOK_ID), any())).thenReturn(aBookDataUpdateStockResponse(SUCCESS));
            when(personDataWebClient.retrievePerson(eq(A_PERSON_ID))).thenReturn(aPersonDataResponse(A_PERSON_ID));

            // when
            BookSalesResult actualResult = bookSalesService.processBookSales(A_PERSON_ID, A_BOOK_ID);

            // then
            assertThat(actualResult.getStatus()).isEqualTo(BookSalesStatus.SUCCESS);
        }
    }

}
