package org.jazzteam.task.todo;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.model.todo.TodoMovedUpEvent;
import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class MoveUpTodoTask extends AbstractTodoTask<Void> {

    public MoveUpTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.moveUp(entry.getId());
        ApplicationContext.getEventBroker().publish(new TodoMovedUpEvent(entry.getId()));
        return null;
    }
}
