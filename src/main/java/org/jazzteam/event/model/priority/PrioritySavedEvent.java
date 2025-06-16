package org.jazzteam.event.model.priority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jazzteam.event.model.AppEvent;
import org.jazzteam.event.model.EventType;
import org.jazzteam.model.Priority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrioritySavedEvent implements AppEvent {
    private Priority priority;

    @Override
    public EventType getType() {
        return EventType.PRIORITY_SAVED;
    }

}
