package org.jazzteam.task.todo;

import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class DeleteTodoTask extends AbstractTodoTask<Void> {

    public DeleteTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.deleteTodo(entry);
        return null;
    }
}
