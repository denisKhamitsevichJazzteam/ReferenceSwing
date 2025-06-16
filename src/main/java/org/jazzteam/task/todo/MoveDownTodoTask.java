package org.jazzteam.task.todo;

import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class MoveDownTodoTask extends AbstractTodoTask<Void> {

    public MoveDownTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.moveDown(entry);
        return null;
    }
}
