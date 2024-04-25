package com.book.sales;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.book.sales.client.persondata.PersonDataServiceWebClient;
import com.book.sales.client.persondata.dto.PersonDataResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

import static au.com.dius.pact.consumer.junit5.ProviderType.SYNCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpStatus.OK;


@ExtendWith(PactConsumerTestExt.class)
@TestInstance(PER_CLASS)
public class BookSalesConsumerForPersonDataContractTest {

    private static final long A_PERSON_ID = 1001L;
    private static final String A_PERSON_NAME = "John";
    private static final String A_PERSON_PASSPORT_NUMBER = "FT858652";
    private final String CONSUMER_BOOK_SALES_SERVICE_GET_PERSONS = "book-sales-service-get-persons";
    private final String PROVIDER_PERSON_DATA_SERVICE = "person-data-service";

    @Pact(consumer = CONSUMER_BOOK_SALES_SERVICE_GET_PERSONS, provider = PROVIDER_PERSON_DATA_SERVICE)
    public V4Pact whenRequestPersonById_thenReturnsPerson(PactDslWithProvider builder) {
        PactDslJsonBody bodyResponse = new PactDslJsonBody()
                .integerType("id", A_PERSON_ID)
                .stringType("name", A_PERSON_NAME)
                .stringType("passportNumber", A_PERSON_PASSPORT_NUMBER);

        return builder
                .given("it has a person and status code is 200")
                    .uponReceiving("a request to retrieve a person by id")
                    .path("/persons/" +A_PERSON_ID)
                    .method("GET")
                .willRespondWith()
                    .headers(Collections.singletonMap("Content-Type", "application/json"))
                    .status(OK.value())
                    .body(bodyResponse)
                .toPact(V4Pact.class);
    }

    @PactTestFor(providerName = PROVIDER_PERSON_DATA_SERVICE, pactMethod = "whenRequestPersonById_thenReturnsPerson", providerType = SYNCH)
    @Test
    public void whenRequestPersonById_thenReturnsPerson(MockServer mockServer) {
        // given
        WebClient webClient = WebClient.builder()
                .baseUrl(mockServer.getUrl())
                .build();

        // when
        PersonDataServiceWebClient personDataServiceWebClient = new PersonDataServiceWebClient(webClient, "/persons/{personId}");
        PersonDataResponse personDataResponse = personDataServiceWebClient.retrievePerson(A_PERSON_ID);

        // then
        assertThat(personDataResponse.getId()).isInstanceOf(Long.class).isEqualTo(A_PERSON_ID);
        assertThat(personDataResponse.getName()).isInstanceOf(String.class).isEqualTo(A_PERSON_NAME);
        assertThat(personDataResponse.getPassportNumber()).isInstanceOf(String.class).isEqualTo(A_PERSON_PASSPORT_NUMBER);
    }

}
