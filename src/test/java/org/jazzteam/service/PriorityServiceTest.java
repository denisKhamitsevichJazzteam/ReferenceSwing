package org.jazzteam.service;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Priority;
import org.jazzteam.repository.PriorityDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class PriorityServiceTest {

    private PriorityDAO priorityDAO;
    private TodoService mockTodoService;
    private PriorityService priorityService;
    private MockedStatic<ApplicationContext> mockedContext;

    @Before
    public void setUp() {
        priorityDAO = mock(PriorityDAO.class);
        mockTodoService = mock(TodoService.class);
        mockedContext = mockStatic(ApplicationContext.class);
        mockedContext.when(ApplicationContext::getTodoService).thenReturn(mockTodoService);

        priorityService = new PriorityService(priorityDAO);
    }

    @After
    public void tearDown() {
        mockedContext.close();
    }

    @Test
    public void testGetAllPriorities() {
        List<Priority> priorities = Arrays.asList(
                new Priority("High", 1),
                new Priority("Low", 3)
        );
        when(priorityDAO.findAll()).thenReturn(priorities);

        List<Priority> result = priorityService.getAllPriorities();

        assertNotNull(result);
        assertEquals(priorities, result);
        verify(priorityDAO).findAll();
    }

    @Test
    public void testSavePriority() {
        Priority p = new Priority("Medium", 2);
        priorityService.savePriority(p);
        verify(priorityDAO).save(p);
    }

    @Test
    public void testUpdatePriority() {
        Priority updatedPriority = new Priority("Urgent", 1);

        priorityService.updatePriority(updatedPriority);

        verify(priorityDAO).update(updatedPriority);
    }

    @Test
    public void testDeletePriority() {
        Priority toDelete = new Priority("Low", 3);

        priorityService.deletePriority(toDelete);

        verify(mockTodoService).clearPriorityFromTodos(toDelete);
        verify(priorityDAO).delete(toDelete);
    }
}
