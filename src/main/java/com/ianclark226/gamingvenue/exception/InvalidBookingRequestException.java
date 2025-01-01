package com.ianclark226.gamingvenue.exception;

public class InvalidBookingRequestException extends RuntimeException {
    public InvalidBookingRequestException(String message) {
        super(message);
    }
}
