package com.assigment.api.consumer.controller;

import com.assigment.api.consumer.model.ClientQueryRequest;
import com.assigment.api.consumer.model.ClientQueryResponse;
import com.assigment.api.consumer.model.CustomerInfo;
import com.assigment.api.consumer.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.of;
import static reactor.core.publisher.Mono.just;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController target;

    @Test
    void testPostClientQuery_Success() {
        testPostClientQuery(true);
    }

    @Test
    void testPostClientQuery_Failure() {
        testPostClientQuery(false);
    }

    private void testPostClientQuery(final boolean isSuccessful) {
        final ClientQueryResponse expectedResponse = getClientQueryResponse(isSuccessful);
        final ClientQueryRequest queryRequest = getClientQueryRequest();
        when(clientService.postClientQuery(eq(queryRequest), eq("TOKEN"))).thenReturn(just(of(Optional.of(expectedResponse))));

        final Mono<ResponseEntity<ClientQueryResponse>> actualResponse = target.postClientQuery(queryRequest, "TOKEN");

        create(actualResponse)
                .expectNextMatches(response -> isSuccessful ? nonNull(requireNonNull(response.getBody()).getCustomerInfo()): isNull(requireNonNull(response.getBody()).getCustomerInfo()))
                .expectComplete()
                .verify();
    }

    private ClientQueryRequest getClientQueryRequest() {
        final ClientQueryRequest queryRequest = new ClientQueryRequest();
        queryRequest.setTransactionId(1);

        return queryRequest;
    }

    private ClientQueryResponse getClientQueryResponse(boolean isSuccessful) {
        final ClientQueryResponse queryResponse = new ClientQueryResponse();

        if (!isSuccessful) {
            queryResponse.setCustomerInfo(null);
        } else {
            final CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setId(1);
            customerInfo.setBillingFirstName("BillingName");
            customerInfo.setBillingLastName("BillingLastName");
            queryResponse.setCustomerInfo(customerInfo);
        }

        return queryResponse;
    }
}