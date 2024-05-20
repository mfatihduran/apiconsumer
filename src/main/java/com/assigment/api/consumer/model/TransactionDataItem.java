package com.assigment.api.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TransactionDataItem implements Serializable {
    @JsonProperty("fx")
    private FxInformation fxInformation;
    private CustomerInfo customerInfo;
    @JsonProperty("merchant")
    private MerchantInfo merchantInfo;
    @JsonProperty("ipn")
    private IpnInformation ipnInformation;
    @JsonProperty("transaction")
    private TransactionInfo transactionInfo;
    @JsonProperty("acquirer")
    private AcquirerInfo acquirerInfo;
    private boolean refundable;
}
