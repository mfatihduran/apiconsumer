package com.assigment.api.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TransactionsReportResponse implements Serializable {
    private String status;
    @JsonProperty("response")
    private List<TransactionsReportResponseDetail> transactionsReportResponseDetails;
}

