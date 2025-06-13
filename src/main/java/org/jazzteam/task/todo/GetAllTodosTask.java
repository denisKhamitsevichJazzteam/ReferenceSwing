package org.jazzteam.task.todo;

import org.jazzteam.model.Todo;
import org.jazzteam.task.listener.TaskListener;

import java.util.List;

public class GetAllTodosTask extends AbstractTodoTask<List<Todo>> {

    public GetAllTodosTask(Todo entry, TaskListener<List<Todo>> listener) {
        super(entry, listener);
    }

    @Override
    protected List<Todo> execute() {
        return todoService.getAllTodos();
    }
}
