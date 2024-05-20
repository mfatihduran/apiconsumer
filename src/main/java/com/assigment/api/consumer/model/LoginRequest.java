package com.assigment.api.consumer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequest implements Serializable {
    private String email;
    private String password;
}
