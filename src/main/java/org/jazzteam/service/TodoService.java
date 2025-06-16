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

    public void refreshTodo(Todo todo) {
        todoDAO.refresh(todo);
    }

    public void clearPriorityFromTodos(Priority priority) {
        todoDAO.clearPriorityFromTodos(priority);
    }

    public void deleteTodo(Todo todo) {
        todoDAO.delete(todo);
    }

    public void moveUp(Long id) {
        todoDAO.moveUp(id);
    }

    public void moveDown(Long id) {
        todoDAO.moveDown(id);
    }
}

