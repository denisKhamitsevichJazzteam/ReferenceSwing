package org.jazzteam.service;

import org.jazzteam.model.Priority;
import org.jazzteam.model.Status;
import org.jazzteam.model.Todo;
import org.jazzteam.repository.TodoDAO;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    private TodoDAO todoDAO;
    private TodoService todoService;

    @Before
    public void setUp() {
        todoDAO = mock(TodoDAO.class);
        todoService = new TodoService(todoDAO);
    }

    @Test
    public void testGetAllTodos() {
        List<Todo> todos = Arrays.asList(
                new Todo(1L, "Title1", "Desc1", new Priority("High", 1),
                        LocalDate.now(), LocalDate.now().plusDays(5), Status.OPEN, 1),
                new Todo(2L, "Title2", "Desc2", new Priority("Low", 3),
                        LocalDate.now(), LocalDate.now().plusDays(10), Status.IN_PROGRESS, 2)
        );
        when(todoDAO.findAll()).thenReturn(todos);

        List<Todo> result = todoService.getAllTodos();

        assertNotNull(result);
        assertEquals(todos, result);
        verify(todoDAO).findAll();
    }

    @Test
    public void testSaveTodo() {
        Todo todo = new Todo(1L, "Title", "Description", new Priority("Medium", 2),
                LocalDate.now(), LocalDate.now().plusDays(7), Status.OPEN, 1);
        todoService.saveTodo(todo);
        verify(todoDAO).save(todo);
    }

    @Test
    public void testUpdateTodo() {
        Todo todo = new Todo(2L, "Updated Title", "Updated Desc", new Priority("High", 1),
                LocalDate.now(), LocalDate.now().plusDays(3), Status.IN_PROGRESS, 1);
        todoService.updateTodo(todo);
        verify(todoDAO).update(todo);
    }

    @Test
    public void testClearPriorityFromTodos() {
        Priority priority = new Priority("Low", 3);
        todoService.clearPriorityFromTodos(priority);
        verify(todoDAO).clearPriorityFromTodos(priority);
    }

    @Test
    public void testDeleteTodo() {
        Todo todo = new Todo();
        todo.setId(2L);
        todoService.deleteTodo(todo);
        verify(todoDAO).delete(todo);
    }

    @Test
    public void testMoveUp() {
        Todo todo = new Todo();
        todo.setId(3L);
        todoService.moveUp(todo);
        verify(todoDAO).moveUp(todo);
    }

    @Test
    public void testMoveDown() {
        Todo todo = new Todo();
        todo.setId(4L);
        todoService.moveDown(todo);
        verify(todoDAO).moveDown(todo);
    }
}
