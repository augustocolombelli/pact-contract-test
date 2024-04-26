package com.book.sales.client.bookdata;

import com.book.sales.client.bookdata.dto.BookDataResponse;
import com.book.sales.client.bookdata.dto.BookDataUpdateStockResponse;
import com.book.sales.client.bookdata.dto.UpdateStockStatus;
import com.book.sales.client.bookdata.exception.BookDataClientException;
import com.book.sales.client.bookdata.exception.BookDataServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.book.sales.client.bookdata.dto.UpdateStockStatus.SUCCESS;
import static com.book.sales.fake.BookDataResponseTestDataBuilder.aBookDataResponse;
import static com.book.sales.fake.BookDataUpdateStockResponseTestDataBuilder.aBookDataUpdateStockResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class BookDataServiceWebClientTest {
    private static final long A_BOOK_ID = 1254L;
    private static final String PATH_GET_BOOK_BY_ID = "/books/{bookId}";
    private static final String PATH_UPDATE_STOCK = "/books/updateStock";

    private BookDataServiceWebClient bookDataServiceWebClient;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void beforeEach() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        WebClient webClient = WebClient.create(baseUrl);
        bookDataServiceWebClient = new BookDataServiceWebClient(webClient, PATH_GET_BOOK_BY_ID, PATH_UPDATE_STOCK);
    }

    @Nested
    class RetrieveBook {

        @Test
        void whenStatusCodeIs200_thenReturnsBook() throws JsonProcessingException {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(aBookDataResponse(A_BOOK_ID)))
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            BookDataResponse response = bookDataServiceWebClient.retrieveBook(A_BOOK_ID);

            // then
            assertThat(response.getId()).isEqualTo(A_BOOK_ID);
        }

        @Test
        void whenStatusCodeIs400_thenThrowsClientException() {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody("error")
                    .setResponseCode(400)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            Throwable thrown = catchThrowable(() -> bookDataServiceWebClient.retrieveBook(A_BOOK_ID));

            // then
            assertThat(thrown)
                    .isInstanceOf(BookDataClientException.class)
                    .hasMessageContaining("A client error occurred when making request to book data service.");
        }

        @Test
        void whenStatusCodeIs500_thenThrowsServerException() {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody("error")
                    .setResponseCode(500)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            Throwable thrown = catchThrowable(() -> bookDataServiceWebClient.retrieveBook(A_BOOK_ID));

            // then
            assertThat(thrown)
                    .isInstanceOf(BookDataServerException.class)
                    .hasMessageContaining("A server error occurred when making request to book data service.");
        }

    }

    @Nested
    class UpdateStock {

        @Test
        void whenStatusCodeIs200_thenReturnsResponse() throws JsonProcessingException {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(aBookDataUpdateStockResponse(SUCCESS)))
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            BookDataUpdateStockResponse response = bookDataServiceWebClient.updateStock(A_BOOK_ID, 1L);

            // then
            assertThat(response.getStatus()).isEqualTo(SUCCESS);
        }

        @Test
        void whenStatusCodeIs400_thenThrowsClientException() {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody("error")
                    .setResponseCode(400)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            Throwable thrown = catchThrowable(() -> bookDataServiceWebClient.updateStock(A_BOOK_ID, 1L));

            // then
            assertThat(thrown)
                    .isInstanceOf(BookDataClientException.class)
                    .hasMessageContaining("A client error occurred when making request to book data service.");
        }

        @Test
        void whenStatusCodeIs500_thenThrowsServerException() {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody("error")
                    .setResponseCode(500)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            Throwable thrown = catchThrowable(() -> bookDataServiceWebClient.updateStock(A_BOOK_ID, 1L));

            // then
            assertThat(thrown)
                    .isInstanceOf(BookDataServerException.class)
                    .hasMessageContaining("A server error occurred when making request to book data service.");
        }

    }

}
