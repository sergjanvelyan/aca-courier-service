package com.aca.acacourierservice.exception;

public class InvalidStoreCredentialsException extends  RuntimeException{
    public InvalidStoreCredentialsException() {
        super();
    }

    public InvalidStoreCredentialsException(String message) {
        super(message);
    }

    public InvalidStoreCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidStoreCredentialsException(Throwable cause) {
        super(cause);
    }
}
