package org.jazzteam.repository;

import org.jazzteam.model.Todo;

import java.util.ArrayList;
import java.util.List;


public class TodoRepository {
    private final List<Todo> todos = new ArrayList<>();

    {
        todos.add(new Todo("Test Task", "Description", null, java.time.LocalDate.now(), null, null));
        todos.add(new Todo("Another Task", "Another Description", null, java.time.LocalDate.now(), null, null));
    }

    public List<Todo> getAllTodos() {
        return todos;
    }

    public void addTodo(Todo todo) {
        todos.add(todo);
    }

    public void updateTodo(int index, Todo todo) {
        todos.set(index, todo);
    }

    public void updateTodoPriorities(String currentName, String newName) {
        todos.stream().filter(todo -> todo.getPriorityName() != null && todo.getPriorityName().equals(currentName))
                .forEach(todo -> todo.setPriorityName(newName));
    }

    public void deleteTodo(int index) {
        todos.remove(index);
    }

    public void moveUp(int index) {
        if (index > 0) {
            Todo t = todos.remove(index);
            todos.add(index - 1, t);
        }
    }

    public void moveDown(int index) {
        if (index < todos.size() - 1) {
            Todo t = todos.remove(index);
            todos.add(index + 1, t);
        }
    }
}
