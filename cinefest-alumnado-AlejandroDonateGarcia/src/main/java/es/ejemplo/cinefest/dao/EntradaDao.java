package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Entrada;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.EntradaEntity;
import es.ejemplo.cinefest.orm.mapper.EntradaMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AlejandroDonGar
 * Acceso a la tabla entradas con JPA (EntityManager y JPQL).
 * Los errores se registran en el log y no se lanzan: los metodos
 * devuelven 0, Optional.empty() o una lista vacia.
 */
public class EntradaDao implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(EntradaDao.class.getName());
    private static final List<String> ESTADOS = List.of("PAGADA", "USADA", "CANCELADA");
    private final EntityManagerFactory managerFactory;

    /**
     * Crea el DAO y su EntityManagerFactory.
     *
     * @param jdbcUrl URL de la base de datos SQLite
     */
    public EntradaDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    private void registrar(String metodo, Exception ex) {
        LOG.log(Level.WARNING, metodo + " no se pudo completar: " + ex.getMessage());
    }

    private boolean validar(Entrada dato) {
        return dato != null && dato.getIdSesion() != null && dato.getIdEspectador() != null
                && dato.getFechaCompra() != null && ESTADOS.contains(dato.getEstado());
    }

    /**
     * Lista todas las entradas de la bbdd.
     *
     * @return Todas las entradas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Entrada> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e from EntradaEntity e order by e.idEntrada", EntradaEntity.class)
                    .getResultStream()
                    .map(EntradaMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca una entrada por su id.
     *
     * @param id Identificador de la entrada
     * @return La entrada encontrada, o Optional.empty() si no existe o hay un error
     */
    public Optional<Entrada> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(EntradaEntity.class, id)).map(EntradaMapper::desdeEntidad);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Inserta una entrada nueva dentro de una transaccion.
     *
     * @param dato Datos a guardar (sesion, espectador, fecha de compra y estado)
     * @return El id generado, o 0 si los datos no son validos, se incumple
     *         una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Entrada dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity entidad = EntradaMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdEntrada(entidad.getIdEntrada());
                tx.commit();
                return entidad.getIdEntrada();
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
     * Actualiza una entrada que ya existe dentro de una transaccion.
     *
     * @param dato Datos nuevos, con su id
     * @return 1 si se ha actualizado, o 0 si no existe, los datos no son validos
     *         o hay un error (se hace rollback)
     */
    public int actualizar(Entrada dato) {
        if (!validar(dato) || dato.getIdEntrada() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity e = em.find(EntradaEntity.class, dato.getIdEntrada());
                if (e == null) {
                    tx.rollback();
                    return 0;
                }
                e.setIdSesion(dato.getIdSesion());
                e.setIdEspectador(dato.getIdEspectador());
                e.setFechaCompra(dato.getFechaCompra());
                e.setEstado(dato.getEstado());
                tx.commit();
                return 1;
            } catch (Exception ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        } catch (Exception e) {
            registrar("actualizar", e);
            return 0;
        }
    }

    /**
     * Borra una entrada por su id.
     *
     * @param id Identificador de la entrada
     * @return 1 si se ha borrado, o 0 si no existe, otras tablas dependen de
     *         este registro (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(int id) {
        if (id < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity e = em.find(EntradaEntity.class, id);
                if (e == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(e);
                tx.commit();
                return 1;
            } catch (Exception ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        } catch (Exception e) {
            registrar("eliminar", e);
            return 0;
        }
    }

    /**
     * Lista las entradas con el espectador y la pelicula (C08).
     *
     * @return Filas con [0] id de la entrada, [1] espectador, [2] pelicula y [3] estado, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> entradasConDetalles() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e.idEntrada, es.nombre, p.titulo, e.estado from EntradaEntity e "
                    + "join EspectadorEntity es on es.idEspectador = e.idEspectador "
                    + "join SesionEntity s on s.idSesion = e.idSesion "
                    + "join PeliculaEntity p on p.idPelicula = s.idPelicula "
                    + "order by e.idEntrada", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("entradasConDetalles", e);
            return List.of();
        }
    }

    /**
     * Historial de entradas de un espectador (C24).
     *
     * @param idEspectador Identificador del espectador
     * @return Filas con [0] pelicula, [1] fecha de la sesion, [2] estado y [3] precio, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> entradasPorEspectador(int idEspectador) {
        if (idEspectador < 1) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p.titulo, s.fecha, e.estado, s.precio from EntradaEntity e "
                    + "join SesionEntity s on s.idSesion = e.idSesion "
                    + "join PeliculaEntity p on p.idPelicula = s.idPelicula "
                    + "where e.idEspectador = :idEspectador "
                    + "order by s.fecha, p.titulo", Object[].class)
                    .setParameter("idEspectador", idEspectador)
                    .getResultList();
        } catch (Exception e) {
            registrar("entradasPorEspectador", e);
            return List.of();
        }
    }

    /**
     * Cuenta las entradas validas y suma lo recaudado por dia de compra (C23).
     *
     * @return Filas con [0] fecha de compra, [1] entradas validas y [2] total, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> ventasPorDia() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e.fechaCompra, count(e.idEntrada), sum(s.precio) from EntradaEntity e "
                    + "join SesionEntity s on s.idSesion = e.idSesion "
                    + "where e.estado in ('PAGADA', 'USADA') "
                    + "group by e.fechaCompra order by e.fechaCompra", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("ventasPorDia", e);
            return List.of();
        }
    }

    /**
     * Cancela una entrada cambiando su estado a CANCELADA, sin borrarla (C30).
     *
     * @param idEntrada Identificador de la entrada
     * @return 1 si se ha cancelado, o 0 si no existe, ya estaba cancelada o hay un error
     */
    public int cancelarEntrada(int idEntrada) {
        if (idEntrada < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity e = em.find(EntradaEntity.class, idEntrada);
                if (e == null || "CANCELADA".equals(e.getEstado())) {
                    tx.rollback();
                    return 0;
                }
                e.setEstado("CANCELADA");
                tx.commit();
                return 1;
            } catch (Exception ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        } catch (Exception e) {
            registrar("cancelarEntrada", e);
            return 0;
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
