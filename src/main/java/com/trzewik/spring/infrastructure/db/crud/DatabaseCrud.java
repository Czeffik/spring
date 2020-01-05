package com.trzewik.spring.infrastructure.db.crud;

import java.io.Serializable;
import java.util.List;

public interface DatabaseCrud<T> {
    T get(Serializable id, Class<T> tclass);

    List<T> getAll(String query, Class<T> tclass);

    void save(T toSave);

    void update(T updated);

    void delete(T toDelete);
}
