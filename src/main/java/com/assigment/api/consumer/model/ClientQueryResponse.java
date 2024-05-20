package com.assigment.api.consumer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ClientQueryResponse implements Serializable {
    private CustomerInfo customerInfo;
}
