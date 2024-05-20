package com.assigment.api.consumer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class TransactionsQueryRequest implements Serializable {
    private Date fromDate;
    private Date toDate;
    private String status;
    private String operation;
    private int merchantId;
    private int acquirerId;
    private String paymentMethod;
    private String errorCode;
    private String filterField;
    private String filterValue;
    private int page;
}
