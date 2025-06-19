package org.jazzteam.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jazzteam.exception.EntityNotFoundException;
import org.jazzteam.exception.InvalidReferenceException;
import org.jazzteam.exception.StaleDataException;
import org.jazzteam.model.Priority;
import org.jazzteam.model.Todo;
import org.jazzteam.util.HibernateUtil;

import java.util.List;


public class TodoDAO extends AbstractDAO<Todo> {

    public static final String TODO_NOT_FOUND = "Todo not found: it may have been deleted";
    public static final String TODO_CURRENT_ORDER_IS_OUTDATED = "Todo current order is outdated!";

    public TodoDAO() {
        super(Todo.class);
    }

    @Override
    public List<Todo> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Todo ORDER BY sortOrder ASC", Todo.class).list();
        }
    }

    @Override
    public void save(Todo todo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Integer maxOrder = session.createQuery(
                            "SELECT COALESCE(MAX(t.sortOrder), 0) FROM Todo t", Integer.class)
                    .uniqueResult();

            todo.setSortOrder(maxOrder + 1);

            session.save(todo);
            tx.commit();
        }
    }

    @Override
    public void update(Todo todo) {
        execute(session -> {
            Todo existing = session.get(Todo.class, todo.getId());
            if (existing == null) {
                throw new EntityNotFoundException(TODO_NOT_FOUND);
            }

            if (todo.getPriority() != null) {
                Priority priority = session.get(Priority.class, todo.getPriority().getId());
                if (priority == null) {
                    throw new InvalidReferenceException("Priority not found for a given Todo");
                }
            }

            session.merge(todo);
            return null;
        });
    }

    @Override
    public void delete(Todo todo) {
        execute(session -> {
            Todo existing = session.get(Todo.class, todo.getId());
            if (existing == null) {
                return null;
            }

            session.delete(existing);
            return null;
        });
    }

    public void moveUp(Todo todo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Todo existing = session.get(Todo.class, todo.getId());
            if (existing == null) {
                tx.commit();
                throw new EntityNotFoundException(TODO_NOT_FOUND);
            }

            int localOrder = todo.getSortOrder();
            int actualOrder = existing.getSortOrder();

            Todo previous = session.createQuery(
                            "FROM Todo WHERE sortOrder < :currentOrder ORDER BY sortOrder DESC",
                            Todo.class)
                    .setParameter("currentOrder", localOrder)
                    .setMaxResults(1)
                    .uniqueResult();

            if (localOrder != actualOrder && (previous == null || !previous.getId().equals(todo.getId()))) {
                tx.commit();
                throw new StaleDataException(TODO_CURRENT_ORDER_IS_OUTDATED);
            }

            if (previous != null) {
                int prevOrder = previous.getSortOrder();
                existing.setSortOrder(prevOrder);
                previous.setSortOrder(actualOrder);

                session.update(existing);
                session.update(previous);
            }

            tx.commit();
        }
    }


    public void moveDown(Todo todo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Todo existing = session.get(Todo.class, todo.getId());
            if (existing == null) {
                tx.commit();
                throw new EntityNotFoundException(TODO_NOT_FOUND);
            }

            int localOrder = todo.getSortOrder();
            int actualOrder = existing.getSortOrder();

            Todo next = session.createQuery(
                            "FROM Todo WHERE sortOrder > :currentOrder ORDER BY sortOrder ASC",
                            Todo.class)
                    .setParameter("currentOrder", localOrder)
                    .setMaxResults(1)
                    .uniqueResult();

            if (localOrder != actualOrder && (next == null || !next.getId().equals(todo.getId()))) {
                tx.commit();
                throw new StaleDataException(TODO_CURRENT_ORDER_IS_OUTDATED);
            }


            if (next != null) {
                int nextOrder = next.getSortOrder();
                existing.setSortOrder(nextOrder);
                next.setSortOrder(actualOrder);

                session.update(existing);
                session.update(next);
            }

            tx.commit();
        }
    }


    public void clearPriorityFromTodos(Priority priority) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            session.createQuery("UPDATE Todo t SET t.priority = null WHERE t.priority = :priority")
                    .setParameter("priority", priority)
                    .executeUpdate();

            tx.commit();
        }
    }


}
