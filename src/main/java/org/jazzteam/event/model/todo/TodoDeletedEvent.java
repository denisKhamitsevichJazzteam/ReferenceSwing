package org.jazzteam.event.model.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jazzteam.event.model.AppEvent;
import org.jazzteam.event.model.EventType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoDeletedEvent implements AppEvent {
    private Long todoId;

    @Override
    public EventType getType() {
        return EventType.TODO_DELETED;
    }

}
