package org.jazzteam.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jazzteam.util.HibernateUtil;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractDAO<T> {

    private final Class<T> clazz;

    protected AbstractDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected <R> R execute(Function<Session, R> command) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            R result = command.apply(session);
            transaction.commit();
            return result;
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Cannot execute command: ", e);
        }
    }

    public void refresh(T entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.refresh(entity);
        }
    }

    public void save(T entity) {
        execute(session -> {
            session.save(entity);
            return null;
        });
    }

    public void update(T entity) {
        execute(session -> {
            session.update(entity);
            return null;
        });
    }

    public void delete(T entity) {
        execute(session -> {
            session.delete(entity);
            return null;
        });
    }

    public List<T> findAll() {
        return execute(session -> session.createQuery("from " + clazz.getName(), clazz).list());
    }
}
