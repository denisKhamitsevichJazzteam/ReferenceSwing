package org.jazzteam.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jazzteam.model.Priority;
import org.jazzteam.model.Todo;
import org.jazzteam.util.HibernateUtil;

import java.util.List;


public class TodoDAO extends AbstractDAO<Todo> {
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

    public void moveUp(Long todoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Todo current = session.get(Todo.class, todoId);
            if (current == null) {
                tx.commit();
                return;
            }

            int currentOrder = current.getSortOrder();
            if (currentOrder <= 1) {
                tx.commit();
                return;
            }

            Todo previous = session.createQuery(
                            "FROM Todo WHERE sortOrder = :prevOrder", Todo.class)
                    .setParameter("prevOrder", currentOrder - 1)
                    .uniqueResult();

            if (previous != null) {
                current.setSortOrder(currentOrder - 1);
                previous.setSortOrder(currentOrder);

                session.update(current);
                session.update(previous);
            }

            tx.commit();
        }
    }

    public void moveDown(Long todoId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Todo current = session.get(Todo.class, todoId);
            if (current == null) {
                tx.commit();
                return;
            }

            int currentOrder = current.getSortOrder();

            Integer maxOrder = session.createQuery("SELECT max(sortOrder) FROM Todo", Integer.class)
                    .uniqueResult();

            if (maxOrder == null || currentOrder >= maxOrder) {
                tx.commit();
                return;
            }

            Todo next = session.createQuery(
                            "FROM Todo WHERE sortOrder = :nextOrder", Todo.class)
                    .setParameter("nextOrder", currentOrder + 1)
                    .uniqueResult();

            if (next != null) {
                current.setSortOrder(currentOrder + 1);
                next.setSortOrder(currentOrder);

                session.update(current);
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
