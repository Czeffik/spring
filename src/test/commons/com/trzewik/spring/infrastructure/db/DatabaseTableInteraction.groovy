package com.trzewik.spring.infrastructure.db

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.util.logging.Slf4j

@Slf4j
trait DatabaseTableInteraction {
    abstract Sql getSql()

    abstract String getDefaultSchema()

    List<GroovyRowResult> getAll(String tableName) {
        String query = "SELECT * FROM $tableName"
        log.info(query)
        return sql.rows(query)
    }

    void deleteAll(String tableName) {
        String query = "DELETE FROM $tableName"
        log.info(query)
        sql.execute(query)
    }

    String table(String name) {
        return "$defaultSchema.$name".toString()
    }
}
