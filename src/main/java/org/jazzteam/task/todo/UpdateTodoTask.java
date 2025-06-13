package org.jazzteam.task.todo;

import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class UpdateTodoTask extends AbstractTodoTask<Void> {

    public UpdateTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.updateTodo(entry);
        return null;
    }
}
