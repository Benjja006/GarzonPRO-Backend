package com.garzonpro.Payment.exception;

public class PagoNotFoundException extends RuntimeException {
    public PagoNotFoundException(String message) {
        super(message);
    }
}