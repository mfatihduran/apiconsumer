package com.assigment.api.consumer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MerchantInfo implements Serializable {
    private int id;
    private String name;
}
