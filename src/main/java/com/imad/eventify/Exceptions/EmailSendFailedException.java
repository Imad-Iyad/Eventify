package com.imad.eventify.Exceptions;

public class EmailSendFailedException extends RuntimeException{
    public EmailSendFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
