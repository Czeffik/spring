package com.trzewik.spring.infrastructure.db.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(Serializable id);

    List<T> getAll();

    void save(T toSave);

    void update(T updated);

    void delete(T toDelete);
}
