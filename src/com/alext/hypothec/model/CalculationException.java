package com.alext.hypothec.model;

public class CalculationException extends Throwable {
    public static enum ErrorType {
        INVALID_PERCENT, ZERO_REPAY
    }

    private final ErrorType type;

    public CalculationException(ErrorType type) {
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }
}

