package com.gimmesomepeace.recipes.testutil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Component
public class TestDatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void clean() {
        entityManager.flush();

        Session session = entityManager.unwrap(Session.class);

        session.doWork(connection -> {

            List<String> tables = new ArrayList<>();

            try (Statement statement = connection.createStatement()) {

                ResultSet rs = statement.executeQuery(
                        "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'"
                );

                while (rs.next()) {
                    String table = rs.getString(1);

                    if (!table.equalsIgnoreCase("flyway_schema_history")) {
                        tables.add(table);
                    }
                }

                statement.execute("SET REFERENTIAL_INTEGRITY FALSE");

                for (String table : tables) {
                    statement.executeUpdate("TRUNCATE TABLE " + table);
                }

                statement.execute("SET REFERENTIAL_INTEGRITY TRUE");
            }
        });
    }
}
