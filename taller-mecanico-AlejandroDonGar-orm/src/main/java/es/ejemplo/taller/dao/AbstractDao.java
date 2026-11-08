package es.ejemplo.taller.dao;
import es.ejemplo.taller.orm.config.OrmFactory;
import es.ejemplo.taller.orm.entity.ClienteEntity;
import es.ejemplo.taller.orm.mapper.ClienteMapper;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

/**
 * @author AlejandroDonGar
 * @param <T> Objeto abstracto
 * @param <E> Entidad abstracta
 * @param <ID> Identificador abstracto
 * Clase AbstractDao
 */
public class AbstractDao <T,E,ID> {
    private final EntityManagerFactory managerFactory;

    public AbstractDao(String url) {
        managerFactory = OrmFactory.crear(url);
    }

    protected List<T> findAll(String sql) {
        /*
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery(sql, E)
                    .getResultStream()
                    .map(ClienteMapper::toDomain)
                    .toList();
        }
        */
        return List.of();
    }
}
