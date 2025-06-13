package org.jazzteam.task.todo;

import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class MoveUpTodoTask extends AbstractTodoTask<Void> {

    public MoveUpTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.moveUp(entry.getId());
        return null;
    }
}
