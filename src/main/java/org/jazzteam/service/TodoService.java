package org.jazzteam.service;

import org.jazzteam.model.Priority;
import org.jazzteam.model.Todo;
import org.jazzteam.repository.TodoDAO;

import java.util.List;

public class TodoService {
    private final TodoDAO todoDAO;

    public TodoService(TodoDAO todoDAO) {
        this.todoDAO = todoDAO;
    }

    public List<Todo> getAllTodos() {
        return todoDAO.findAll();
    }

    public void saveTodo(Todo todo) {
        todoDAO.save(todo);
    }

    public void updateTodo(Todo todo) {
        todoDAO.update(todo);
    }

    public void clearPriorityFromTodos(Priority priority) {
        todoDAO.clearPriorityFromTodos(priority);
    }

    public void deleteTodo(Todo todo) {
        todoDAO.delete(todo);
    }

    public void moveUp(Todo todo) {
        todoDAO.moveUp(todo);
    }

    public void moveDown(Todo todo) {
        todoDAO.moveDown(todo);
    }
}

