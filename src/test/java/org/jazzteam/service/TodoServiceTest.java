package org.jazzteam.service;

import org.jazzteam.model.Status;
import org.jazzteam.model.Todo;
import org.jazzteam.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    private TodoRepository todoRepository;
    private TodoService todoService;

    @Before
    public void setUp() {
        todoRepository = mock(TodoRepository.class);
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void testGetAllTodos() {
        List<Todo> todos = Arrays.asList(
                new Todo("Title1", "Desc1", "High", LocalDate.now(), LocalDate.now().plusDays(5), Status.OPEN),
                new Todo("Title2", "Desc2", "Low", LocalDate.now(), LocalDate.now().plusDays(10), Status.IN_PROGRESS)
        );
        when(todoRepository.getAllTodos()).thenReturn(todos);

        List<Todo> result = todoService.getAllTodos();

        assertNotNull(result);
        assertEquals(todos, result);
        verify(todoRepository).getAllTodos();
    }

    @Test
    public void testAddTodo() {
        Todo todo = new Todo("Title", "Description", "Medium", LocalDate.now(), LocalDate.now().plusDays(7), Status.OPEN);
        todoService.addTodo(todo);
        verify(todoRepository).addTodo(todo);
    }

    @Test
    public void testUpdateTodo() {
        int index = 0;
        Todo todo = new Todo("Updated Title", "Updated Desc", "High", LocalDate.now(), LocalDate.now().plusDays(3), Status.IN_PROGRESS);
        todoService.updateTodo(index, todo);
        verify(todoRepository).updateTodo(index, todo);
    }

    @Test
    public void testUpdateTodoPriorities() {
        String currentName = "Low";
        String newName = "Medium";

        todoService.updateTodoPriorities(currentName, newName);

        verify(todoRepository).updateTodoPriorities(currentName, newName);
    }

    @Test
    public void testDeleteTodo() {
        int index = 2;
        todoService.deleteTodo(index);
        verify(todoRepository).deleteTodo(index);
    }

    @Test
    public void testMoveUp() {
        int index = 3;
        todoService.moveUp(index);
        verify(todoRepository).moveUp(index);
    }

    @Test
    public void testMoveDown() {
        int index = 4;
        todoService.moveDown(index);
        verify(todoRepository).moveDown(index);
    }
}
