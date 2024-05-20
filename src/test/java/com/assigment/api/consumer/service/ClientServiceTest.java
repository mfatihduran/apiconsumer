package com.assigment.api.consumer.service;

import com.assigment.api.consumer.model.ClientQueryRequest;
import com.assigment.api.consumer.model.ClientQueryResponse;
import com.assigment.api.consumer.model.CustomerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static reactor.test.StepVerifier.create;

class ClientServiceTest extends AbstractServiceTest {
    private ClientService target;

    @BeforeEach
    protected void initialize() {
        super.initialize();
        target = new ClientService(environment);
    }

    @Test
    public void testClientQuery_Success() throws JsonProcessingException, InterruptedException {
       testClientQuery(true);
    }

    @Test
    public void testClientQuery_Failure() throws JsonProcessingException, InterruptedException {
        testClientQuery(false);
    }

    @Test
    public void testPostTransactionsReport_With_4XX_Error() {
        testClientQuery(IllegalArgumentException.class, BAD_REQUEST);
    }

    @Test
    public void testPostTransactionsReport_With_5XX_Error() {
        testClientQuery(Exception.class, INTERNAL_SERVER_ERROR);
    }

    private void testClientQuery(final boolean isSuccessful) throws JsonProcessingException, InterruptedException {
        when(environment.getProperty(eq("client.query.url"))).thenReturn("/api/v3/client");

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(getClientQueryResponse(isSuccessful)))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "TOKEN"));

        final Mono<ResponseEntity<ClientQueryResponse>> actualResponse = target.postClientQuery(getClientQueryRequest(), "TOKEN");

        if (isSuccessful) {
            create(actualResponse)
                    .expectNextMatches(response -> nonNull(requireNonNull(response.getBody()).getCustomerInfo()))
                    .verifyComplete();
        } else {
            create(actualResponse)
                    .expectNextMatches(response -> isNull(requireNonNull(response.getBody()).getCustomerInfo()))
                    .verifyComplete();
        }

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("TOKEN", recordedRequest.getHeader("Authorization"));
    }

    private void testClientQuery(final Class<? extends Throwable> clazz, final HttpStatus httpStatus) {
        when(environment.getProperty(eq("client.query.url"))).thenReturn("/api/v3/client");

        mockBackEnd.enqueue(new MockResponse()
                .setBody("")
                .addHeader("Authorization", "TOKEN")
                .setResponseCode(httpStatus.value()));

        final Mono<ResponseEntity<ClientQueryResponse>> actualResponse = target.postClientQuery(getClientQueryRequest(), "TOKEN");

        create(actualResponse)
                .expectError(clazz).verify();
    }

    private ClientQueryRequest getClientQueryRequest() {
        final ClientQueryRequest clientQueryRequest = new ClientQueryRequest();
        clientQueryRequest.setTransactionId(1);
        return clientQueryRequest;
    }

    private ClientQueryResponse getClientQueryResponse(final boolean isSuccessful) {
        final ClientQueryResponse clientQueryResponse = new ClientQueryResponse();

        if (isSuccessful) {
            final CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setId(1);
            customerInfo.setBillingFirstName("FirstName");
            customerInfo.setBillingLastName("LastName");
            clientQueryResponse.setCustomerInfo(customerInfo);

        } else {
            clientQueryResponse.setCustomerInfo(null);
        }

        return  clientQueryResponse;
    }

}