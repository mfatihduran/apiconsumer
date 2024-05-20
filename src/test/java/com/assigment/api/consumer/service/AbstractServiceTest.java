package com.assigment.api.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.io.IOException;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractServiceTest {
    @Mock
    protected Environment environment;
    protected static MockWebServer mockBackEnd;
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    protected void initialize() {
        when(environment.getProperty(eq("base.url"))).thenReturn(format("http://localhost:%s", mockBackEnd.getPort()));
    }

    @BeforeAll
    protected static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    protected static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }
}