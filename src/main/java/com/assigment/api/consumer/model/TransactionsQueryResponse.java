package com.assigment.api.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TransactionsQueryResponse implements Serializable {
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("current_page")
    private int currentPage;
    @JsonProperty("next_page_url")
    private String nextPageUrl;
    @JsonProperty("prev_page_url")
    private String prevPageUrl;
    private int from;
    private int to;
    @JsonProperty("data")
    private List<TransactionDataItem> transactionDataItems;
}
