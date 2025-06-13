package org.jazzteam.task.priority;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Priority;
import org.jazzteam.service.PriorityService;
import org.jazzteam.task.AbstractTask;
import org.jazzteam.task.listener.TaskListener;

public abstract class AbstractPriorityTask<T> extends AbstractTask<T> {
    protected final Priority entry;
    protected final PriorityService priorityService = ApplicationContext.getPriorityService();

    protected AbstractPriorityTask(Priority entry, TaskListener<T> listener) {
        super(listener);
        this.entry = entry;
    }
}
