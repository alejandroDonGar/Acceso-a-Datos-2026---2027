package es.ejemplo.taller.orm.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Map;

/**
 * Crea la EntityManagerFactory de la unidad tallerPU con la URL que se le pase (base de trabajo o temporal de pruebas)
 * @author AlejandroDonGar
 */
public final class OrmFactory {
    private OrmFactory() {}

    public static EntityManagerFactory crear(String jdbcUrl) {
        return Persistence.createEntityManagerFactory("tallerPU", Map.of("jakarta.persistence.jdbc.url", jdbcUrl)
        );
    }
}