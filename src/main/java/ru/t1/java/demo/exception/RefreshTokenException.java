package ru.t1.java.demo.exception;

public class RefreshTokenException extends RuntimeException{
    public RefreshTokenException() {
    }

    public RefreshTokenException(String message) {
        super(message);
    }
}
