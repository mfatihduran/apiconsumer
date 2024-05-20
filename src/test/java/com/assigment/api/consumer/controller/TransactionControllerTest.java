package com.assigment.api.consumer.controller;

import com.assigment.api.consumer.enums.Status;
import com.assigment.api.consumer.model.TransactionDataItem;
import com.assigment.api.consumer.model.TransactionInfo;
import com.assigment.api.consumer.model.TransactionsQueryRequest;
import com.assigment.api.consumer.model.TransactionsQueryResponse;
import com.assigment.api.consumer.model.TransactionsReportRequest;
import com.assigment.api.consumer.model.TransactionsReportResponse;
import com.assigment.api.consumer.model.TransactionsReportResponseDetail;
import com.assigment.api.consumer.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.assigment.api.consumer.enums.Status.APPROVED;
import static com.assigment.api.consumer.enums.Status.DECLINED;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.of;
import static reactor.core.publisher.Mono.just;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController target;

    @Test
    void testPostTransactionsReport_Success() {
        testPostTransactionsReport(APPROVED);
    }

    @Test
    void testPostTransactionsReport_Failure() {
        testPostTransactionsReport(DECLINED);
    }

    @Test
    void testPostTransactionsQuery_Success() {
        testPostTransactionsQuery(true);
    }

    @Test
    void testPostTransactionsQuery_Failure() {
        testPostTransactionsQuery(false);
    }

    @Mock
    private TransactionsReportRequest transactionsReportRequest;

    @Mock
    private TransactionsQueryRequest transactionsQueryRequest;

    private void testPostTransactionsReport(final Status expectedStatus) {
        final TransactionsReportResponse expectedResponse = getTransactionsReportResponse(expectedStatus);
        when(transactionService.postTransactionsReport(any(TransactionsReportRequest.class), eq("TOKEN"))).thenReturn(just(of(Optional.of(expectedResponse))));

        final Mono<ResponseEntity<TransactionsReportResponse>> actualResponse = target.postTransactionsReport(transactionsReportRequest, "TOKEN");

        if (expectedStatus.equals(APPROVED)) {
            create(actualResponse)
                    .expectNextMatches(response ->
                            requireNonNull(response.getBody()).getStatus().equals(expectedStatus.toString()) && nonNull(response.getBody().getTransactionsReportResponseDetails()) && !response.getBody().getTransactionsReportResponseDetails().isEmpty())
                    .expectComplete()
                    .verify();
        } else {
            create(actualResponse)
                    .expectNextMatches(response ->
                            requireNonNull(response.getBody()).getStatus().equals(expectedStatus.toString()) && nonNull(response.getBody().getTransactionsReportResponseDetails()) && response.getBody().getTransactionsReportResponseDetails().isEmpty())
                    .expectComplete()
                    .verify();
        }

    }
    private void testPostTransactionsQuery(final boolean isSuccessful) {
        final TransactionsQueryResponse expectedResponse = getTransactionsQueryResponse(isSuccessful);
        when(transactionService.postTransactionsQuery(any(TransactionsQueryRequest.class), eq("TOKEN"))).thenReturn(just(of(Optional.of(expectedResponse))));

        final Mono<ResponseEntity<TransactionsQueryResponse>> actualResponse = target.postTransactionsQuery(transactionsQueryRequest, "TOKEN");

        if (isSuccessful) {
            create(actualResponse)
                    .expectNextMatches(response -> !requireNonNull(response.getBody()).getTransactionDataItems().isEmpty())
                    .expectComplete()
                    .verify();
        } else {
            create(actualResponse)
                    .expectNextMatches(response -> isNull(requireNonNull(response.getBody()).getTransactionDataItems()))
                    .expectComplete()
                    .verify();
        }

    }
    private TransactionsReportResponse getTransactionsReportResponse(final Status expectedStatus) {
        final TransactionsReportResponse response = new TransactionsReportResponse();

        response.setStatus(expectedStatus.toString());

        if (expectedStatus.equals(APPROVED)) {
           response.setTransactionsReportResponseDetails(of(new TransactionsReportResponseDetail(1, 1, "USD")));
        } else {
           response.setTransactionsReportResponseDetails(emptyList());
        }

        return response;
    }

    private TransactionsQueryResponse getTransactionsQueryResponse(final boolean isSuccessful) {
        final TransactionsQueryResponse response = new TransactionsQueryResponse();

        if (isSuccessful) {
            final TransactionDataItem dataItem = new TransactionDataItem();
            dataItem.setTransactionInfo(new TransactionInfo("reference", "status"));
            dataItem.setRefundable(true);
            response.setTransactionDataItems(of(dataItem));
        }

        return response;
    }
}