package com.assigment.api.consumer.controller;

import com.assigment.api.consumer.enums.Status;
import com.assigment.api.consumer.model.LoginRequest;
import com.assigment.api.consumer.model.LoginResponse;
import com.assigment.api.consumer.service.MerchantService;
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
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.*;
import static reactor.core.publisher.Mono.just;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
public class MerchantControllerTest {
    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private MerchantController target;

    @Test
    public void testUserLogin_Success() {
        testUserLogin(APPROVED, "TOKEN");
    }

    @Test
    public void testUserLogin_Failure() {
        testUserLogin(DECLINED, "");
    }

    private void testUserLogin(final Status expectedStatus, final String expectedToken) {
        final LoginResponse expectedResponse = getLoginResponse(expectedStatus, expectedToken);
        final LoginRequest userCredentials = getUserCredentials();
        when(merchantService.login(eq(userCredentials))).thenReturn(just(of(Optional.of(expectedResponse))));

        final Mono<ResponseEntity<LoginResponse>> actualResponse = target.userLogin(userCredentials);

        create(actualResponse)
                .expectNextMatches(response -> requireNonNull(response.getBody()).getStatus().equals(expectedStatus.toString()) && nonNull(response.getBody().getToken()))
                .expectComplete()
                .verify();
    }

    private LoginRequest getUserCredentials() {
        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("1234");

        return loginRequest;
    }

    private LoginResponse getLoginResponse(final Status responseStatus, final  String token) {
        final LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatus(responseStatus.toString());
        loginResponse.setToken(token);

        return loginResponse;
    }
}