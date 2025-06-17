package org.jazzteam.event.listener;

import org.jazzteam.event.model.AppEvent;

/**
 * Interface for application event listeners.
 * <p>
 * Implementing classes should define the logic for handling events of a specific type.
 *
 * @param <T> the type of event this listener handles, must extend {@link AppEvent}
 */
public interface AppEventListener<T extends AppEvent> {
    void onEvent(T event);
}
