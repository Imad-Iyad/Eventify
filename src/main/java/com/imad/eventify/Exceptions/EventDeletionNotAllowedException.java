package com.imad.eventify.Exceptions;

public class EventDeletionNotAllowedException extends RuntimeException {
    public EventDeletionNotAllowedException(String message) {
        super(message);
    }
}
