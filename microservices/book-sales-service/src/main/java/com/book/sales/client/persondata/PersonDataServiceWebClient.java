package com.book.sales.client.persondata;

import com.book.sales.client.bookdata.exception.BookDataWebClientRequestException;
import com.book.sales.client.persondata.exception.PersonDataClientException;
import com.book.sales.client.persondata.exception.PersonDataServerException;
import com.book.sales.client.persondata.dto.PersonDataErrorResponse;
import com.book.sales.client.persondata.dto.PersonDataResponse;
import com.book.sales.client.persondata.exception.PersonDataWebClientRequestException;
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
public class PersonDataServiceWebClient {


    private final String personDataGetByIdPath;

    private final WebClient personDataWebClient;

    @Autowired
    public PersonDataServiceWebClient(WebClient personDataWebClient, @Value("${service-client.person-data-service.get-person-by-id}") String personDataGetByIdPath){
        this.personDataWebClient = personDataWebClient;
        this.personDataGetByIdPath = personDataGetByIdPath;
    }

    public PersonDataResponse retrievePerson(Long ID) {
        log.info("Retrieve person {} from person data service in path {}.", ID, personDataGetByIdPath);
        Mono<PersonDataResponse> responseMono = personDataWebClient
                .get()
                .uri(personDataGetByIdPath, ID)
                .retrieve()
                .onStatus(UNPROCESSABLE_ENTITY::equals, PersonDataServiceWebClient::onSpecificServerError)
                .onStatus(HttpStatusCode::is5xxServerError, PersonDataServiceWebClient::onServerError)
                .onStatus(HttpStatusCode::is4xxClientError, PersonDataServiceWebClient::onClientError)
                .bodyToMono(PersonDataResponse.class)
                .onErrorMap(WebClientRequestException.class, exception -> new PersonDataWebClientRequestException(exception));

        return Objects.requireNonNull(responseMono.block());
    }

    private static Mono<Throwable> onSpecificServerError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(PersonDataErrorResponse.class)
                .flatMap(responseBody -> {
                    log.error("A specific server error occurred on person data service request. Status {}. Response {}", clientResponse.statusCode(), responseBody);
                    return Mono.error(new PersonDataServerException(responseBody.getErrorMessage()));
                });
    }

    private static Mono<RuntimeException> onClientError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).map(responseBody -> {
            log.error("A client error occurred on person data service request. Status {}. Response {}", clientResponse.statusCode(), responseBody);
            throw new PersonDataClientException();
        });
    }

    private static Mono<RuntimeException> onServerError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).map(responseBody -> {
            log.error("A server error occurred person data service request. Status {}. Response {}", clientResponse.statusCode(), responseBody);
            throw new PersonDataServerException("A server error occurred when making request to person data service.");
        });
    }
}
