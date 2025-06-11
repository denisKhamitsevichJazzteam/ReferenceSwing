package org.jazzteam.service;

import org.jazzteam.model.Todo;
import org.jazzteam.repository.TodoRepository;

import java.util.List;

public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.getAllTodos();
    }

    public void addTodo(Todo todo) {
        todoRepository.addTodo(todo);
    }

    public void updateTodo(int index, Todo todo) {
        todoRepository.updateTodo(index, todo);
    }

    public void updateTodoPriorities(String currentName, String newName) {
        todoRepository.updateTodoPriorities(currentName, newName);
    }

    public void deleteTodo(int index) {
        todoRepository.deleteTodo(index);
    }

    public void moveUp(int index) {
        todoRepository.moveUp(index);
    }

    public void moveDown(int index) {
        todoRepository.moveDown(index);
    }
}

