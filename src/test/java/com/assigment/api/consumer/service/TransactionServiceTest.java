package com.assigment.api.consumer.service;

import com.assigment.api.consumer.enums.Status;
import com.assigment.api.consumer.model.TransactionDataItem;
import com.assigment.api.consumer.model.TransactionInfo;
import com.assigment.api.consumer.model.TransactionsQueryRequest;
import com.assigment.api.consumer.model.TransactionsQueryResponse;
import com.assigment.api.consumer.model.TransactionsReportRequest;
import com.assigment.api.consumer.model.TransactionsReportResponse;
import com.assigment.api.consumer.model.TransactionsReportResponseDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.assigment.api.consumer.enums.Status.APPROVED;
import static com.assigment.api.consumer.enums.Status.DECLINED;
import static java.time.LocalDate.now;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static reactor.test.StepVerifier.create;

class TransactionServiceTest extends AbstractServiceTest {
    private TransactionService target;

    @BeforeEach
    protected void initialize() {
        super.initialize();
        target = new TransactionService(environment);
    }

    @Test
    public void testPostTransactionsReport_Success() throws JsonProcessingException, InterruptedException {
        testPostTransactionsReport(APPROVED);
    }

    @Test
    public void testPostTransactionsReport_Failure() throws JsonProcessingException, InterruptedException {
        testPostTransactionsReport(DECLINED);
    }

    @Test
    public void testPostTransactionsReport_With_4XX_Error() {
        testPostTransactionsReport(IllegalArgumentException.class, BAD_REQUEST);
    }

    @Test
    public void testPostTransactionsReport_With_5XX_Error() {
        testPostTransactionsReport(Exception.class, INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testPostTransactionsQuery_Success() throws JsonProcessingException, InterruptedException {
        testPostTransactionsQuery(true);
    }

    @Test
    public void testPostTransactionsQuery_Failure() throws JsonProcessingException, InterruptedException {
        testPostTransactionsQuery(false);
    }

    @Test
    public void testPostTransactionsQuery_With_4XX_Error() {
        testPostTransactionsQuery(IllegalArgumentException.class, BAD_REQUEST);
    }

    @Test
    public void testPostTransactionsQuery_With_5XX_Error() {
        testPostTransactionsQuery(Exception.class, INTERNAL_SERVER_ERROR);
    }

    private void testPostTransactionsReport(final Status status) throws JsonProcessingException, InterruptedException {
        when(environment.getProperty(eq("transactions.report.url"))).thenReturn("/api/v3/transactions/report");

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(getTransactionsReportResponse(status)))
                .setResponseCode(OK.value())
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "TOKEN"));

        final Mono<ResponseEntity<TransactionsReportResponse>> actualResponse = target.postTransactionsReport(getTransactionsReportRequest(), "TOKEN");

        if (status.equals(APPROVED)) {
            create(actualResponse)
                    .expectNextMatches(response -> response.getStatusCode().equals(OK) &&
                            requireNonNull(response.getBody()).getStatus().equals(status.toString()) &&
                            !response.getBody().getTransactionsReportResponseDetails().isEmpty())
                    .verifyComplete();
        } else {
            create(actualResponse)
                    .expectNextMatches(response -> response.getStatusCode().equals(OK) &&
                            requireNonNull(response.getBody()).getStatus().equals(status.toString()) &&
                            response.getBody().getTransactionsReportResponseDetails().isEmpty())
                    .verifyComplete();
        }

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("TOKEN", recordedRequest.getHeader("Authorization"));
    }

    private void testPostTransactionsReport(final Class<? extends Throwable> clazz, final HttpStatus httpStatus) {
        when(environment.getProperty(eq("transactions.report.url"))).thenReturn("/api/v3/transactions/report");

        mockBackEnd.enqueue(new MockResponse()
                .setBody("")
                .addHeader("Authorization", "TOKEN")
                .setResponseCode(httpStatus.value()));

        final Mono<ResponseEntity<TransactionsReportResponse>> actualResponse = target.postTransactionsReport(getTransactionsReportRequest(), "TOKEN");

        create(actualResponse)
                .expectError(clazz).verify();
    }

    private void testPostTransactionsQuery(final boolean isSuccessful) throws JsonProcessingException, InterruptedException {
        when(environment.getProperty(eq("transactions.list.url"))).thenReturn("/api/v3/transaction/list");

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(getTransactionsQueryResponse(isSuccessful)))
                .setResponseCode(OK.value())
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "TOKEN"));

        final Mono<ResponseEntity<TransactionsQueryResponse>> actualResponse = target.postTransactionsQuery(getTransactionsQueryRequest(), "TOKEN");

        if (isSuccessful) {
            create(actualResponse)
                    .expectNextMatches(response -> nonNull(requireNonNull(response.getBody()).getTransactionDataItems()))
                    .verifyComplete();
        } else {
            create(actualResponse)
                    .expectNextMatches(response -> isNull(requireNonNull(response.getBody()).getTransactionDataItems()))
                    .verifyComplete();
        }

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("TOKEN", recordedRequest.getHeader("Authorization"));
    }

    private void testPostTransactionsQuery(final Class<? extends Throwable> clazz, final HttpStatus httpStatus) {
        when(environment.getProperty(eq("transactions.list.url"))).thenReturn("/api/v3/transaction/list");

        mockBackEnd.enqueue(new MockResponse()
                .setBody("")
                .addHeader("Authorization", "TOKEN")
                .setResponseCode(httpStatus.value()));

        final Mono<ResponseEntity<TransactionsQueryResponse>> actualResponse = target.postTransactionsQuery(getTransactionsQueryRequest(), "TOKEN");

        create(actualResponse)
                .expectError(clazz).verify();
    }
    private TransactionsReportRequest getTransactionsReportRequest() {
        final TransactionsReportRequest reportRequest = new TransactionsReportRequest();
        reportRequest.setMerchant(1);
        reportRequest.setAcquirer(1);
        reportRequest.setFromDate(now());
        reportRequest.setToDate(now());

        return reportRequest;
    }

    private TransactionsReportResponse getTransactionsReportResponse(final Status responseStatus) {
        final TransactionsReportResponse reportResponse = new TransactionsReportResponse();
        reportResponse.setStatus(responseStatus.toString());

        if (responseStatus.equals(APPROVED)) {
            reportResponse.setTransactionsReportResponseDetails(of(new TransactionsReportResponseDetail(1, 1, "USD")));
        } else {
            reportResponse.setTransactionsReportResponseDetails(emptyList());
        }

        return  reportResponse;
    }

    private TransactionsQueryRequest getTransactionsQueryRequest() {
        final TransactionsQueryRequest request = new TransactionsQueryRequest();
        request.setStatus(APPROVED.toString());

        return request;
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