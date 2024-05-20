package com.assigment.api.consumer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClientQueryRequest implements Serializable {
    private int transactionId;
}
