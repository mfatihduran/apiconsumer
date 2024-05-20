package com.assigment.api.consumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerInfo implements Serializable {
    private int id;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;
    private String customerNumber;
    private String expiryMonth;
    private String expiryYear;
    private String startMonth;
    private String startYear;
    private String issueNumber;
    private String email;
    private LocalDateTime birthDay;
    private String gender;
    private String billingTitle;
    private String billingFirstName;
    private String billingLastName;
    private String billingCompany;
    private String billingAddress1;
    private String billingAddress2;
    private String billingCity;
    private String billingPostcode;
    private String billingState;
    private String billingCountry;
    private String billingPhone;
    private String billingFax;
    private String shippingTitle;
    private String shippingFirstName;
    private String shippingLastName;
    private String shippingCompany;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingCity;
    private String shippingPostcode;
    private String shippingState;
    private String shippingCountry;
    private String shippingPhone;
    private String shippingFax;
}
