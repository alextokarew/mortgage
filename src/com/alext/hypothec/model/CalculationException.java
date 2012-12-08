package com.alext.hypothec.model;

public class CalculationException extends Throwable {

    private final ErrorType type;

    public CalculationException(ErrorType type) {
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }
}

