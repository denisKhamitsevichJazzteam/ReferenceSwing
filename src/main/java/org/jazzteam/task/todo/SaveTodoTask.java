package org.jazzteam.task.todo;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.event.model.todo.TodoSavedEvent;
import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

public class SaveTodoTask extends AbstractTodoTask<Void> {

    public SaveTodoTask(Todo entry, TaskListener<Void> listener) {
        super(entry, listener);
    }

    @Override
    protected Void execute() {
        todoService.saveTodo(entry);
        ApplicationContext.getEventBroker().publish(new TodoSavedEvent(entry));
        return null;
    }
}
