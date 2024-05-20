package com.assigment.api.consumer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransactionsReportResponseDetail implements Serializable {
    private int count;
    private int total;
    private String currency;

    public TransactionsReportResponseDetail(int count, int total, String currency) {
        this.count = count;
        this.total = total;
        this.currency = currency;
    }
}

