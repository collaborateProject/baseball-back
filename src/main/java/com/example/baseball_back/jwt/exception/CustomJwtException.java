package com.example.baseball_back.jwt.exception;

public class CustomJwtException extends RuntimeException {
    public CustomJwtException(String msg) {
        super(msg);
    }

    public CustomJwtException(String msg, Exception e) {
    }
}