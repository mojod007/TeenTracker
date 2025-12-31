package com.trace.product.enums;

public enum TypeGestion {
    FIFO("First In First Out"),
    LIFO("Last In First Out"),
    FEFO("First Expired First Out"),
    MANUEL("Manuel");

    private final String description;

    TypeGestion(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
