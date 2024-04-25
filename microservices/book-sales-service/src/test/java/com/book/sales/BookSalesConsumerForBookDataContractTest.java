package com.book.sales;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.book.sales.client.bookdata.BookDataServiceWebClient;
import com.book.sales.client.bookdata.dto.BookDataResponse;
import com.book.sales.client.bookdata.dto.BookDataUpdateStockResponse;
import com.book.sales.client.bookdata.dto.UpdateStockStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

import static au.com.dius.pact.consumer.junit5.ProviderType.SYNCH;
import static com.book.sales.client.bookdata.dto.UpdateStockStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(PactConsumerTestExt.class)
@TestInstance(PER_CLASS)
public class BookSalesConsumerForBookDataContractTest {

    private static final long A_BOOK_ID = 1001L;
    private static final long A_BOOK_STOCK = 15L;
    private static final long A_BOOK_QUANTITY = 1L;
    public static final String PATH_GET_BOOK_BY_ID = "/books/{bookId}";
    public static final String PATH_UPDATE_STOCK = "/books/updateStock";
    private final String CONSUMER_BOOK_SALES_SERVICE_GET_BOOKS = "book-sales-service-get-books";
    private final String CONSUMER_BOOK_SALES_SERVICE_UPDATE_STOCK = "book-sales-service-update-stock";
    private final String PROVIDER_BOOK_DATA_SERVICE = "book-data-service";

    @Pact(consumer = CONSUMER_BOOK_SALES_SERVICE_GET_BOOKS, provider = PROVIDER_BOOK_DATA_SERVICE)
    public V4Pact whenRequestBookById_thenReturnsBook(PactDslWithProvider builder) {
        PactDslJsonBody bodyResponse = new PactDslJsonBody()
                .integerType("id", A_BOOK_ID)
                .integerType("stock", A_BOOK_STOCK);

        return builder
                .given("it has a book and status code is 200")
                .uponReceiving("a request to retrieve a book by id")
                .path("/books/" + A_BOOK_ID)
                .method("GET")
                .willRespondWith()
                .headers(Collections.singletonMap("Content-Type", "application/json"))
                .status(OK.value())
                .body(bodyResponse)
                .toPact(V4Pact.class);
    }

    @Pact(consumer = CONSUMER_BOOK_SALES_SERVICE_UPDATE_STOCK, provider = PROVIDER_BOOK_DATA_SERVICE)
    public V4Pact whenUpdateBookStock_thenReturnsStatus(PactDslWithProvider builder) {
        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringType("status", "SUCCESS")
                .stringType("message", "The stock of the book " + A_BOOK_ID + " was update to 16");

        PactDslJsonBody requestBody = new PactDslJsonBody()
                .integerType("id", A_BOOK_ID)
                .integerType("quantity", A_BOOK_QUANTITY)
                .asBody();

        return builder
                .given("it has a book and stock can be updated")
                .uponReceiving("a request to update the stock of book")
                .method(HttpMethod.POST.name())
                .path(PATH_UPDATE_STOCK)
                .headers(Collections.singletonMap("Content-Type", "application/json"))
                .body(requestBody)
                .willRespondWith()
                .headers(Map.of("Content-type", "application/json"))
                .status(OK.value())
                .body(responseBody)
                .toPact(V4Pact.class);
    }

    @PactTestFor(providerName = PROVIDER_BOOK_DATA_SERVICE, pactMethod = "whenRequestBookById_thenReturnsBook", providerType = SYNCH)
    @Test
    public void whenRequestBookById_thenReturnsBook(MockServer mockServer) {
        // given
        WebClient webClient = WebClient.builder()
                .baseUrl(mockServer.getUrl())
                .build();

        // when
        BookDataServiceWebClient bookDataServiceWebClient =
                new BookDataServiceWebClient(webClient, PATH_GET_BOOK_BY_ID, PATH_UPDATE_STOCK);
        BookDataResponse bookDataResponse = bookDataServiceWebClient.retrieveBook(A_BOOK_ID);

        // then
        assertThat(bookDataResponse.getId()).isInstanceOf(Long.class).isEqualTo(A_BOOK_ID);
        assertThat(bookDataResponse.getStock()).isInstanceOf(Long.class).isEqualTo(A_BOOK_STOCK);
    }

    @PactTestFor(providerName = PROVIDER_BOOK_DATA_SERVICE, pactMethod = "whenUpdateBookStock_thenReturnsStatus", providerType = SYNCH)
    @Test
    public void whenUpdateBookStock_thenReturnsStatus(MockServer mockServer) {
        // given
        WebClient webClient = WebClient.builder()
                .baseUrl(mockServer.getUrl())
                .build();

        // when
        BookDataServiceWebClient bookDataServiceWebClient =
                new BookDataServiceWebClient(webClient, PATH_GET_BOOK_BY_ID, PATH_UPDATE_STOCK);
        BookDataUpdateStockResponse bookDataUpdateStockResponse = bookDataServiceWebClient.updateStock(A_BOOK_ID, A_BOOK_QUANTITY);

        // then
        assertThat(bookDataUpdateStockResponse.getStatus()).isInstanceOf(UpdateStockStatus.class).isEqualTo(SUCCESS);
        assertThat(bookDataUpdateStockResponse.getMessage()).isInstanceOf(String.class)
                .isEqualTo("The stock of the book " + A_BOOK_ID + " was update to 16");
    }

}
