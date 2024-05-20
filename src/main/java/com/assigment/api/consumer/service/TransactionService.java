package com.assigment.api.consumer.service;

import com.assigment.api.consumer.model.TransactionsQueryRequest;
import com.assigment.api.consumer.model.TransactionsQueryResponse;
import com.assigment.api.consumer.model.TransactionsReportRequest;
import com.assigment.api.consumer.model.TransactionsReportResponse;
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
public class TransactionService {
    private WebClient webClient;

    private Environment environment;

    @Autowired
    public TransactionService(Environment environment) {
        this.environment = environment;
        this.webClient = WebClient.create(requireNonNull(environment.getProperty("base.url")));
    }

    public Mono<ResponseEntity<TransactionsReportResponse>> postTransactionsReport(final TransactionsReportRequest transactionsReportRequest, final String token) {
        return webClient.post()
                .uri(requireNonNull(environment.getProperty("transactions.report.url")))
                .body(just(transactionsReportRequest), TransactionsReportRequest.class)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (response) -> (just(new IllegalArgumentException())))
                .onStatus(HttpStatusCode::is5xxServerError, (response) -> (just(new Exception())))
                .toEntity(TransactionsReportResponse.class);
    }

    public Mono<ResponseEntity<TransactionsQueryResponse>> postTransactionsQuery(final TransactionsQueryRequest transactionsQueryRequest, final String token) {
        return webClient.post()
                .uri(requireNonNull(environment.getProperty("transactions.list.url")))
                .body(just(transactionsQueryRequest), TransactionsQueryRequest.class)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (response) -> (just(new IllegalArgumentException())))
                .onStatus(HttpStatusCode::is5xxServerError, (response) -> (just(new Exception())))
                .toEntity(TransactionsQueryResponse.class);
    }

}
