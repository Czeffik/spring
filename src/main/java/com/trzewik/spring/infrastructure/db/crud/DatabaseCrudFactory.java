package com.trzewik.spring.infrastructure.db.crud;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseCrudFactory {
    public static DatabaseCrud create(SessionFactory sessionFactory) {
        return new CrudImpl(sessionFactory);
    }
}
