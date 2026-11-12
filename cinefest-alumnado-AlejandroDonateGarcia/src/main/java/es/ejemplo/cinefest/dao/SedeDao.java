package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sede;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.SedeEntity;
import es.ejemplo.cinefest.orm.mapper.SedeMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AlejandroDonGar
 * Acceso a la tabla sedes con JPA (EntityManager y JPQL).
 * Los errores se registran en el log y no se lanzan: los metodos
 * devuelven 0, Optional.empty() o una lista vacia.
 */
public class SedeDao implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(SedeDao.class.getName());
    private final EntityManagerFactory managerFactory;

    /**
     * Crea el DAO y su EntityManagerFactory.
     *
     * @param jdbcUrl URL de la base de datos SQLite
     */
    public SedeDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    private void registrar(String metodo, Exception ex) {
        LOG.log(Level.WARNING, metodo + " no se pudo completar: " + ex.getMessage());
    }

    private boolean validar(Sede dato) {
        return dato != null && dato.getNombre() != null && !dato.getNombre().isBlank()
                && dato.getMunicipio() != null && !dato.getMunicipio().isBlank();
    }

    /**
     * Lista todas las sedes de la bbdd.
     *
     * @return Todas las sedes, o una lista vacia si no hay resultados o hay un error
     */
    public List<Sede> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SedeEntity s order by s.nombre", SedeEntity.class)
                    .getResultStream()
                    .map(SedeMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca una sede por su id.
     *
     * @param id Identificador de la sede
     * @return La sede encontrada, o Optional.empty() si no existe o hay un error
     */
    public Optional<Sede> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(SedeEntity.class, id)).map(SedeMapper::desdeEntidad);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Inserta una sede nueva dentro de una transaccion.
     *
     * @param dato Datos a guardar (nombre y municipio)
     * @return El id generado, o 0 si los datos no son validos, se incumple
     *         una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Sede dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SedeEntity entidad = SedeMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdSede(entidad.getIdSede());
                tx.commit();
                return entidad.getIdSede();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("insertar", e);
            return 0;
        }
    }

    /**
     * Actualiza una sede que ya existe dentro de una transaccion.
     *
     * @param dato Datos nuevos, con su id
     * @return 1 si se ha actualizado, o 0 si no existe, los datos no son validos
     *         o hay un error (se hace rollback)
     */
    public int actualizar(Sede dato) {
        if (!validar(dato) || dato.getIdSede() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SedeEntity s = em.find(SedeEntity.class, dato.getIdSede());
                if (s == null) {
                    tx.rollback();
                    return 0;
                }
                s.setNombre(dato.getNombre());
                s.setMunicipio(dato.getMunicipio());
                tx.commit();
                return 1;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("actualizar", e);
            return 0;
        }
    }

    /**
     * Borra una sede por su id.
     *
     * @param id Identificador de la sede
     * @return 1 si se ha borrado, o 0 si no existe, otras tablas dependen de
     *         este registro (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(int id) {
        if (id < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SedeEntity s = em.find(SedeEntity.class, id);
                if (s == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(s);
                tx.commit();
                return 1;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("eliminar", e);
            return 0;
        }
    }

    /**
     * Lista las salas de una sede.
     *
     * @param idSede Identificador de la sede
     * @return Filas con [0] nombre de la sala y [1] aforo, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> salasPorSede(int idSede) {
        if (idSede < 1) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select sa.nombre, sa.aforo from SalaEntity sa where sa.idSede = :idSede order by sa.nombre", Object[].class)
                    .setParameter("idSede", idSede)
                    .getResultList();
        } catch (Exception e) {
            registrar("salasPorSede", e);
            return List.of();
        }
    }

    /**
     * Cuenta las salas y suma el aforo de cada sede, tambien de las sedes sin salas (C12).
     *
     * @return Filas con [0] sede, [1] total de salas y [2] aforo total, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> aforoTotalPorSede() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select se.nombre, count(sa.idSala), coalesce(sum(sa.aforo), 0) from SedeEntity se "
                    + "left join SalaEntity sa on sa.idSede = se.idSede "
                    + "group by se.idSede, se.nombre order by se.nombre", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("aforoTotalPorSede", e);
            return List.of();
        }
    }

    /**
     * Cierra la EntityManagerFactory del DAO.
     */
    @Override
    public void close() {
        if (managerFactory.isOpen()) managerFactory.close();
    }
}
