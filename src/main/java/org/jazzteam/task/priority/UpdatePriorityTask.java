package org.jazzteam.task.priority;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.model.priority.PriorityUpdatedEvent;
import org.jazzteam.model.Priority;
import org.jazzteam.task.listener.TaskListener;

public class UpdatePriorityTask extends AbstractPriorityTask<Void> {

    public UpdatePriorityTask(Priority entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        priorityService.updatePriority(entry);
        ApplicationContext.getEventBroker().publish(new PriorityUpdatedEvent(entry.getId()));
        return null;
    }
}
