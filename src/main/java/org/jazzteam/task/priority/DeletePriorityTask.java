package org.jazzteam.task.priority;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.model.priority.PriorityDeletedEvent;
import org.jazzteam.model.Priority;
import org.jazzteam.task.listener.TaskListener;

public class DeletePriorityTask extends AbstractPriorityTask<Void> {

    public DeletePriorityTask(Priority entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        priorityService.deletePriority(entry);
        ApplicationContext.getEventBroker().publish(new PriorityDeletedEvent(entry.getId()));
        return null;
    }
}
