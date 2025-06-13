package org.jazzteam.task.todo;

import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class SaveTodoTask extends AbstractTodoTask<Void> {

    public SaveTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.saveTodo(entry);
        return null;
    }
}
