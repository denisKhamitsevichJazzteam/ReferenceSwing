package org.jazzteam.repository;

import org.jazzteam.model.Priority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PriorityRepository {
    public static final String PRIORITY_ALREADY_EXISTS_MESSAGE = "Priority already exists";
    public static final String PRIORITY_NOT_FOUND = "Priority not found";
    private final List<Priority> priorities = new ArrayList<>();

    {
        priorities.add(new Priority("critical", 2));
        priorities.add(new Priority("minor", 1));
    }

    public List<Priority> getAllPriorities() {
        return priorities;
    }

    public void addPriority(Priority priority) {
        if (isAlreadyExistsByName(priority.getName()) || isAlreadyExistsByWeight(priority.getWeight())) {
            throw new IllegalStateException(PRIORITY_ALREADY_EXISTS_MESSAGE);
        }

        priorities.add(priority);
    }

    public void updatePriority(String currentName, Priority newPriority) {
        Priority priority = priorities.stream().filter(p -> p.getName().equals(currentName))
                .findFirst().orElseThrow(() -> new IllegalStateException(PRIORITY_NOT_FOUND));

        boolean isNameChanged = !priority.getName().equals(newPriority.getName());
        boolean isWeightChanged = priority.getWeight() != newPriority.getWeight();

        if ((isNameChanged && isAlreadyExistsByName(newPriority.getName()))
                || (isWeightChanged && isAlreadyExistsByWeight(newPriority.getWeight()))) {
            throw new IllegalStateException(PRIORITY_ALREADY_EXISTS_MESSAGE);
        }

        priority.setName(newPriority.getName());
        priority.setWeight(newPriority.getWeight());
    }

    public void deletePriority(String name) {
        for (Iterator<Priority> iterator = priorities.iterator(); iterator.hasNext(); ) {
            Priority obj = iterator.next();
            if (obj.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }
    }

    public List<String> getPriorityNames() {
        return priorities.stream().map(Priority::getName).collect(Collectors.toList());
    }

    private boolean isAlreadyExistsByName(String name) {
        return priorities.stream().anyMatch(priority -> priority.getName().equals(name));
    }

    private boolean isAlreadyExistsByWeight(int weight) {
        return priorities.stream().anyMatch(priority -> priority.getWeight() == weight);
    }

}
