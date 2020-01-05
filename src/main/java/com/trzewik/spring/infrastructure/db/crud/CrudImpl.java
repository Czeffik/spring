package com.trzewik.spring.infrastructure.db.crud;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

class CrudImpl<T> implements DatabaseCrud<T> {
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    CrudImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public T get(Serializable id, Class<T> tclass) {
        openSession();
        T entity = session.get(tclass, id);
        closeSession();
        return entity;
    }

    @Override
    public List<T> getAll(String query, Class<T> tclass) {
        openSession();
        List<T> list = session.createQuery(query, tclass).getResultList();
        closeSession();
        return list;
    }

    @Override
    public void save(T toSave) {
        openSessionWithTransaction();
        session.save(toSave);
        closeSessionWithTransaction();
    }

    @Override
    public void update(T updated) {
        openSessionWithTransaction();
        session.update(updated);
        closeSessionWithTransaction();
    }

    @Override
    public void delete(T toDelete) {
        openSessionWithTransaction();
        session.delete(toDelete);
        closeSessionWithTransaction();
    }

    private void openSession() {
        session = sessionFactory.openSession();
    }

    private void openSessionWithTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    private void closeSession() {
        session.close();
    }

    private void closeSessionWithTransaction() {
        transaction.commit();
        session.close();
    }
}
