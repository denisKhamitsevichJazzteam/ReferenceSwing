package org.jazzteam.service;


import org.jazzteam.core.ApplicationContext;
import org.jazzteam.model.Priority;
import org.jazzteam.repository.PriorityRepository;

import java.util.List;

public class PriorityService {

    private final PriorityRepository priorityRepository;

    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public List<Priority> getAllPriorities() {
        return priorityRepository.getAllPriorities();
    }

    public void addPriority(Priority p) {
        priorityRepository.addPriority(p);
    }

    public void updatePriority(String currentName, Priority newPriority) {
        priorityRepository.updatePriority(currentName, newPriority);
        ApplicationContext.getTodoService().updateTodoPriorities(currentName, newPriority.getName());
    }

    public void deletePriority(String name) {
        priorityRepository.deletePriority(name);
        ApplicationContext.getTodoService().updateTodoPriorities(name, null);
    }

    public List<String> getPriorityNames() {
        return priorityRepository.getPriorityNames();
    }
}

