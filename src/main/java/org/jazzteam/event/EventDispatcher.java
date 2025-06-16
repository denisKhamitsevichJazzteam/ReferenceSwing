package org.jazzteam.event;

import org.jazzteam.event.listener.AppEventListener;
import org.jazzteam.event.listener.ListenerRegistration;
import org.jazzteam.event.model.AppEvent;
import org.jazzteam.event.model.EventType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Dispatcher for application events that manages registration of listeners and
 * dispatches events to all registered listeners of a specific event type.
 * <p>
 * This class supports multiple listeners per event type and ensures that all
 * listeners are notified when an event of that type is dispatched.
 * <p>
 * Listeners should implement {@link AppEventListener} and handle events of type
 * {@link AppEvent} or its subclasses.
 */
public class EventDispatcher {

    /**
     * Map storing event listeners registered for each event type.
     * Key: {@link EventType} — the type of the event.
     * Value: List of listeners interested in that event type.
     */
    private final Map<EventType, List<AppEventListener<? extends AppEvent>>> listeners = new EnumMap<>(EventType.class);

    public <T extends AppEvent> void register(EventType type, AppEventListener<T> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public void unregisterAll(List<ListenerRegistration<?>> registrations) {
        registrations.forEach(reg -> {
            List<AppEventListener<? extends AppEvent>> listenersList = listeners.get(reg.getEventType());
            if (listenersList != null) {
                listenersList.remove(reg.getListener());
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void dispatch(AppEvent event) {
        List<AppEventListener<? extends AppEvent>> eventListeners = listeners.get(event.getType());
        if (eventListeners != null) {
            for (AppEventListener listener : eventListeners) {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Exception occurred while dispatching event of type " + event.getType() +
                                    " to listener: " + listener.getClass().getName(), e);
                }
            }
        }
    }
}
