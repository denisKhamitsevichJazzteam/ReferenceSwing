package org.jazzteam.task.priority;

import org.jazzteam.model.Priority;
import org.jazzteam.task.listener.TaskListener;

public class SavePriorityTask extends AbstractPriorityTask<Void> {

    public SavePriorityTask(Priority entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        priorityService.savePriority(entry);
        return null;
    }
}
