package org.jazzteam.task.todo;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.model.todo.TodoDeletedEvent;
import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class DeleteTodoTask extends AbstractTodoTask<Void> {

    public DeleteTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.deleteTodo(entry);
        ApplicationContext.getEventBroker().publish(new TodoDeletedEvent(entry.getId()));
        return null;
    }
}
