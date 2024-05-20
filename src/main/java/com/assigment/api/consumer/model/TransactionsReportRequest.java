package com.assigment.api.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionsReportRequest implements Serializable {
    @JsonProperty(required = true)
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private LocalDate fromDate;
    @JsonProperty(required = true)
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private LocalDate toDate;
    private int merchant;
    private int acquirer;
}
