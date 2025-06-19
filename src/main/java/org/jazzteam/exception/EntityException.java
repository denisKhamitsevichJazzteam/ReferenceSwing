package org.jazzteam.exception;

public abstract class EntityException extends RuntimeException {
    protected EntityException(String message) {
        super(message);
    }
}
