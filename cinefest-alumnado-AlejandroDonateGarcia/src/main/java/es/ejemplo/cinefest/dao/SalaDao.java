package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sala;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.SalaEntity;
import es.ejemplo.cinefest.orm.mapper.SalaMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AlejandroDonGar
 * Acceso a la tabla salas con JPA (EntityManager y JPQL).
 * Los errores se registran en el log y no se lanzan: los metodos
 * devuelven 0, Optional.empty() o una lista vacia.
 */
public class SalaDao implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(SalaDao.class.getName());
    private final EntityManagerFactory managerFactory;

    /**
     * Crea el DAO y su EntityManagerFactory.
     *
     * @param jdbcUrl URL de la base de datos SQLite
     */
    public SalaDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    private void registrar(String metodo, Exception ex) {
        LOG.log(Level.WARNING, metodo + " no se pudo completar: " + ex.getMessage());
    }

    private boolean validar(Sala dato) {
        return dato != null && dato.getNombre() != null && !dato.getNombre().isBlank()
                && dato.getAforo() != null && dato.getAforo() > 0 && dato.getIdSede() != null;
    }

    /**
     * Lista todas las salas de la bbdd.
     *
     * @return Todas las salas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Sala> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select sa from SalaEntity sa order by sa.idSede, sa.nombre", SalaEntity.class)
                    .getResultStream()
                    .map(SalaMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca una sala por su id.
     *
     * @param id Identificador de la sala
     * @return La sala encontrada, o Optional.empty() si no existe o hay un error
     */
    public Optional<Sala> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(SalaEntity.class, id)).map(SalaMapper::desdeEntidad);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Inserta una sala nueva dentro de una transaccion.
     *
     * @param dato Datos a guardar (nombre, aforo mayor que 0 y sede)
     * @return El id generado, o 0 si los datos no son validos, se incumple
     *         una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Sala dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SalaEntity entidad = SalaMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdSala(entidad.getIdSala());
                tx.commit();
                return entidad.getIdSala();
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
     * Actualiza una sala que ya existe dentro de una transaccion.
     *
     * @param dato Datos nuevos, con su id
     * @return 1 si se ha actualizado, o 0 si no existe, los datos no son validos
     *         o hay un error (se hace rollback)
     */
    public int actualizar(Sala dato) {
        if (!validar(dato) || dato.getIdSala() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SalaEntity sa = em.find(SalaEntity.class, dato.getIdSala());
                if (sa == null) {
                    tx.rollback();
                    return 0;
                }
                sa.setNombre(dato.getNombre());
                sa.setAforo(dato.getAforo());
                sa.setIdSede(dato.getIdSede());
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
     * Borra una sala por su id.
     *
     * @param id Identificador de la sala
     * @return 1 si se ha borrado, o 0 si no existe, otras tablas dependen de
     *         este registro (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(int id) {
        if (id < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SalaEntity sa = em.find(SalaEntity.class, id);
                if (sa == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(sa);
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
     * Lista las salas con el nombre de su sede (C06).
     *
     * @return Filas con [0] sede, [1] sala y [2] aforo, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> salasConSede() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select se.nombre, sa.nombre, sa.aforo from SalaEntity sa "
                    + "join SedeEntity se on se.idSede = sa.idSede "
                    + "order by se.nombre, sa.nombre", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("salasConSede", e);
            return List.of();
        }
    }

    /**
     * Lista las salas que no tienen ninguna sesion PROGRAMADA (C28).
     *
     * @return Las salas sin sesiones programadas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Sala> salasSinSesiones() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select sa from SalaEntity sa where not exists "
                    + "(select s from SesionEntity s where s.idSala = sa.idSala and s.estado = 'PROGRAMADA') "
                    + "order by sa.idSede, sa.nombre", SalaEntity.class)
                    .getResultStream()
                    .map(SalaMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("salasSinSesiones", e);
            return List.of();
        }
    }

    /**
     * Muestra por sala cuantas sesiones tiene y cuantas entradas validas (PAGADA o USADA) ha vendido.
     *
     * @return Filas con [0] sede, [1] sala, [2] aforo, [3] sesiones y [4] entradas validas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> ocupacionPorSala() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select se.nombre, sa.nombre, sa.aforo, count(distinct s.idSesion), count(e.idEntrada) from SalaEntity sa "
                    + "join SedeEntity se on se.idSede = sa.idSede "
                    + "left join SesionEntity s on s.idSala = sa.idSala "
                    + "left join EntradaEntity e on e.idSesion = s.idSesion and e.estado in ('PAGADA', 'USADA') "
                    + "group by sa.idSala, se.nombre, sa.nombre, sa.aforo "
                    + "order by se.nombre, sa.nombre", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("ocupacionPorSala", e);
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
