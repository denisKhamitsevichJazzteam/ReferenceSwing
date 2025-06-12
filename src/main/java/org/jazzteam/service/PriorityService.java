package org.jazzteam.service;


import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Priority;
import org.jazzteam.repository.PriorityDAO;

import java.util.List;

public class PriorityService {

    private final PriorityDAO priorityDAO;

    public PriorityService(PriorityDAO priorityDAO) {
        this.priorityDAO = priorityDAO;
    }

    public List<Priority> getAllPriorities() {
        return priorityDAO.findAll();
    }

    public void savePriority(Priority priority) {
        priorityDAO.save(priority);
    }

    public void updatePriority(Priority newPriority) {
        priorityDAO.update(newPriority);
    }

    public void deletePriority(Priority priority) {
        ApplicationContext.getTodoService().clearPriorityFromTodos(priority);
        priorityDAO.delete(priority);
    }
}

