package com.assigment.api.consumer.service;

import com.assigment.api.consumer.model.ClientQueryRequest;
import com.assigment.api.consumer.model.ClientQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;
import static reactor.core.publisher.Mono.just;

@Service
public class ClientService {
    private WebClient webClient;
    private final Environment environment;

    @Autowired
    public ClientService(Environment environment) {
        this.environment = environment;
        this.webClient = WebClient.create(requireNonNull(environment.getProperty("base.url")));
    }

    public Mono<ResponseEntity<ClientQueryResponse>> postClientQuery(final ClientQueryRequest clientQueryRequest, final String token) {
        return webClient.post()
                .uri(requireNonNull(environment.getProperty("client.query.url")))
                .body(just(clientQueryRequest), ClientQueryRequest.class)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (response) -> (just(new IllegalArgumentException())))
                .onStatus(HttpStatusCode::is5xxServerError, (response) -> (just(new Exception())))
                .toEntity(ClientQueryResponse.class);
    }
}
