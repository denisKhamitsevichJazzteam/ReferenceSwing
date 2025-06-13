package org.jazzteam.task.priority;

import org.jazzteam.model.Priority;
import org.jazzteam.task.listener.TaskListener;

public class UpdatePriorityTask extends AbstractPriorityTask<Void> {

    public UpdatePriorityTask(Priority entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        priorityService.updatePriority(entry);
        return null;
    }
}
