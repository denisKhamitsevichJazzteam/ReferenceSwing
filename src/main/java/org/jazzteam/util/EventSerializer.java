package org.jazzteam.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jazzteam.event.model.AppEvent;
import org.jazzteam.event.model.EventType;
import org.jazzteam.event.model.priority.PriorityDeletedEvent;
import org.jazzteam.event.model.priority.PrioritySavedEvent;
import org.jazzteam.event.model.priority.PriorityUpdatedEvent;
import org.jazzteam.event.model.todo.*;

public class EventSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static String serialize(AppEvent event) throws Exception {
        return objectMapper.writeValueAsString(event);
    }

    public static AppEvent deserialize(String json, EventType type) throws Exception {
        switch (type) {
            case TODO_UPDATED:
                return objectMapper.readValue(json, TodoUpdatedEvent.class);
            case TODO_SAVED:
                return objectMapper.readValue(json, TodoSavedEvent.class);
            case TODO_MOVED_UP:
                return objectMapper.readValue(json, TodoMovedUpEvent.class);
            case TODO_MOVED_DOWN:
                return objectMapper.readValue(json, TodoMovedDownEvent.class);
            case TODO_DELETED:
                return objectMapper.readValue(json, TodoDeletedEvent.class);
            case PRIORITY_SAVED:
                return objectMapper.readValue(json, PrioritySavedEvent.class);
            case PRIORITY_UPDATED:
                return objectMapper.readValue(json, PriorityUpdatedEvent.class);
            case PRIORITY_DELETED:
                return objectMapper.readValue(json, PriorityDeletedEvent.class);
            default:
                throw new IllegalArgumentException("Unknown event type: " + type);
        }
    }

}
