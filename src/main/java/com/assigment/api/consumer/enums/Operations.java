package com.assigment.api.consumer.enums;

public enum Operations {
    DIRECT("DIRECT"),
    REFUND("REFUND"),
    AUTH("AUTH"),
    STORED("STORED"),
    _3D("3D"),
    _3DAUTH("3DAUTH");

    private final String operationCode;

    Operations(String operationCode) {
        this.operationCode = operationCode;
    }

    @Override
    public String toString() {
        return operationCode;
    }
}
