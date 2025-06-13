package org.jazzteam.task.priority;

import org.jazzteam.model.Priority;
import org.jazzteam.task.listener.TaskListener;

import java.util.List;

public class GetAllPrioritiesTask extends AbstractPriorityTask<List<Priority>> {

    public GetAllPrioritiesTask(Priority entry, TaskListener<List<Priority>> listener) {
        super(entry, listener);
    }

    @Override
    protected List<Priority> execute() {
        return priorityService.getAllPriorities();
    }
}
