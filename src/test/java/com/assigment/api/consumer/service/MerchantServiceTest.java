package com.assigment.api.consumer.service;

import com.assigment.api.consumer.enums.Status;
import com.assigment.api.consumer.model.LoginRequest;
import com.assigment.api.consumer.model.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static com.assigment.api.consumer.enums.Status.APPROVED;
import static com.assigment.api.consumer.enums.Status.DECLINED;
import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static reactor.test.StepVerifier.create;

class MerchantServiceTest extends AbstractServiceTest {
    private MerchantService target;

    @BeforeEach
    protected void initialize() {
        super.initialize();
        target = new MerchantService(environment);
    }

    @Test
    public void testLogin_Success() throws JsonProcessingException {
        testLogin(APPROVED, "TOKEN");
    }

    @Test
    public void testLogin_Failure() throws JsonProcessingException {
        testLogin(DECLINED, "");
    }

    @Test
    public void testLogin_With_4XX_Error() {
        testLogin(IllegalArgumentException.class, BAD_REQUEST);
    }

    @Test
    public void testLogin_With_5XX_Error() {
        testLogin(Exception.class, INTERNAL_SERVER_ERROR);
    }

    private void testLogin(final Status status, final String token) throws JsonProcessingException {
        when(environment.getProperty(eq("merchant.user.login.url"))).thenReturn("/api/v3/merchant/user/login");

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(getLoginResponse(status, token)))
                .addHeader("Content-Type", "application/json"));

        final Mono<ResponseEntity<LoginResponse>> actualResponse = target.login(getUserCredentials());

        if (status.equals(APPROVED)) {
            create(actualResponse)
                    .expectNextMatches(response -> requireNonNull(response.getBody()).getStatus()
                            .equals(APPROVED.toString()) && response.getBody().getToken().equals(token))
                    .verifyComplete();
        } else {
            create(actualResponse)
                    .expectNextMatches(response -> requireNonNull(response.getBody()).getStatus()
                            .equals(DECLINED.toString()) && response.getBody().getToken().equals(""))
                    .verifyComplete();
        }
    }

    private void testLogin(final Class<? extends Throwable> clazz, final HttpStatus httpStatus) {
        when(environment.getProperty(eq("merchant.user.login.url"))).thenReturn("/api/v3/merchant/user/login");

        mockBackEnd.enqueue(new MockResponse()
                .setBody("")
                .setResponseCode(httpStatus.value()));

        final Mono<ResponseEntity<LoginResponse>> actualResponse = target.login(getUserCredentials());

        create(actualResponse)
                .expectError(clazz).verify();
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