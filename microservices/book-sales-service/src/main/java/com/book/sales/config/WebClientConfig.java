package com.book.sales.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class WebClientConfig {

    @Value("${service-client.book-data-service.base-url}")
    public String bookDataBasePath;

    @Value("${service-client.person-data-service.base-url}")
    public String personDataBasePath;

    @Bean("bookDataWebClient")
    public WebClient bookDataWebClient() {
        return WebClient.builder()
                .baseUrl(bookDataBasePath)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }
    @Bean("personDataWebClient")
    public WebClient personDataWebClient() {
        return WebClient.builder()
                .baseUrl(personDataBasePath)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }

}
