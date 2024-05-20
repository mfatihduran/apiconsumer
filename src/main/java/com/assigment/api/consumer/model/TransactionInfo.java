package com.assigment.api.consumer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransactionInfo implements Serializable {
    private String referenceNo;
    private String status;

    public TransactionInfo(String referenceNo, String status) {
        this.referenceNo = referenceNo;
        this.status = status;
    }
}
