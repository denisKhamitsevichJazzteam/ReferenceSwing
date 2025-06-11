package org.jazzteam.service;

import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Priority;
import org.jazzteam.repository.PriorityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class PriorityServiceTest {

    private PriorityRepository priorityRepository;
    private TodoService mockTodoService;

    private MockedStatic<ApplicationContext> mockedContext;

    private PriorityService priorityService;

    @Before
    public void setUp() {
        priorityRepository = mock(PriorityRepository.class);
        mockTodoService = mock(TodoService.class);
        mockedContext = Mockito.mockStatic(ApplicationContext.class);
        mockedContext.when(ApplicationContext::getTodoService).thenReturn(mockTodoService);
        priorityService = new PriorityService(priorityRepository);
    }

    @Test
    public void testGetAllPriorities() {
        List<Priority> priorities = Arrays.asList(
                new Priority("High", 1),
                new Priority("Low", 3)
        );
        when(priorityRepository.getAllPriorities()).thenReturn(priorities);

        List<Priority> result = priorityService.getAllPriorities();

        assertNotNull(result);
        assertEquals(priorities, result);
        verify(priorityRepository).getAllPriorities();
    }

    @Test
    public void testAddPriority() {
        Priority p = new Priority("Medium", 2);
        priorityService.addPriority(p);
        verify(priorityRepository).addPriority(p);
    }

    @Test
    public void testUpdatePriority() {
        String currentName = "High";
        Priority newPriority = new Priority("Urgent", 1);

        priorityService.updatePriority(currentName, newPriority);

        verify(priorityRepository).updatePriority(currentName, newPriority);
        verify(mockTodoService).updateTodoPriorities(currentName, newPriority.getName());
    }

    @Test
    public void testDeletePriority() {
        String name = "Low";

        priorityService.deletePriority(name);

        verify(priorityRepository).deletePriority(name);
        verify(mockTodoService).updateTodoPriorities(name, null);
    }

    @Test
    public void testGetPriorityNames() {
        List<String> names = Arrays.asList("High", "Medium", "Low");
        when(priorityRepository.getPriorityNames()).thenReturn(names);

        List<String> result = priorityService.getPriorityNames();

        assertNotNull(result);
        assertEquals(names, result);
        verify(priorityRepository).getPriorityNames();
    }

    @org.junit.After
    public void tearDown() {
        mockedContext.close();
    }
}