package org.jazzteam.task.priority;

import org.jazzteam.model.Priority;
import org.jazzteam.task.listener.TaskListener;

public class DeletePriorityTask extends AbstractPriorityTask<Void> {

    public DeletePriorityTask(Priority entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        priorityService.deletePriority(entry);
        return null;
    }
}
