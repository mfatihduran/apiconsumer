package com.assigment.api.consumer.controller;

import com.assigment.api.consumer.model.TransactionsQueryRequest;
import com.assigment.api.consumer.model.TransactionsQueryResponse;
import com.assigment.api.consumer.model.TransactionsReportRequest;
import com.assigment.api.consumer.model.TransactionsReportResponse;
import com.assigment.api.consumer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(path = "/report")
    public Mono<ResponseEntity<TransactionsReportResponse>> postTransactionsReport(@RequestBody final TransactionsReportRequest transactionsReportRequest, @RequestHeader final String token) {
        return transactionService.postTransactionsReport(transactionsReportRequest, token);
    }

    @PostMapping(path = "/list")
    public Mono<ResponseEntity<TransactionsQueryResponse>> postTransactionsQuery(@RequestBody final TransactionsQueryRequest transactionsQueryRequest, @RequestHeader final String token) {
        return transactionService.postTransactionsQuery(transactionsQueryRequest, token);
    }
}
