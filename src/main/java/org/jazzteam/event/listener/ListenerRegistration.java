package org.jazzteam.event.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jazzteam.event.model.AppEvent;
import org.jazzteam.event.model.EventType;

@AllArgsConstructor
@Getter
public class ListenerRegistration<T extends AppEvent> {
    private final EventType eventType;
    private final AppEventListener<T> listener;
}