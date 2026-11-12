package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Espectador;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.EspectadorEntity;
import es.ejemplo.cinefest.orm.mapper.EspectadorMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AlejandroDonGar
 * Acceso a la tabla espectadores con JPA (EntityManager y JPQL).
 * Los errores se registran en el log y no se lanzan: los metodos
 * devuelven 0, Optional.empty() o una lista vacia.
 */
public class EspectadorDao implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(EspectadorDao.class.getName());
    private final EntityManagerFactory managerFactory;

    /**
     * Crea el DAO y su EntityManagerFactory.
     *
     * @param jdbcUrl URL de la base de datos SQLite
     */
    public EspectadorDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    private void registrar(String metodo, Exception ex) {
        LOG.log(Level.WARNING, metodo + " no se pudo completar: " + ex.getMessage());
    }

    private boolean validar(Espectador dato) {
        return dato != null && dato.getNombre() != null && !dato.getNombre().isBlank()
                && dato.getEmail() != null && !dato.getEmail().isBlank() && dato.getFechaAlta() != null
                && dato.getActivo() != null && (dato.getActivo() == 0 || dato.getActivo() == 1);
    }

    /**
     * Lista todos los espectadores de la bbdd.
     *
     * @return Todos los espectadores, o una lista vacia si no hay resultados o hay un error
     */
    public List<Espectador> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es from EspectadorEntity es order by es.fechaAlta, es.nombre", EspectadorEntity.class)
                    .getResultStream()
                    .map(EspectadorMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca un espectador por su id.
     *
     * @param id Identificador del espectador
     * @return El espectador encontrado, o Optional.empty() si no existe o hay un error
     */
    public Optional<Espectador> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(EspectadorEntity.class, id)).map(EspectadorMapper::desdeEntidad);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Inserta un espectador nuevo dentro de una transaccion.
     *
     * @param dato Datos a guardar (nombre, email, fecha de alta y activo 0 o 1)
     * @return El id generado, o 0 si los datos no son validos, se incumple
     *         una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Espectador dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EspectadorEntity entidad = EspectadorMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdEspectador(entidad.getIdEspectador());
                tx.commit();
                return entidad.getIdEspectador();
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
     * Actualiza un espectador que ya existe dentro de una transaccion.
     *
     * @param dato Datos nuevos, con su id
     * @return 1 si se ha actualizado, o 0 si no existe, los datos no son validos
     *         o hay un error (se hace rollback)
     */
    public int actualizar(Espectador dato) {
        if (!validar(dato) || dato.getIdEspectador() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EspectadorEntity es = em.find(EspectadorEntity.class, dato.getIdEspectador());
                if (es == null) {
                    tx.rollback();
                    return 0;
                }
                es.setNombre(dato.getNombre());
                es.setEmail(dato.getEmail());
                es.setFechaAlta(dato.getFechaAlta());
                es.setActivo(dato.getActivo());
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
     * Borra un espectador por su id.
     *
     * @param id Identificador del espectador
     * @return 1 si se ha borrado, o 0 si no existe, otras tablas dependen de
     *         este registro (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(int id) {
        if (id < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EspectadorEntity es = em.find(EspectadorEntity.class, id);
                if (es == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(es);
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
     * Busca un espectador por su email.
     *
     * @param email Email del espectador
     * @return El espectador, o Optional.empty() si no existe, el email esta vacio o hay un error
     */
    public Optional<Espectador> buscarPorEmail(String email) {
        if (email == null || email.isBlank()) return Optional.empty();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es from EspectadorEntity es where es.email = :email", EspectadorEntity.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .map(EspectadorMapper::desdeEntidad);
        } catch (Exception e) {
            registrar("buscarPorEmail", e);
            return Optional.empty();
        }
    }

    /**
     * Lista los espectadores que nunca han comprado una entrada (C09).
     *
     * @return Los espectadores sin entradas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Espectador> espectadoresSinEntradas() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es from EspectadorEntity es where not exists "
                    + "(select e from EntradaEntity e where e.idEspectador = es.idEspectador) order by es.nombre", EspectadorEntity.class)
                    .getResultStream()
                    .map(EspectadorMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("espectadoresSinEntradas", e);
            return List.of();
        }
    }

    /**
     * Ranking de espectadores por entradas no canceladas, incluidos los que tienen 0 (C13).
     *
     * @return Filas con [0] nombre y [1] total de entradas, de mas a menos, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> rankingEspectadores() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es.nombre, count(e.idEntrada) from EspectadorEntity es "
                    + "left join EntradaEntity e on e.idEspectador = es.idEspectador and e.estado in ('PAGADA', 'USADA') "
                    + "group by es.idEspectador, es.nombre "
                    + "order by count(e.idEntrada) desc, es.nombre", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("rankingEspectadores", e);
            return List.of();
        }
    }

    /**
     * Lista los espectadores con entradas validas en un minimo de sedes distintas (C20).
     *
     * @param minimo Numero minimo de sedes
     * @return Filas con [0] nombre y [1] sedes distintas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> espectadoresVariasSedes(int minimo) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es.nombre, count(distinct sa.idSede) from EntradaEntity e "
                    + "join EspectadorEntity es on es.idEspectador = e.idEspectador "
                    + "join SesionEntity s on s.idSesion = e.idSesion "
                    + "join SalaEntity sa on sa.idSala = s.idSala "
                    + "where e.estado in ('PAGADA', 'USADA') "
                    + "group by es.idEspectador, es.nombre having count(distinct sa.idSede) >= :minimo "
                    + "order by count(distinct sa.idSede) desc, es.nombre", Object[].class)
                    .setParameter("minimo", (long) minimo)
                    .getResultList();
        } catch (Exception e) {
            registrar("espectadoresVariasSedes", e);
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
