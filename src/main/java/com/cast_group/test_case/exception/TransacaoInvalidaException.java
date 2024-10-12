package com.cast_group.test_case.exception;

public class TransacaoInvalidaException extends RuntimeException {
    public TransacaoInvalidaException(String message) {
        super(message);
    }
}
