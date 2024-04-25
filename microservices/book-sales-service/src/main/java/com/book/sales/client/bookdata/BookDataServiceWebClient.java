package com.book.sales.client.bookdata;

import com.book.sales.client.bookdata.dto.BookDataUpdateStockRequest;
import com.book.sales.client.bookdata.dto.BookDataUpdateStockResponse;
import com.book.sales.client.bookdata.exception.BookDataClientException;
import com.book.sales.client.bookdata.exception.BookDataServerException;
import com.book.sales.client.bookdata.dto.BookDataErrorResponse;
import com.book.sales.client.bookdata.dto.BookDataResponse;
import com.book.sales.client.bookdata.exception.BookDataWebClientRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Service
public class BookDataServiceWebClient {

    public final String bookDataGetByIdPath;

    public final String bookDataUpdateStockPath;

    private final WebClient bookDataWebClient;

    @Autowired
    public BookDataServiceWebClient(
            WebClient bookDataWebClient,
            @Value("${service-client.book-data-service.get-book-by-id}") String bookDataGetByIdPath,
            @Value("${service-client.book-data-service.update-stock}") String bookDataUpdateStockPath){
        this.bookDataWebClient = bookDataWebClient;
        this.bookDataGetByIdPath = bookDataGetByIdPath;
        this.bookDataUpdateStockPath = bookDataUpdateStockPath;
    }

    public BookDataResponse retrieveBook(Long id) {
        log.info("Retrieve book {} from book data service in path {}.", id, bookDataGetByIdPath);
        Mono<BookDataResponse> responseMono = bookDataWebClient
                .get()
                .uri(bookDataGetByIdPath, id)
                .retrieve()
                .onStatus(UNPROCESSABLE_ENTITY::equals, BookDataServiceWebClient::onSpecificServerError)
                .onStatus(HttpStatusCode::is5xxServerError, BookDataServiceWebClient::onServerError)
                .onStatus(HttpStatusCode::is4xxClientError, BookDataServiceWebClient::onClientError)
                .bodyToMono(BookDataResponse.class)
                .onErrorMap(WebClientRequestException.class, exception -> new BookDataWebClientRequestException(exception));
        return Objects.requireNonNull(responseMono.block());
    }

    public BookDataUpdateStockResponse updateStock(Long id, Long quantity) {
        log.info("Request to update the stock of book {} to book data service in path {}.", id, bookDataGetByIdPath);
        BookDataUpdateStockRequest requestBody = BookDataUpdateStockRequest.builder()
                .id(id)
                .quantity(quantity)
                .build();

        Mono<BookDataUpdateStockResponse> responseMono = bookDataWebClient
                .post()
                .uri(bookDataUpdateStockPath)
                .body(Mono.just(requestBody), BookDataUpdateStockRequest.class)
                .retrieve()
                .onStatus(UNPROCESSABLE_ENTITY::equals, BookDataServiceWebClient::onSpecificServerError)
//                .onStatus(HttpStatusCode::is5xxServerError, BookDataServiceWebClient::onServerError)
                .onStatus(HttpStatusCode::is4xxClientError, BookDataServiceWebClient::onClientError)
                .bodyToMono(BookDataUpdateStockResponse.class)
                .onErrorMap(WebClientRequestException.class, exception -> new BookDataWebClientRequestException(exception));
        return Objects.requireNonNull(responseMono.block());
    }

    private static Mono<Throwable> onSpecificServerError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(BookDataErrorResponse.class)
                .flatMap(responseBody -> {
                    log.error("A specific server error occurred on book data service request. Status {}. Response {}", clientResponse.statusCode(), responseBody);
                    return Mono.error(new BookDataServerException(responseBody.getErrorMessage()));
                });
    }

    private static Mono<RuntimeException> onClientError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).map(responseBody -> {
            log.error("A client error occurred on book data service request. Status {}. Response {}", clientResponse.statusCode(), responseBody);
            throw new BookDataClientException();
        });
    }

    private static Mono<RuntimeException> onServerError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).map(responseBody -> {
            log.error("A server error occurred on book data service request. Status {}. Response {}", clientResponse.statusCode(), responseBody);
            throw new BookDataServerException("A server error occurred when making request to book data service.");
        });
    }
}
