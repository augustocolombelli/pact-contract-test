package com.book.sales.client.persondata;

import com.book.sales.client.persondata.dto.PersonDataResponse;
import com.book.sales.client.persondata.exception.PersonDataClientException;
import com.book.sales.client.persondata.exception.PersonDataServerException;
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

import static com.book.sales.fake.PersonDataResponseTestDataBuilder.aPersonDataResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class PersonDataServiceWebClientTest {

    private static final long A_PERSON_ID = 1254L;

    private PersonDataServiceWebClient personDataServiceWebClient;
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
        personDataServiceWebClient = new PersonDataServiceWebClient(webClient, "/persons/{personId}");
    }

    @Nested
    class RetrievePerson {

        @Test
        void whenStatusCodeIs200_thenReturnsPerson() throws JsonProcessingException {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(aPersonDataResponse(A_PERSON_ID)))
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            PersonDataResponse response = personDataServiceWebClient.retrievePerson(A_PERSON_ID);

            // then
            assertThat(response.getId()).isEqualTo(A_PERSON_ID);
        }

        @Test
        void whenStatusCodeIs400_thenThrowsClientException() {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody("error")
                    .setResponseCode(400)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            Throwable thrown = catchThrowable(() -> personDataServiceWebClient.retrievePerson(A_PERSON_ID));

            // then
            assertThat(thrown)
                    .isInstanceOf(PersonDataClientException.class)
                    .hasMessageContaining("A client error occurred when making request to person data service.");
        }

        @Test
        void whenStatusCodeIs500_thenThrowsServerException() {
            // given
            mockWebServer.enqueue(new MockResponse()
                    .setBody("error")
                    .setResponseCode(500)
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json"));

            // when
            Throwable thrown = catchThrowable(() -> personDataServiceWebClient.retrievePerson(A_PERSON_ID));

            // then
            assertThat(thrown)
                    .isInstanceOf(PersonDataServerException.class)
                    .hasMessageContaining("A server error occurred when making request to person data service.");
        }

    }

}
