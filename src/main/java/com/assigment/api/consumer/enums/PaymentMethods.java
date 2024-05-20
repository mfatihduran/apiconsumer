package com.assigment.api.consumer.enums;

public enum PaymentMethods {
    CREDITCARD("CREDITCARD"),
    CUP("CUP"),
    IDEAL("IDEAL"),
    GIROPAY("GIROPAY"),
    MISTERCASH("3D"),
    STORED("STORED"),
    PAYTOCARD("PAYTOCARD"),
    CEPBANK("CEPBANK"),
    CITADEL("CITADEL");

    private final String methodName;

    PaymentMethods(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return methodName;
    }
}
