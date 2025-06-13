package org.jazzteam.task.todo;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Todo;
import org.jazzteam.service.TodoService;
import org.jazzteam.task.AbstractTask;
import org.jazzteam.task.listener.TaskListener;

public abstract class AbstractTodoTask<T> extends AbstractTask<T> {
    protected final Todo entry;
    protected final TodoService todoService = ApplicationContext.getTodoService();

    protected AbstractTodoTask(Todo entry, TaskListener<T> listener) {
        super(listener);
        this.entry = entry;
    }
}
