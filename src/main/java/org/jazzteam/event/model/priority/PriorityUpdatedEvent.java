package org.jazzteam.event.model.priority;

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
public class PriorityUpdatedEvent implements AppEvent {
    private Long priorityId;

    @Override
    public EventType getType() {
        return EventType.PRIORITY_UPDATED;
    }

}
