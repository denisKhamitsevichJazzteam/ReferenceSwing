package org.jazzteam.repository;

import org.jazzteam.exception.EntityNotFoundException;
import org.jazzteam.model.Priority;


public class PriorityDAO extends AbstractDAO<Priority> {

    public static final String PRIORITY_NOT_FOUND = "Priority not found: it may have been deleted";

    public PriorityDAO() {
        super(Priority.class);
    }

    @Override
    public void update(Priority priority) {
        execute(session -> {
            Priority existing = session.get(Priority.class, priority.getId());
            if (existing == null) {
                throw new EntityNotFoundException(PRIORITY_NOT_FOUND);
            }

            session.merge(priority);
            return null;
        });
    }

    @Override
    public void delete(Priority priority) {
        execute(session -> {
            Priority existing = session.get(Priority.class, priority.getId());
            if (existing == null) {
                return null;
            }

            session.delete(existing);
            return null;
        });
    }
}
