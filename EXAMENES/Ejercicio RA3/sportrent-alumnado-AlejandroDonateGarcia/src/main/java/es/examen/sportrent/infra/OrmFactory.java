package es.examen.sportrent.infra;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;

public class OrmFactory {
    private OrmFactory() {}

    public static EntityManagerFactory crear(String jdbcUrl) {
        return Persistence.createEntityManagerFactory("sportrent",
                Map.of("jakarta.persistence.jdbc.url",jdbcUrl,
                        "hibernate.connection.foreign_keys", "true"));
    }
}
