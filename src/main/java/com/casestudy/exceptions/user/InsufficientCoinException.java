package com.casestudy.exceptions.user;

public class InsufficientCoinException extends RuntimeException{
    public InsufficientCoinException() {
        super();
    }

    public InsufficientCoinException(String message) {
        super(message);
    }
}
