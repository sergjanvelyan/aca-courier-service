package com.aca.acacourierservice.exception;

public class CourierServiceException extends RuntimeException{
    public CourierServiceException() {
    }
    public CourierServiceException(String message) {
        super(message);
    }
    public CourierServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public CourierServiceException(Throwable cause) {
        super(cause);
    }
}