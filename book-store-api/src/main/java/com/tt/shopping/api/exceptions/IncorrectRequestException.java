package com.tt.shopping.api.exceptions;

public class IncorrectRequestException extends RuntimeException {

    public IncorrectRequestException(String message) {
        super(message);
    }
}
