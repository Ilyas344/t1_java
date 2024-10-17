package ru.t1.java.demo.exception;

public class AuthException extends RuntimeException{
    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }
}
