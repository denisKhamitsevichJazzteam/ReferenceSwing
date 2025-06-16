package org.jazzteam.event.model;

/**
 * Represents a generic application event.
 * <p>
 * All application events should implement this interface to provide their event type.
 */
public interface AppEvent {
    EventType getType();
}

