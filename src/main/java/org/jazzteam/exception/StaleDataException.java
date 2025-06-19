package org.jazzteam.exception;

public class StaleDataException extends EntityException {
    public StaleDataException(String message) {
        super(message);
    }
}
