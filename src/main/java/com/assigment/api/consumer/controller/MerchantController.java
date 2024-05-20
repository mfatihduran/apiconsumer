package com.assigment.api.consumer.controller;

import com.assigment.api.consumer.model.LoginRequest;
import com.assigment.api.consumer.model.LoginResponse;
import com.assigment.api.consumer.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("merchants")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    @PostMapping("/user/login")
    @ResponseStatus(OK)
    public Mono<ResponseEntity<LoginResponse>> userLogin(@RequestBody LoginRequest loginRequest) {
        return merchantService.login(loginRequest);
    }
}
