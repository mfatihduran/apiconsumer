package com.assigment.api.consumer.controller;

import com.assigment.api.consumer.model.ClientQueryRequest;
import com.assigment.api.consumer.model.ClientQueryResponse;
import com.assigment.api.consumer.model.LoginRequest;
import com.assigment.api.consumer.model.LoginResponse;
import com.assigment.api.consumer.service.ClientService;
import com.assigment.api.consumer.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/client")
    @ResponseStatus(OK)
    public Mono<ResponseEntity<ClientQueryResponse>> postClientQuery(@RequestBody ClientQueryRequest clientQueryRequest, @RequestHeader String token) {
        return clientService.postClientQuery(clientQueryRequest, token);
    }
}
