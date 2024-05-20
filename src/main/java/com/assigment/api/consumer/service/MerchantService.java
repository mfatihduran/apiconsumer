package com.assigment.api.consumer.service;

import com.assigment.api.consumer.model.LoginRequest;
import com.assigment.api.consumer.model.LoginResponse;
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
public class MerchantService {
    private WebClient webClient;
    private final Environment environment;

    @Autowired
    public MerchantService(Environment environment) {
        this.environment = environment;
        this.webClient = WebClient.create(requireNonNull(environment.getProperty("base.url")));
    }

    public Mono<ResponseEntity<LoginResponse>> login(final LoginRequest loginRequest) {
        return webClient.post()
                .uri(requireNonNull(environment.getProperty("merchant.user.login.url")))
                .body(just(loginRequest), LoginRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (response) -> (just(new IllegalArgumentException())))
                .onStatus(HttpStatusCode::is5xxServerError, (response) -> (just(new Exception())))
                .toEntity(LoginResponse.class);
    }
}
