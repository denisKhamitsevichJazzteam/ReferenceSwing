package org.jazzteam.task.todo;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.model.todo.TodoMovedDownEvent;
import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class MoveDownTodoTask extends AbstractTodoTask<Void> {

    public MoveDownTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.moveDown(entry.getId());
        ApplicationContext.getEventBroker().publish(new TodoMovedDownEvent(entry.getId()));
        return null;
    }
}
